package com.jangbee.estimate;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by test on 2016-01-31.
 */
@Service
@Transactional
@Slf4j
public class EstimateService {

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private EstimateRepository repository;



    public void deleteEstimate(Long id) {
        repository.delete(getEstimate(id));
    }


    public Estimate getEstimate(Long id){


        Estimate estimate =   repository.findOne(id);

        if(estimate == null)
        {
            throw new EstimateNotFoundException(id);
        }


        return estimate;
    }


    public Page<Estimate> getSpoons(Specification<Estimate> spec, Pageable pageable){


        Page<Estimate> page                =   repository.findAll(spec, pageable);


        return page;
    }


    public Estimate createEstimate(EstimateDto.Create estimateDto) {

        Estimate estimate = this.modelMapper.map(estimateDto, Estimate.class);


        return this.repository.save(estimate);

    }
}
