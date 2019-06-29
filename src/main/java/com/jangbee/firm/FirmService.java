package com.jangbee.firm;

import com.jangbee.coupon.CouponService;
import com.jangbee.local.EquiLocalService;
import com.jangbee.local.EquipmentLocal;
import com.jangbee.utils.GeoUtils;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.vividsolutions.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    CouponService couponService;


    @Autowired
    private FirmRepository repository;

    public Firm create(FirmDto.Create create) throws ParseException {
        // i don't know why Numberformatexception occure
        String accountId = create.getAccountId();
        create.setAccountId("");
        Firm newFirm = this.modelMapper.map(create, Firm.class);
        newFirm.setAccountId(accountId);

        newFirm.setLocation(GeoUtils.getPoint(create.getAddrLatitude(), create.getAddrLongitude()));

        // Save Equipment Local for search
        String[] equipmentArr = newFirm.getEquiListStr().split(EQUIPMENT_STR_SEPERATOR);
        String sido = newFirm.getSidoAddr();
        String sigungu = newFirm.getSigunguAddr();
        for(String equipment: equipmentArr){
            if(equipment.isEmpty()){continue;}
            EquipmentLocal eLocal = equiLocalService.get(equipment, sido, sigungu);

            if(eLocal == null){equiLocalService.create(equipment, sido, sigungu);}
        }

        // 베타버전 프로모션
        couponService.payFirmWorkCoupon(newFirm.getAccountId(), 100);

        return this.repository.save(newFirm);
    }

    public Firm getByAccountId(String accountId) {
        Firm firm =   repository.findByAccountId(accountId);


        return firm;
    }

    public Firm update(Firm firm, FirmDto.Update update) throws ParseException {
        firm.setFname(update.getFname());
        firm.setPhoneNumber(update.getPhoneNumber());
        firm.setEquiListStr(update.getEquiListStr());
        firm.setModelYear(update.getModelYear());
        firm.setAddress(update.getAddress());
        firm.setAddressDetail(update.getAddressDetail());
        firm.setSidoAddr(update.getSidoAddr());
        firm.setSigunguAddr(update.getSigunguAddr());
        firm.setWorkAlarmSido(update.getWorkAlarmSido());
        firm.setWorkAlarmSigungu(update.getWorkAlarmSigungu());
        firm.setIntroduction(update.getIntroduction());
        firm.setThumbnail(update.getThumbnail());
        firm.setPhoto1(update.getPhoto1());
        firm.setPhoto2(update.getPhoto2());
        firm.setPhoto3(update.getPhoto3());
        firm.setBlog(update.getBlog());
        firm.setHomepage(update.getHomepage());
        firm.setSns(update.getSns());
        firm.setLocation(GeoUtils.getPoint(update.getAddrLatitude(), update.getAddrLongitude()));

        return this.repository.save(firm);
    }

    public Page<Object> findNearFirm(String equipment, Double longitude, Double latitude, Pageable pageable) {

        List<FirmDto.ListResponse> list = repository.getNearFirm(equipment, longitude, latitude, (pageable.getPageNumber())*pageable.getPageSize(), pageable.getPageSize());

        long totalCnt = repository.countByEquiListStr(equipment);

        return new PageImpl(list, pageable, totalCnt);
    }

    public Page<Firm> findLocalFirm(String equipment, String sidoAddr, String sigunguAddr, Pageable pageable) {

        Page<Firm> list;
        if (sigunguAddr != null) {
            list = repository.findByEquiListStrAndSidoAddrAndSigunguAddr(equipment, sidoAddr, sigunguAddr, pageable);
        } else {
            list = repository.findByEquiListStrAndSidoAddr(equipment, sidoAddr, pageable);
        }

        return list;
    }

    public List<Firm> findAvaWorkFirm(String equipment, String sido, String sigungu) {
        String likeSidoStr = "%"+sido+"%";
        String likeSigunguStr = "%"+sido+" "+sigungu+"%";

        return repository.findAvaWorkFirm(equipment, likeSidoStr, likeSigunguStr);
    }

    public List<Firm> getFirmList(List<String> appliAccountIdList) {
        return repository.findByAccountIdIn(appliAccountIdList);
    }

    public Long deleteFirm(String accountId) {
        return repository.deleteByAccountId(accountId);
    }
}
