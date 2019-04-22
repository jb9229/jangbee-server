package com.jangbee.firm_evalu;

import com.jangbee.common.JBBadRequestException;
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
 * Created by test on 2019-04-20.
 */
@RestController
@RequestMapping("/api/v1/")
public class FirmEvaluController {
    @Autowired
    private FirmEvaluService service;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(value="firm/evaluation", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody @Valid FirmEvaluDto.Create create, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new JBBadRequestException();
        }

        FirmEvalu evalu = service.create(create);

        boolean rsult = true;

        if (evalu == null){rsult = false;}

        return new ResponseEntity<>(rsult, HttpStatus.OK);

    }
}
