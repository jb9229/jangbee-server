package com.jangbee.local;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by test on 2019-02-13.
 */
@Service
public class EquiLocalService {
    @Autowired
    EquiLocalRepository repository;
    public EquipmentLocal get(String equipment, String sido, String sigungu){
        EquipmentLocal equipmentLocal = repository.findByEquipmentAndSidoAndSigungu(equipment, sido, sigungu);

        return equipmentLocal;
    }

    public EquipmentLocal create(String equipment, String sido, String sigungu) {
        // Validation
        if(equipment == null || equipment.isEmpty()){return null;}
        if(sido == null || sido.isEmpty()){return null;}
        if(sigungu == null || sigungu.isEmpty()){return null;}

        EquipmentLocal newEquipLocal = new EquipmentLocal();
        newEquipLocal.setEquipment(equipment);
        newEquipLocal.setSido(sido);
        newEquipLocal.setSigungu(sigungu);

        return this.repository.save(newEquipLocal);
    }

    public Map<String, List> getEquiLocalList(String equipment){
        List<EquipmentLocal> equipmentLocalList = repository.findByEquipment(equipment);

        Map<String, List> localData = new HashMap();

        if(equipmentLocalList == null){return new HashMap();}
        for(EquipmentLocal eLocal: equipmentLocalList){
            List sigunguList = localData.get(eLocal.getSido());

            if(sigunguList == null){
                sigunguList = new ArrayList();
                sigunguList.add(eLocal.getSigungu());
                localData.put(eLocal.getSido(), sigunguList);
            }else{
                sigunguList.add(eLocal.getSigungu());
            }
        }

        return localData;
    }
}
