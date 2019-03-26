package com.jangbee.client_evalu;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by test on 2019-03-26.
 */
@Service
public class ClientEvaluService {

    @Autowired private ClientEvaluRepository repository;
    @Autowired private ModelMapper modelMapper;
    private List<ClientEvalu> clientEvaluAll;

    public ClientEvalu create(ClientEvaluDto.Create create) {
        String accountId = create.getAccountId();
        create.setAccountId("");
        ClientEvalu newEvaluation = this.modelMapper.map(create, ClientEvalu.class);
        newEvaluation.setAccountId(accountId);
        newEvaluation.setLikeCount(0);
        newEvaluation.setUnlikeCount(0);

        return repository.save(newEvaluation);
    }

    public List<ClientEvalu> getClientEvaluAll() {
        return repository.findAll();
    }
}
