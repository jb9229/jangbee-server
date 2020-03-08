package com.jangbee.payment;

import com.jangbee.utils.RestTemplateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.Charset;

/**
 * Created by kosac on 08/03/2020.
 */
@Service
public class PaymentService {
    @Autowired
    RestTemplateUtils restTemplateUtils;
    @Value( "${kakao.payment.ak}" )
    private String kakaoPGAK;
    @Value( "${kakao.payment.approval-url}" )
    private String kakaoPGApprovalUrl;

    public PaymentDto.ApprovalResponse requestApproval(PaymentDto.Approval data) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
        try {
            requestBody.add("cid", "TCSUBSCRIP");
//            requestBody.add("cid_secret", obClinetPw);
            requestBody.add("pg_token", data.getPgToken());
            requestBody.add("tid", data.getTid());
            requestBody.add("partner_order_id", data.getPartnerOrderId());
            requestBody.add("partner_user_id", data.getPartnerUserId());
            ResponseEntity<PaymentDto.ApprovalResponse> approvalResult = restTemplateUtils.postForObject(kakaoPGApprovalUrl, requestBody, kakaoPGAK, PaymentDto.ApprovalResponse.class, new MediaType(MediaType.APPLICATION_FORM_URLENCODED, Charset.forName("UTF-8")));

            if (approvalResult.getStatusCodeValue() == 200) {
                PaymentDto.ApprovalResponse tokenResult = approvalResult.getBody();

                if (tokenResult != null && tokenResult.getSid() != null && !tokenResult.getSid().isEmpty()) {
                    return tokenResult;
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }

        return null;
    }
}
