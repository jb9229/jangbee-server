package com.jangbee.firm;

import com.jangbee.common.ErrorResponse;
import com.jangbee.estimate.EstimateDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value="firm/{accountId}", method = RequestMethod.GET)
    public ResponseEntity get(@PathVariable Long accountId) {
        Firm firm =   service.getByAccountId(accountId);

        FirmDto.Response response        =   modelMapper.map(firm, FirmDto.Response.class);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @RequestMapping(value="firm", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestBody @Valid FirmDto.Update updateDto, BindingResult result) {

        if(result.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Firm account     =   service.getByAccountId(updateDto.getAccountId());

        if(account  ==  null)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Firm updateAccount     =   service.update(account, updateDto);

        return new ResponseEntity<>(modelMapper.map(updateAccount, FirmDto.Response.class),
                HttpStatus.OK);
    }

    @ExceptionHandler(FirmNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerSpoonNotFoundException(FirmNotFoundException e){
        ErrorResponse errorResponse  =   new ErrorResponse();
        errorResponse.setMessage("["+ e.getId()+"]에 해당하는 업체가 없습니다.");
        errorResponse.setCode("firm.not.found.exception");

        return errorResponse;
    }
}
