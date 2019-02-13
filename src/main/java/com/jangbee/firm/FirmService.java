package com.jangbee.firm;

import com.jangbee.local.EquiLocalService;
import com.jangbee.local.EquipmentLocal;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by test on 2019-01-20.
 */
@Service
@Transactional
@Slf4j
public class FirmService {
    static String EQUIPMENT_STR_SEPERATOR = ",";
    @Autowired
    private ModelMapper modelMapper;
    @Autowired private EquiLocalService equiLocalService;


    @Autowired
    private FirmRepository repository;

    public Firm create(FirmDto.Create create) {
        // i don't know why Numberformatexception occure
        String accountId = create.getAccountId();
        create.setAccountId("");
        Firm firm = this.modelMapper.map(create, Firm.class);
        firm.setAccountId(accountId);

        // Save Equipment Local for search
        String[] equipmentArr = firm.getEquiListStr().split(EQUIPMENT_STR_SEPERATOR);
        String sido = firm.getSidoAddr();
        String sigungu = firm.getSigunguAddr();
        for(String equipment: equipmentArr){
            if(equipment.isEmpty()){continue;}
            EquipmentLocal eLocal = equiLocalService.get(equipment, sido, sigungu);

            if(eLocal == null){equiLocalService.create(equipment, sido, sigungu);}
        }


        return this.repository.save(firm);
    }

    public Firm getByAccountId(String accountId) {
        Firm firm =   repository.findByAccountId(accountId);


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
