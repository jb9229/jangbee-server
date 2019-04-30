package com.jangbee.firm_evalu;

import com.jangbee.firm.Firm;
import com.jangbee.firm.FirmRepository;
import com.jangbee.work.Work;
import com.jangbee.work.WorkRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.OptionalDouble;

/**
 * Created by test on 2019-04-18.
 */
@Service
public class FirmEvaluService {

    @Autowired FirmEvaluRepository repository;

    @Autowired
    WorkRepository workRepository;

    @Autowired
    FirmRepository firmRepository;

    @Autowired
    private ModelMapper modelMapper;
    public FirmEvalu getFirmEvaluByWorkId(Long workId) {
        return repository.findByWorkId(workId);
    }

    public FirmEvalu create(FirmEvaluDto.Create create) {
        Work work = workRepository.findOne(create.getWorkId());

        if (work != null && work.getMatchedAccId() != null) {
            String firmAccountId = work.getMatchedAccId();

            FirmEvalu newEvalu = this.modelMapper.map(create, FirmEvalu.class);

            newEvalu.setAccountId(work.getAccountId());
            newEvalu.setFirmAccountId(firmAccountId);
            newEvalu.setPhoneNumber(work.getPhoneNumber());
            newEvalu.setRegiDate(new Date());

            FirmEvalu savedEvalu =  repository.save(newEvalu);

            // Save Firm Evaluation Rate
            Firm firm = firmRepository.findByAccountId(firmAccountId);
            List<Byte> rateList = repository.getRateByFirmAccountId(firmAccountId);

            if(firm != null && rateList != null) {
                rateList.add(create.getRating());

                OptionalDouble average = rateList
                        .stream()
                        .mapToDouble(a -> a)
                        .average();

                firm.setRating(average.isPresent() ? (byte)average.getAsDouble() : 0);
                firm.setRatingCnt(firm.getRating()+1);

                firmRepository.saveAndFlush(firm);
            }


            return savedEvalu;
        }

        return null;
    }

    public List<FirmEvalu> getByAccountId(String accountId) {
        return repository.findByFirmAccountId(accountId);
    }
}
