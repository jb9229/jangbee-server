package com.jangbee.firm;

import com.google.common.collect.Lists;
import com.jangbee.common.ErrorResponse;
import com.jangbee.estimate.EstimateDto;
import com.jangbee.local.EquiLocalDto;
import com.jangbee.local.EquiLocalService;
import com.vividsolutions.jts.io.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by test on 2019-01-20.
 */
@RestController
@RequestMapping("/api/v1/")
public class FirmController {

    @Autowired
    private FirmService service;
    @Autowired private EquiLocalService equiLocalService;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(value="firm", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody @Valid FirmDto.Create create, BindingResult result) throws ParseException {   //RequestBody

        if(result.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Firm firm = service.create(create);


        return new ResponseEntity<>(modelMapper.map(firm, EstimateDto.Response.class), HttpStatus.OK);
    }

    @RequestMapping(value="firm/{accountId}", method = RequestMethod.GET)
    public ResponseEntity get(@PathVariable String accountId) {
        Firm firm =   service.getByAccountId(accountId);

        if(firm  ==  null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        FirmDto.Response response        =   modelMapper.map(firm, FirmDto.Response.class);
        response.setAddrLongitude(firm.getLocation().getX());
        response.setAddrLatitude(firm.getLocation().getY());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value="/firm/near", method = RequestMethod.GET)
    public ResponseEntity getNearFirm(@RequestParam  String equipment, @RequestParam Double longitude, @RequestParam Double latitude, Pageable pageable){

        Page<Object> pageResult =      service.findNearFirm(equipment, longitude, latitude, pageable);

        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }

    @RequestMapping(value="/firm/local", method = RequestMethod.GET)
    public ResponseEntity getLoclFirm(@RequestParam  String equipment, @RequestParam String sido, @RequestParam String gungu, Pageable pageable){

        Page<Firm> page =      service.findLocalFirm(equipment, sido, gungu, pageable);

        List<FirmDto.ListResponse> content = page.getContent().parallelStream()
                .map(newFirm -> modelMapper.map(newFirm, FirmDto.ListResponse.class))
                .collect(Collectors.toList());


        PageImpl<FirmDto.ListResponse> pageResult    =   new PageImpl<>(content, pageable, page.getTotalElements());

        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }

    @RequestMapping(value="firm/local/{equipment}", method = RequestMethod.GET)
    public ResponseEntity getEquiLocalData(@PathVariable String equipment) {
        Map<String, List> localData =   equiLocalService.getEquiLocalList(equipment);

        EquiLocalDto.Response response = new EquiLocalDto.Response();

        response.setSidoList(Lists.newArrayList(localData.keySet().iterator()));
        response.setGunguData(localData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @RequestMapping(value="firm", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestBody @Valid FirmDto.Update updateDto, BindingResult result) throws ParseException {

        if(result.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Firm firm     =   service.getByAccountId(updateDto.getAccountId());

        if(firm  ==  null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        Firm updateAccount     =   service.update(firm, updateDto);

        return new ResponseEntity<>(modelMapper.map(updateAccount, FirmDto.Response.class),
                HttpStatus.OK);
    }

    @ExceptionHandler(FirmNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerSpoonNotFoundException(FirmNotFoundException e){
        ErrorResponse errorResponse  =   new ErrorResponse();
        errorResponse.setMessage("["+ e.getAccountId()+"]에 해당하는 업체가 없습니다.");
        errorResponse.setCode("firm.not.found.exception");

        return errorResponse;
    }
}
