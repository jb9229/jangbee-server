package com.jangbee.equipment;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by test on 2019-01-15.
 */
@RestController
@RequestMapping("/api/v1/")
public class EquipmentController {
    @Autowired
    private  EquipmentRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(value="equipment/list", method = GET)
    public ResponseEntity getAll(){

        List<String> equipmentNameList        =   repository.getEquipmentNameList();

        return new ResponseEntity<>(equipmentNameList, HttpStatus.OK);
    }
}
