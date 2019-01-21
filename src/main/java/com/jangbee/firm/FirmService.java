package com.jangbee.firm;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by test on 2019-01-20.
 */
@Service
@Transactional
@Slf4j
public class FirmService {
    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private FirmRepository repository;

    public Firm create(FirmDto.Create create) {
        Firm firm = this.modelMapper.map(create, Firm.class);


        return this.repository.save(firm);
    }
}
