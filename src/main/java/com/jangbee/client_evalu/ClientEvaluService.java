package com.jangbee.client_evalu;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    public List<ClientEvalu> get(String cliName, String firmName, String telNumber, String firmNumber) {
        if(cliName == null && firmName == null && telNumber == null && firmNumber == null){return new ArrayList();}

        if(cliName != null) {
            String param = "%"+cliName+"%";
            return repository.findByCliNameLike(param);
        }

        if(firmName != null) {
            String param = "%"+firmName+"%";
            return repository.findByFirmNameLike(param);
        }

        if(telNumber != null) {
            String param = "%"+telNumber+"%";
            return repository.findByTelNumberLikeOrTelNumber2LikeOrTelNumber3Like(param, param, param);
        }

        if(firmNumber != null) {
            String param = "%"+firmNumber+"%";
            return repository.findByFirmNumberLike(param);
        }
        return null;
    }

    public Page<ClientEvalu> getMyClientEvalu(String accountId, Pageable pageable) {
        return repository.getMy(accountId, pageable);
    }

    public Page<ClientEvalu> getNewestClientEvalu(Pageable pageable) {
        Calendar beforMonthsCal = Calendar.getInstance();
        beforMonthsCal.add(Calendar.MONTH, -2);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return repository.getNewest(beforMonthsCal.getTime(), new Date(), pageable);
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
        updateCE.setFirmName(update.getFirmName());
        updateCE.setTelNumber2(update.getTelNumber2());
        updateCE.setTelNumber3(update.getTelNumber3());
        updateCE.setFirmNumber(update.getFirmNumber());
        updateCE.setEquipment(update.getEquipment());
        updateCE.setLocal(update.getLocal());
        updateCE.setAmount(update.getAmount());
        updateCE.setRegiTelNumber(update.getRegiTelNumber());
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

    public boolean existEvaluLike(String accountId, Long evaluId) {
        return likeRepository.existsByAccountIdAndEvaluId(accountId, evaluId);
    }

    public void deleteByAccountId(String accountId) {
        likeRepository.deleteByAccountId(accountId);
        repository.deleteByAccountId(accountId);
    }
}
