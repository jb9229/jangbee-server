package com.jangbee.ad;

import com.jangbee.utils.RestTemplateUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by test on 2019-02-14.
 */
@Service
public class AdService {
    @Autowired AdRepository repository;
    @Autowired AdWithdrawHistRepository withHistRepository;
    @Autowired private ModelMapper modelMapper;
    @Autowired
    RestTemplateUtils restTemplateUtils;
    @Value( "${openbankapi.transfer.withdraw}" )
    private String adWithdrawUrl;
    @Value( "${openbankapi.token}" )
    private String obtokenUrl;
    @Value( "${openbank.client.id}" )
    private String obClientId;
    @Value( "${openbank.client.secret}" )
    private String obClientSecret;

    public List<Ad> getByAdLocation(AdLocation adLocation, String equiTarget, String sidoTarget, String gugunTarget) {
        List<Ad> adList = repository.getByAdLocationAndEquiTargetAndSidoTargetAndGugunTarget(adLocation, equiTarget, sidoTarget, gugunTarget);

        return adList;
    }

    public List<Ad> getByAccountId(String accountId) {
        List<Ad> adList = repository.getByAccountId(accountId);

        return adList;
    }

    public Ad createAd(AdDto.Create create) {
        // i don't know why Numberformatexception occure
        String accountId = create.getAccountId();
        create.setAccountId("");
        Ad newAd = this.modelMapper.map(create, Ad.class);
        newAd.setAccountId(accountId);

        newAd.setAdLocation(calAdLocation(create.getAdType()));
        Calendar nowCal = Calendar.getInstance();
        newAd.setStartDate(nowCal.getTime());

        nowCal.add(Calendar.MONTH, create.getForMonths());
        newAd.setEndDate(nowCal.getTime());

        if(create.getForMonths() > 1){
            Calendar nextWithdrawCal = Calendar.getInstance();
            nextWithdrawCal.add(Calendar.MONTH, 1);
            newAd.setNextWithdrawDate(nextWithdrawCal.getTime());
        }

        return repository.save(newAd);
    }

    private AdLocation calAdLocation(short adType) {
        if(adType < 11){
            return AdLocation.MAIN;
        }else if(adType < 21){
            return AdLocation.EQUIPMENT;
        }else if(adType < 31){
            return AdLocation.LOCAL;
        }

        return AdLocation.SEARCH;
    }

    public boolean obTransferWithdraw(String authToken, String fintechUseNum, int price) {
        /**
         * AuthToken을 FB 접속해서 받아와야 하는데, Firebase Databe에서 결과를 event 메소드 방식이라, Future에 답아 결과를 받아 볼 방법을 찾이 못했음
         */
        JSONObject userJSON = new JSONObject();
        try {
            userJSON.put("dps_print_content", "장비콜 광고비");
            userJSON.put("fintech_use_num", fintechUseNum);
            userJSON.put("tran_amt", price);
            LocalDateTime ldt = LocalDateTime.now();
            DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss", Locale.KOREA);
            userJSON.put("tran_dtime", formmat1.format(ldt));
            ResponseEntity<AdDto.TransferWithdrawResponse> withdrawResult = restTemplateUtils.postForObject(adWithdrawUrl, userJSON.toString(), authToken, AdDto.TransferWithdrawResponse.class, MediaType.APPLICATION_JSON);

            if (withdrawResult.getStatusCodeValue() == 200) {
                AdDto.TransferWithdrawResponse tranResult = withdrawResult.getBody();

                if (tranResult.getRsp_code().equals("A0000")) {
                    AdWithdrawHistory withHistory = modelMapper.map(tranResult, AdWithdrawHistory.class);
                    withHistRepository.save(withHistory);

                    return true;
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }

        return false;
    }

    public boolean refreshObtoken(String authToken, Ad ad) throws JSONException {
        try {
            JSONObject userJSON = new JSONObject();
            userJSON.put("client_id", obClientId);
            userJSON.put("client_secret", obClientSecret);
//            userJSON.put("refresh_token", ad.getObRefreshToken());
            userJSON.put("scope", "login inquiry transfer");
            userJSON.put("grant_type", "refresh_token");

            ResponseEntity<AdDto.RefreshTokenResponse> withdrawResult = restTemplateUtils.postForObject(obtokenUrl, userJSON.toString(), authToken, AdDto.RefreshTokenResponse.class, MediaType.APPLICATION_FORM_URLENCODED);
            if (withdrawResult.getStatusCodeValue() == 200) {
                AdDto.RefreshTokenResponse tranResult = withdrawResult.getBody();

//                ad.setObAccToken(tranResult.getAccess_token());
//                ad.setObRefreshToken(tranResult.getRefresh_token());

                Calendar tokenExpDateCal = Calendar.getInstance();
                tokenExpDateCal.add(Calendar.MONTH, 3);
//                ad.setObAcctokenExpdate(tokenExpDateCal.getTime());
                repository.saveAndFlush(ad);
                return true;
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }

        return false;
    }

    public List<Integer> getBookedAdType() {
        return repository.getBookedAdType();
    }

    /**
     * 장비 타켓광고 중복확인 함수
     * @param equipment
     * @return
     */
    public Ad getByEquiTarget(String equipment) {
        return repository.getByEquiTargetAndAdType(equipment, Ad.ADTYPE_EQUIPMENT_FIRST);
    }

    /**
     * 지역 타켓광고 중복 확인함수
     * @param equipment
     * @param sido
     * @param gungu
     * @return
     */
    public Ad getByLocalTarget(String equipment, String sido, String gungu) {
        return repository.getByEquiTargetAndSidoTargetAndGugunTargetAndAdType(equipment, sido, gungu, Ad.ADTYPE_LOCAL_FIRST);
    }

    public Ad updateAd(AdDto.Update update) {
        Ad ad = repository.getOne(update.getId());

        if (ad == null) {
            throw new AdNotFoundException();
        }

        ad.setTitle(update.getTitle());
        ad.setSubTitle(update.getSubTitle());
        ad.setPhotoUrl(update.getPhotoUrl());
        ad.setTelNumber(update.getTelNumber());
        if (update.getForMonths() > 0) {
            Calendar endDateCal = Calendar.getInstance();
            endDateCal.setTime(ad.getEndDate());

            endDateCal.add(Calendar.MONTH, update.getForMonths());
            ad.setEndDate(endDateCal.getTime());
        }

        return repository.saveAndFlush(ad);
    }

    public Ad terminateAd(Long id) {
        Ad ad = repository.getOne(id);

        if (ad == null) {
            throw new AdNotFoundException();
        }

        ad.setEndDate(ad.getNextWithdrawDate());

        return repository.saveAndFlush(ad);
    }

    public boolean updateAdFintechUseNum(String newFintechUseNum, String accountId) {
        Integer result = repository.updateFintechUseNum(newFintechUseNum, accountId);

        if(result == null || result > 1 ){return false;}

        return true;
    }
}
