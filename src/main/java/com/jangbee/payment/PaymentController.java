package com.jangbee.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by kosac on 08/03/2020.
 */
@RestController
@RequestMapping("/api/v1/")
public class PaymentController {
    @Autowired
    private PaymentService service;

    @CrossOrigin("*")
    @RequestMapping(value="payment/approval", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody @Valid PaymentDto.Approval approval, BindingResult result) {   //RequestBody

        if(result.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        PaymentDto.ApprovalResponse response = service.requestApproval(approval);

        if (response != null)
        {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }

    }
}
