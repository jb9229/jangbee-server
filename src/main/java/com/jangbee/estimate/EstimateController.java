package com.jangbee.estimate;

import com.jangbee.common.ErrorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by test on 2016-01-31.
 */
@RestController
@RequestMapping("/api/v1/")
public class EstimateController {

    @Autowired
    private EstimateService service;

    @Autowired
    private EstimateRepository repository;

    @Autowired
    private ModelMapper modelMapper;



    @RequestMapping(value="/estimate", method = RequestMethod.POST)
    public ResponseEntity createEstimate(@RequestBody @Valid EstimateDto.Create create, BindingResult result){

        if(result.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Estimate estimate = service.createEstimate(create);


        return new ResponseEntity<>(modelMapper.map(estimate, EstimateDto.Response.class), HttpStatus.OK);
    }

    @RequestMapping(value="/estimates", method = GET)
    public ResponseEntity getEstimate(@PageableDefault(size = 2, sort="id", direction = Sort.Direction.ASC) Pageable pageable){

        Page<Estimate> page              =      repository.findAll(pageable);


        List<EstimateDto.Response> content = page.getContent().parallelStream()
                .map(newEstimate -> modelMapper.map(newEstimate, EstimateDto.Response.class))
                .collect(Collectors.toList());


        PageImpl<EstimateDto.Response> result    =   new PageImpl<>(content, pageable, page.getTotalElements());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value="/estimate/{id}/", method = GET)
    public ResponseEntity get(@PathVariable Long id){


        Estimate estimate   =   service.getEstimate(id);

        EstimateDto.Response response        =   modelMapper.map(estimate, EstimateDto.Response.class);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @RequestMapping(value="/spoon/{id}/", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Long id){
        service.deleteEstimate(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }




    @ExceptionHandler(EstimateNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerSpoonNotFoundException(EstimateNotFoundException e){
        ErrorResponse errorResponse  =   new ErrorResponse();
        errorResponse.setMessage("["+ e.getId()+"]에 해당하는 기부가 없습니다.");
        errorResponse.setCode("spoon.not.found.exception");

        return errorResponse;
    }
}
