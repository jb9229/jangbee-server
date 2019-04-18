package com.jangbee.firm_evalu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by test on 2019-04-18.
 */
@Service
public class FirmEvaluService {

    @Autowired FirmEvaluRepository repository;
    public FirmEvalu getFirmEvaluByWorkId(Long workId) {
        return repository.findByWorkId(workId);
    }
}
