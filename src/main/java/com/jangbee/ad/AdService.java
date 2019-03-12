package com.jangbee.ad;

import com.jangbee.firm.Firm;
import com.jangbee.firm.FirmService;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
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

    public List<Ad> getByAdType(AdType adType, String equiTarget, String sidoTarget, String gugunTarget) {
        List<Ad> adList = repository.getByAdTypeAndEquiTargetAndSidoTargetAndGugunTarget(adType, equiTarget, sidoTarget, gugunTarget);

        return adList;
    }

    public List<Ad> getByAccountId(String accountId) {
        List<Ad> adList = repository.getByAccountId(accountId);

        return adList;
    }

    public Ad createAd(AdDto.Create create) {
        Ad newAd = this.modelMapper.map(create, Ad.class);

        Calendar tokenExpDateCal = Calendar.getInstance();
        tokenExpDateCal.add(Calendar.MONTH, 3);
        newAd.setObAcctokenExpdate(tokenExpDateCal.getTime());

        tokenExpDateCal.add(Calendar.MONTH, 9);
        newAd.setObAcctokenDiscdate(tokenExpDateCal.getTime());

        return repository.save(newAd);
    }

    public boolean obTransferWithdraw(String fintechUseNum, int price) {
        JSONObject userJSON = new JSONObject();
        try {
            userJSON.put("dps_print_content", "장비콜 광고비");
            userJSON.put("fintech_use_num", fintechUseNum);
            userJSON.put("tran_amt", price);
            LocalDateTime ldt = LocalDateTime.now();
            DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss", Locale.KOREA);
            userJSON.put("tran_dtime", formmat1.format(ldt));
            ResponseEntity<AdDto.TransferWithdrawResponse> withdrawResult = restTemplateUtils.postForObject(adWithdrawUrl, userJSON.toString(), AdDto.TransferWithdrawResponse.class, MediaType.APPLICATION_JSON);

            if (withdrawResult.getStatusCodeValue() == 200) {
                AdDto.TransferWithdrawResponse tranResult = withdrawResult.getBody();

                if (tranResult.getRsp_code() == "A0000") {
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

    public boolean refreshObtoken(Ad ad) throws JSONException {
        try {
            JSONObject userJSON = new JSONObject();
            userJSON.put("client_id", obClientId);
            userJSON.put("client_secret", obClientSecret);
            userJSON.put("refresh_token", ad.getObRefreshToken());
            userJSON.put("scope", "login inquiry transfer");
            userJSON.put("grant_type", "refresh_token");

            ResponseEntity<AdDto.RefreshTokenResponse> withdrawResult = restTemplateUtils.postForObject(obtokenUrl, userJSON.toString(), AdDto.RefreshTokenResponse.class, MediaType.APPLICATION_FORM_URLENCODED);
            if (withdrawResult.getStatusCodeValue() == 200) {
                AdDto.RefreshTokenResponse tranResult = withdrawResult.getBody();

                ad.setObAccToken(tranResult.getAccess_token());
                ad.setObRefreshToken(tranResult.getRefresh_token());

                Calendar tokenExpDateCal = Calendar.getInstance();
                tokenExpDateCal.add(Calendar.MONTH, 3);
                ad.setObAcctokenExpdate(tokenExpDateCal.getTime());
                repository.saveAndFlush(ad);
                return true;
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }

        return false;
    }
}
