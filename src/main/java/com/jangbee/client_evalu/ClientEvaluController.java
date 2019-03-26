package com.jangbee.client_evalu;

import com.jangbee.common.JBBadRequestException;
import com.vividsolutions.jts.io.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by test on 2019-03-26.
 */
@Controller
@RequestMapping("/api/v1/client/")
public class ClientEvaluController {
    @Autowired ClientEvaluService service;
    @Autowired private ModelMapper modelMapper;


    @RequestMapping(value="evaluation", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody @Valid ClientEvaluDto.Create create, BindingResult result) throws ParseException {   //RequestBody

        if(result.hasErrors()){
            throw new JBBadRequestException();
        }

        ClientEvalu clientEvalu = service.create(create);


        return new ResponseEntity<>(modelMapper.map(clientEvalu, ClientEvaluDto.Response.class), HttpStatus.OK);
    }

    @RequestMapping(value="evaluations", method = RequestMethod.GET)
    public ResponseEntity getAll() {
        List<ClientEvalu> list =   service.getClientEvaluAll();

        List<ClientEvaluDto.Response> responseList = list.parallelStream()
                .map(newEvalu -> modelMapper.map(newEvalu, ClientEvaluDto.Response.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }
}
