package com.jangbee.ad;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.jangbee.utils.RestTemplateUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.uid;

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
        if(adLocation.equals(AdLocation.MAIN)){
            return repository.getByAdLocationOrderByAdType(adLocation);
        }

        return repository.getByAdLocationAndEquiTargetAndSidoTargetAndGugunTarget(adLocation, equiTarget, sidoTarget, gugunTarget);
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

    public boolean refreshObtoken(String authToken, String refreshToken, Ad ad) throws JSONException {
        try {
            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
            requestBody.add("client_id", obClientId);
            requestBody.add("client_secret", obClientSecret);
            requestBody.add("refresh_token", refreshToken);
            requestBody.add("scope", "login inquiry transfer");
            requestBody.add("grant_type", "refresh_token");

            ResponseEntity<AdDto.RefreshTokenResponse> withdrawResult = restTemplateUtils.postForObject(obtokenUrl, requestBody, null, AdDto.RefreshTokenResponse.class, new MediaType(MediaType.APPLICATION_FORM_URLENCODED, Charset.forName("UTF-8")));
            if (withdrawResult.getStatusCodeValue() == 200) {
                AdDto.RefreshTokenResponse refreshResult = withdrawResult.getBody();

                if (refreshResult == null) {return false;}
                String respCode = refreshResult.getRsp_code();
                if (respCode != null && respCode.equals("O0001")) {return false;}

                Map<String,Object> update = new HashMap<String,Object>();
                update.put("/users/" + ad.getAccountId() + "/obAccessToken", refreshResult.getAccess_token());
                update.put("/users/" + ad.getAccountId() + "/obRefreshToken", refreshResult.getRefresh_token());
                update.put("/users/" + ad.getAccountId() + "/obAccTokenExpDate", refreshResult.getAccTokenExpDate());

                FirebaseDatabase.getInstance().getReference().updateChildren(update, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        // TODO Event Driven Return
                    }
                });

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
