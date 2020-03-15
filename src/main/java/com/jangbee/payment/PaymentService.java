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
    @Value( "${kakao.payment.ready-url}" )
    private String kakaoPGReadyUrl;
    @Value( "${kakao.payment.approval-url}" )
    private String kakaoPGApprovalUrl;
    @Value( "${kakao.payment.callback-approval}" )
    private String kakaoPGCallbackApproval;
    @Value( "${kakao.payment.callback-fail}" )
    private String kakaoPGCallbackFail;
    @Value( "${kakao.payment.callback-cancel}" )
    private String kakaoPGCallbackCancel;


    public PaymentDto.ReadyResponse requestReady(PaymentDto.Ready data) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
        try {
            requestBody.add("cid", "TCSUBSCRIP");
            requestBody.add("partner_order_id", data.getPartnerOrderId());
            requestBody.add("partner_user_id", data.getPartnerUserId());

            requestBody.add("item_name", data.getItemName());
            requestBody.add("quantity", "1");
            requestBody.add("total_amount", data.getTotalAmount());
            requestBody.add("tax_free_amount", "0");
            requestBody.add("approval_url", kakaoPGCallbackApproval);
            requestBody.add("fail_url", kakaoPGCallbackFail);
            requestBody.add("cancel_url", kakaoPGCallbackCancel);

            ResponseEntity<PaymentDto.ReadyResponse> readyResult = restTemplateUtils.postForObject(kakaoPGReadyUrl, requestBody, kakaoPGAK, PaymentDto.ReadyResponse.class, new MediaType(MediaType.APPLICATION_FORM_URLENCODED, Charset.forName("UTF-8")));

            if (readyResult.getStatusCodeValue() == 200) {
                PaymentDto.ReadyResponse tokenResult = readyResult.getBody();

                if (tokenResult != null && tokenResult.getNext_redirect_mobile_url() != null && !tokenResult.getNext_redirect_mobile_url().isEmpty()) {
                    return tokenResult;
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }

        return null;
    }

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
