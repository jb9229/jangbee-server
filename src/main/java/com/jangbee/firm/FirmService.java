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

    public Firm getByAccountId(Long accountId) {
        Firm firm =   repository.findByAccountId(accountId);

        if(firm == null)
        {
            throw new FirmNotFoundException(accountId);
        }


        return firm;
    }

    public Firm update(Firm firm, FirmDto.Update update) {
        firm.setFname(update.getFname());
        firm.setEquiListStr(update.getEquiListStr());
        firm.setAddress(update.getAddress());
        firm.setAddressDetail(update.getAddressDetail());
        firm.setSidoAddr(update.getSidoAddr());
        firm.setSigunguAddr(update.getSigunguAddr());
        firm.setAddrLatitude(update.getAddrLatitude());
        firm.setAddrLongitude(update.getAddrLongitude());
        firm.setIntroduction(update.getIntroduction());
        firm.setThumbnail(update.getThumbnail());
        firm.setPhoto1(update.getPhoto1());
        firm.setPhoto2(update.getPhoto2());
        firm.setPhoto3(update.getPhoto3());
        firm.setBlog(update.getBlog());
        firm.setHomepage(update.getHomepage());
        firm.setSns(update.getSns());

        return this.repository.save(firm);
    }
}
