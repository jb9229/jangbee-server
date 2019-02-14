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

    public List<Ad> getByAdType(AdType adType) {
        List<Ad> adList = repository.getByAdType(adType);

        return adList;
    }
}
