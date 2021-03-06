package com.jangbee.openbank;

import com.jangbee.ad.AdDto;
import com.jangbee.utils.RestTemplateUtils;
import org.json.JSONArray;
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

/**
 * Created by test on 2019-05-21.
 */
@Service
public class OpenbankService {
    @Autowired
    AdWithdrawHistRepository withHistRepository;
    @Autowired
    DepositHistRepository depositHistRepository;
    @Autowired private ModelMapper modelMapper;

    @Autowired
    RestTemplateUtils restTemplateUtils;
    @Value( "${openbankapi.transfer.withdraw}" )
    private String adWithdrawUrl;
    @Value( "${openbankapi.token}" )
    private String tokenUrl;
    @Value( "${openbankapi.transfer.deposit}" )
    private String adDepositUrl;
    @Value( "${openbank.client.id}" )
    private String obClinetId;
    @Value( "${openbank.client.secret}" )
    private String obClinetPw;

    public OpenbankDto.OobAccTokenResponse requestOobToken() {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
        try {
            requestBody.add("client_id", obClinetId);
            requestBody.add("client_secret", obClinetPw);
            requestBody.add("scope", "oob");
            requestBody.add("grant_type", "client_credentials");
            ResponseEntity<OpenbankDto.OobAccTokenResponse> withdrawResult = restTemplateUtils.postForObject(tokenUrl, requestBody, null, OpenbankDto.OobAccTokenResponse.class, new MediaType(MediaType.APPLICATION_FORM_URLENCODED, Charset.forName("UTF-8")));

            if (withdrawResult.getStatusCodeValue() == 200) {
                OpenbankDto.OobAccTokenResponse tokenResult = withdrawResult.getBody();

                if (tokenResult != null && tokenResult.getAccess_token() != null && !tokenResult.getAccess_token().isEmpty()) {
                    return tokenResult;
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }

        return null;
    }

    public boolean diposit(String authToken, String fintechUseNum, String printContent, int tranAmt) {
        JSONObject userJSON = new JSONObject();

        JSONObject tranJSON = new JSONObject();
        JSONArray tranArrJSON = new JSONArray();
        String WD_PASS_PHRASE = "NONE";

        userJSON.put("wd_pass_phrase", WD_PASS_PHRASE);
        userJSON.put("wd_print_content", printContent);
        userJSON.put("name_check_option", "off");
        userJSON.put("req_cnt", "1");
        tranJSON.put("tran_no", "1");
        tranJSON.put("fintech_use_num", fintechUseNum);
        tranJSON.put("print_content", printContent);
        tranJSON.put("tran_amt", ""+tranAmt);
        tranArrJSON.put(tranJSON);
        userJSON.put("req_list", tranArrJSON);
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss", Locale.KOREA);
        userJSON.put("tran_dtime", formmat1.format(ldt));
        ResponseEntity<AdDto.TransferDepositResponse> depositResult = restTemplateUtils.postForObject(adDepositUrl, userJSON.toString(), authToken, AdDto.TransferDepositResponse.class, MediaType.APPLICATION_JSON);

        if (depositResult.getStatusCodeValue() == 200) {
            AdDto.TransferDepositResponse tranResult = depositResult.getBody();

            if (tranResult.getRsp_code().equals("A0000")) {
                DepositHistory depositHistory = modelMapper.map(tranResult.getRes_list().get(0), DepositHistory.class);
                depositHistRepository.save(depositHistory);

                return true;
            } else {
                String erroMsg = "["+tranResult.getRsp_code()+"] "+tranResult.getRsp_message();
                throw new OpenbackCommonException("["+printContent+"]에 실패했습니다! 사유를 확인 후 재시도 해주세요: "+erroMsg);
            }
        } else {
            throw new OpenbackCommonException("'"+printContent+"'오픈뱅크 요청에 실패했습니다!HTTP STATUS CODE: "+depositResult.getStatusCodeValue());
        }
    }
}
