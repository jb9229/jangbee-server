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
import org.springframework.web.bind.annotation.RequestParam;

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
    public ResponseEntity getAll(@RequestParam String accountId) {
        List<ClientEvalu> list =   service.getClientEvaluAll();

        List<Long> evaluLikeList = service.listEvaluLike(accountId);

        List<ClientEvaluDto.Response> responseList = list.parallelStream()
                .map(newEvalu -> {
                    ClientEvaluDto.Response response = modelMapper.map(newEvalu, ClientEvaluDto.Response.class);
                    if(evaluLikeList.contains(newEvalu.getId())){
                        response.setLikedEvau(true);
                    }

                    return response;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @RequestMapping(value="evaluation", method = RequestMethod.GET)
    public ResponseEntity get(@RequestParam(required = false) String cliName, @RequestParam(required = false) String firmName, @RequestParam(required = false) String telNumber, @RequestParam(required = false) String firmNumber) {
        List<ClientEvalu> list =   service.get(cliName, firmName, telNumber, firmNumber);


        List<ClientEvaluDto.Response> responseList = list.parallelStream()
                .map(newEvalu -> modelMapper.map(newEvalu, ClientEvaluDto.Response.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @RequestMapping(value="evaluation/exist/telnumber", method = RequestMethod.GET)
    public ResponseEntity existTelNumber(@RequestParam String telNumber) {
        boolean result =   service.existTelNumer(telNumber);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value="evaluation", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestBody @Valid ClientEvaluDto.Update update, BindingResult result) throws ParseException {   //RequestBody

        if(result.hasErrors()){
            throw new JBBadRequestException();
        }

        ClientEvalu updateClientEvalu = service.update(update);

        return new ResponseEntity<>(modelMapper.map(updateClientEvalu, ClientEvaluDto.Response.class), HttpStatus.OK);
    }

    @RequestMapping(value="evaluation", method = RequestMethod.DELETE)
    public ResponseEntity delete(@RequestParam Long id){   //RequestBody
        service.delete(id);

        return new ResponseEntity<>(modelMapper.map(true, ClientEvaluDto.Response.class), HttpStatus.OK);
    }

    @RequestMapping(value="evaluation/like", method = RequestMethod.POST)
    public ResponseEntity createlike(@RequestBody @Valid ClientEvaluLikeDto.Create create, BindingResult result) throws ParseException {
        if(result.hasErrors()){
            throw new JBBadRequestException();
        }

        ClientEvaluLike evaluLike = service.updateLike(create);


        return new ResponseEntity<>(modelMapper.map(evaluLike, ClientEvaluLikeDto.Response.class), HttpStatus.OK);
    }

    @RequestMapping(value="evaluation/like/exist", method = RequestMethod.GET)
    public ResponseEntity existLike(@RequestParam String accountId, @RequestParam Long evaluId){

        boolean result = service.existEvaluLike(accountId, evaluId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value="evaluation/like", method = RequestMethod.GET)
    public ResponseEntity listLike(@RequestParam long evaluId) throws ParseException {

        List<ClientEvaluLike> evaluLikeList = service.getEvaluLikeList(evaluId);

        List<ClientEvaluLikeDto.Response> evaluLikeResList = evaluLikeList
                .parallelStream()
                .map((item) -> modelMapper.map(item, ClientEvaluLikeDto.Response.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(evaluLikeResList, HttpStatus.OK);
    }

    @RequestMapping(value="evaluation/like", method = RequestMethod.DELETE)
    public ResponseEntity cancelLike(@RequestParam long evaluId, @RequestParam String accountId, @RequestParam boolean like){

        boolean result = service.cancelEvaluLike(evaluId, accountId, like);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
