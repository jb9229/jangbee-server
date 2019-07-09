package com.jangbee.stat;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by test on 2019-06-07.
 */
@RestController
@RequestMapping("/api/v1/")
public class FirmStatController {
    static String[] STAT_LOCAL_LIST = {"서울", "부산", "경기", "인천", "세종특별자치시", "대전", "광주", "대구", "울산", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주특별자치도"};
    static Map<String, String[]> STAT_EQUI_MODEL_LIST = new HashMap();
    @PostConstruct
    public void init() { //"15톤",
        String[] crainModel = {"5톤", "10톤", "13톤", "25톤", "50톤", "100톤", "160톤", "200톤", "250톤", "300톤", "400톤", "500톤", "700톤", "800톤", "1200톤"};
        String[] cagoModel = {"5톤", "11톤", "18톤", "25톤"};
        String[] porkrainModel = {"미니", "02W", "03W", "06W", "08W", "02LC", "04LC", "06LC"};
        String[] skyModel = {"1톤", "1.2톤", "2톤", "2.5톤", "3.5톤", "5톤", "28m", "45m", "58m", "60m", "75m"};
        String[] liftModel = {"2톤", "2.5톤", "3톤", "4.5톤", "5톤", "6톤", "7톤", "8톤", "11.5톤", "15톤", "18톤", "25톤"};
        STAT_EQUI_MODEL_LIST.put("카고크레인", cagoModel);
        STAT_EQUI_MODEL_LIST.put("크레인", crainModel);
        STAT_EQUI_MODEL_LIST.put("굴착기", porkrainModel);
        STAT_EQUI_MODEL_LIST.put("스카이", skyModel);
        STAT_EQUI_MODEL_LIST.put("지게차", liftModel);
    }

    @Autowired FirmStatService service;

    @RequestMapping(value="stat", method = RequestMethod.GET)
    public ResponseEntity firmLocalCount(@RequestParam String equipment) {

        Map<String, Map> countData = service.getFirmLocalCount(equipment);

        String[] modelList = STAT_EQUI_MODEL_LIST.get(equipment);
        JSONArray array = new JSONArray();
        for(String model : modelList) {
            Map<String, Integer> localData = countData.get(model);
            List localCnt = new ArrayList();
            for (String local : STAT_LOCAL_LIST) {
                if (localData == null || localData.get(local) == null) {
                    localCnt.add(0);
                } else {
                    localCnt.add(localData.get(local));
                }
            }
            JSONObject obj = new JSONObject();
            obj.put("model", model);
            obj.put("array", localCnt);
            array.put(obj);
        }

        return new ResponseEntity<>(array.toString(), HttpStatus.OK);
    }
}
