package com.jangbee.stat;

import com.jangbee.firm.FirmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by test on 2019-06-07.
 */
@Service
public class FirmStatService {

    @Autowired
    FirmRepository firmRepository;


    public Map<String, Map> getFirmLocalCount(String equipment) {
        String equipmentLikeQuery = "% "+equipment;
        List<StatDto.FirmLocalCount> firmLocalCounts =  firmRepository.getFirmLocalCount(equipmentLikeQuery);

        Map<String, Map> countData = new HashMap();

        for(StatDto.FirmLocalCount localCount : firmLocalCounts) {
            String model = localCount.model;

            Map<String, Integer> localData;
            if(countData.get(model) == null) {
                localData = new HashMap();
                localData.put(localCount.sido, localCount.firmCount);
                countData.put(model, localData);
            } else {
                localData = countData.get(model);
                localData.put(localCount.sido, localCount.firmCount);
            }
        }

        return countData;
    }
}
