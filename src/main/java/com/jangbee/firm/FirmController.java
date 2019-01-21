package com.jangbee.firm;

import com.jangbee.estimate.EstimateDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by test on 2019-01-20.
 */
@RestController
@RequestMapping("/api/v1/")
public class FirmController {

    @Autowired
    private FirmService service;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(value="firm", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody @Valid FirmDto.Create create, BindingResult result){   //RequestBody

        if(result.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Firm firm = service.create(create);


        return new ResponseEntity<>(modelMapper.map(firm, EstimateDto.Response.class), HttpStatus.OK);
    }
}
