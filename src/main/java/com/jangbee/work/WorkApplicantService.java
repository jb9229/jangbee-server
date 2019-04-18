package com.jangbee.work;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by test on 2019-04-18.
 */
@Service
public class WorkApplicantService {
    @Autowired private WorkApplicantRepository repository;

    public List<String> getAccountIdList(Long workId) {
        return repository.findByWorkId(workId);
    }
}
