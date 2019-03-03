package com.jangbee.ad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by test on 2019-02-14.
 */
@Service
public class AdService {
    @Autowired AdRepository repository;

    public List<Ad> getByAdType(AdType adType, String equiTarget, String sidoTarget, String gugunTarget) {
        List<Ad> adList = repository.getByAdTypeAndEquiTargetAndSidoTargetAndGugunTarget(adType, equiTarget, sidoTarget, gugunTarget);

        return adList;
    }
}
