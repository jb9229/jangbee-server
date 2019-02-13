package com.jangbee.local;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by test on 2019-02-13.
 */
public interface EquiLocalRepository extends JpaRepository<EquipmentLocal, Long> {
    EquipmentLocal findByEquipmentAndSidoAndSigungu(String equipment, String sido, String sigungu);

    List<EquipmentLocal> findByEquipment(String equipment);
}
