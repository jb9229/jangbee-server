package com.jangbee.firm_evalu;

import com.jangbee.work.Work;
import com.jangbee.work.WorkRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by test on 2019-04-18.
 */
@Service
public class FirmEvaluService {

    @Autowired FirmEvaluRepository repository;
    @Autowired
    WorkRepository workRepository;

    @Autowired
    private ModelMapper modelMapper;
    public FirmEvalu getFirmEvaluByWorkId(Long workId) {
        return repository.findByWorkId(workId);
    }

    public FirmEvalu create(FirmEvaluDto.Create create) {
        Work work = workRepository.findOne(create.getWorkId());

        if (work != null && work.getMatchedAccId() != null) {
            FirmEvalu newEvalu = this.modelMapper.map(create, FirmEvalu.class);

            newEvalu.setAccountId(work.getAccountId());
            newEvalu.setFirmAccountId(work.getMatchedAccId());

            return repository.save(newEvalu);
        }

        return null;
    }
}
