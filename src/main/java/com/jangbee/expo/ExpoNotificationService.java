package com.jangbee.expo;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseException;
import com.jangbee.accounts.AccountDto;
import com.jangbee.ad.AdDto;
import com.jangbee.firm.Firm;
import com.jangbee.utils.RestTemplateUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by test on 2019-03-12.
 * push test
 */
@Service
public class ExpoNotificationService {

    @Autowired RestTemplateUtils restTemplateUtils;

    @Value( "${expo.push.endpoint}" )
    private String expoPushEndpoint;

    public ResponseEntity sendSingle(String token, String title, String body, String notiType){
        JSONObject pushJSON = makeMessage(title, body, token, notiType);
        String jsonStr = pushJSON.toString();
        ResponseEntity<Object> responseEntity = restTemplateUtils.postForObject(expoPushEndpoint, jsonStr, null, AdDto.TransferWithdrawResponse.class, MediaType.APPLICATION_JSON_UTF8);

        return responseEntity;
    }

    public ResponseEntity sendMulti(List<String> tokenList, String title, String body, String notiType){
        JSONArray jsonArray = new JSONArray();

        if(tokenList.isEmpty()){return null;}

        for(String token : tokenList){
            JSONObject pushJSON = makeMessage(title, body, token, notiType);

            jsonArray.put(pushJSON);
        }
        String jsonStr = jsonArray.toString();
        ResponseEntity<Object> responseEntity = restTemplateUtils.postForObject(expoPushEndpoint, jsonStr, null, AdDto.TransferWithdrawResponse.class, MediaType.APPLICATION_JSON_UTF8);

        return responseEntity;
    }

    public List getTokenList(List<Firm> noticeFirmList, DataSnapshot dataSnapshot) {
        List tokenList = new ArrayList();

        for (Firm firm : noticeFirmList) {
            DataSnapshot dSnapshot = dataSnapshot.child(firm.getAccountId());
            try {
                AccountDto.FirebaseUser user = dSnapshot.getValue(AccountDto.FirebaseUser.class); // for(DataSnapshot ds : dataSnapshot.getChildren()) {} .child("Address")
                if (user.getExpoPushToken() != null) {
                    tokenList.add(user.getExpoPushToken());
                }
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
        }

        return tokenList;
    }

    private JSONObject makeMessage(String title, String body, String token, String notiType) {
        JSONObject pushJSON = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("title", title);
        data.put("body", body);
        if(notiType != null) { data.put("notice", notiType); }
        try {
            pushJSON.put("to", token);
            pushJSON.put("title", title);
            pushJSON.put("body", body);
            pushJSON.put("channelId", "jbcall-messages");
            pushJSON.put("sound", "default");
            pushJSON.put("badge", 1);
            pushJSON.put("data", data.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pushJSON;
    }
}
