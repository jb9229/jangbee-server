package com.jangbee.firm_evalu;

import com.jangbee.common.JBBadRequestException;
import com.jangbee.firm.FirmDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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

    @RequestMapping(value="firm/evaluation", method = RequestMethod.GET)
    public ResponseEntity get(@RequestParam String accountId) {
        List<FirmEvalu> list =   service.getByAccountId(accountId);

        if(list  ==  null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<FirmEvaluDto.Response> content = list.parallelStream()
                .map(newEvalu -> modelMapper.map(newEvalu, FirmEvaluDto.Response.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(content, HttpStatus.OK);
    }
}
