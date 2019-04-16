package com.jangbee.work;

import com.jangbee.utils.GeoUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by test on 2019-04-15.
 */
@Service
public class WorkService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired private WorkRepository repository;

    public Work create(WorkDto.Create create) {
        Work newWork = this.modelMapper.map(create, Work.class);
        newWork.setAddressPoint(GeoUtils.getPoint(create.getAddrLatitude(), create.getAddrLongitude()));

        return repository.save(newWork);
    }

    public List<Work> getFirmWorkingList(String equipment, String accountId) {
        return repository.getFirmWorkingList(equipment, accountId);
    }
}
