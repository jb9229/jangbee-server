package com.jangbee.expo;

import com.jangbee.ad.AdDto;
import com.jangbee.utils.RestTemplateUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by test on 2019-03-12.
 */
@Service
public class ExpoNotificationService {

    @Autowired RestTemplateUtils restTemplateUtils;

    @Value( "${expo.push.endpoint}" )
    private String expoPushEndpoint;

    public ResponseEntity sendSingle(String token, String title, String body){
        JSONObject pushJSON = new JSONObject();
        try {
            pushJSON.put("to", token);
            pushJSON.put("title", title);
            pushJSON.put("body", body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonStr = pushJSON.toString();
        ResponseEntity<Object> responseEntity = restTemplateUtils.postForObject(expoPushEndpoint, jsonStr, AdDto.TransferWithdrawResponse.class, MediaType.APPLICATION_JSON_UTF8);

        return responseEntity;
    }
}
