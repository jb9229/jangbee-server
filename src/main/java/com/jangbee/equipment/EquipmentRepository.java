package com.jangbee.equipment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by test on 2019-01-15.
 */
public interface EquipmentRepository extends JpaRepository<Equipment, Long>{

    @Query(value="SELECT e_name FROM equipment", nativeQuery = true)
    List<String> getEquipmentNameList();
}
