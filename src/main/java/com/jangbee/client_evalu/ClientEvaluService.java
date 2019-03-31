package com.jangbee.client_evalu;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by test on 2019-03-26.
 */
@Service
public class ClientEvaluService {

    @Autowired private ClientEvaluRepository repository;
    @Autowired private ClientEvaluLikeRepository likeRepository;
    @Autowired private ModelMapper modelMapper;
    private List<ClientEvalu> clientEvaluAll;

    public ClientEvalu create(ClientEvaluDto.Create create) {
        String accountId = create.getAccountId();
        create.setAccountId("");
        ClientEvalu newEvaluation = this.modelMapper.map(create, ClientEvalu.class);
        newEvaluation.setAccountId(accountId);
        newEvaluation.setLikeCount(0);
        newEvaluation.setUnlikeCount(0);
        newEvaluation.setUpdateDate(new Date());

        return repository.save(newEvaluation);
    }

    public List<ClientEvalu> getClientEvaluAll() {
        return repository.findAll();
    }

    public boolean existTelNumer(String telNumber) {
        return repository.existsByTelNumber(telNumber);
    }

    public ClientEvalu update(ClientEvaluDto.Update update) {
        ClientEvalu updateCE = repository.findOne(update.getId());

        if (update == null){
            throw new ClientEvaluNotFoundException();
        }

        updateCE.setCliName(update.getCliName());
        updateCE.setReason(update.getReason());

        return repository.saveAndFlush(updateCE);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public ClientEvaluLike updateLike(ClientEvaluLikeDto.Create create) {
        ClientEvalu updateEvalu = repository.findOne(create.getEvaluId());
        if (create.isEvaluLike()){
            updateEvalu.setLikeCount(updateEvalu.getLikeCount()+1);
        }else{
            updateEvalu.setUnlikeCount(updateEvalu.getUnlikeCount()+1);
        }

        if(repository.saveAndFlush(updateEvalu) != null){
            ClientEvaluLike evaluLike = new ClientEvaluLike();
            evaluLike.setEvaluLike(create.isEvaluLike());
            evaluLike.setAccountId(create.getAccountId());
            evaluLike.setEvaluId(create.getEvaluId());
            evaluLike.setReason(create.getReason());
            evaluLike.setUpdateDate(new Date());


            return likeRepository.save(evaluLike);
        }

        return null;
    }

    public List<ClientEvaluLike> getEvaluLikeList(long evaluId) {
        return likeRepository.findByEvaluId(evaluId);
    }

    public List<Long> listEvaluLike(String accountId) {
        return likeRepository.findByAccountId(accountId);
    }

    public boolean cancelEvaluLike(long evaluId, String accountId, boolean like) {
        ClientEvalu updateEvalu = repository.findOne(evaluId);

        if (like) {
            updateEvalu.setLikeCount(updateEvalu.getLikeCount()-1);
        } else {
            updateEvalu.setUnlikeCount(updateEvalu.getUnlikeCount()-1);
        }

        if(repository.saveAndFlush(updateEvalu) != null){
            return (likeRepository.deleteByAccountId(accountId) > 0);
        }

        return false;
    }

    public boolean existEvaluLike(String accountId) {
        return likeRepository.existsByAccountId(accountId);
    }
}
