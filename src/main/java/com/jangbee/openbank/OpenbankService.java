package com.jangbee.openbank;

import com.jangbee.ad.AdDto;
import com.jangbee.ad.AdWithdrawHistRepository;
import com.jangbee.ad.AdWithdrawHistory;
import com.jangbee.utils.RestTemplateUtils;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Created by test on 2019-05-21.
 */
@Service
public class OpenbankService {
    @Autowired
    AdWithdrawHistRepository withHistRepository;
    @Autowired private ModelMapper modelMapper;

    @Autowired
    RestTemplateUtils restTemplateUtils;
    @Value( "${openbankapi.transfer.withdraw}" )
    private String adWithdrawUrl;

    public boolean withdraw(String authToken, String fintechUseNum, String printContent, int tranAmt) {
        JSONObject userJSON = new JSONObject();
        try {
            userJSON.put("dps_print_content", printContent);
            userJSON.put("fintech_use_num", fintechUseNum);
            userJSON.put("tran_amt", tranAmt);
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
}
