package com.jangbee.ad;

import com.jangbee.firm.FirmDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by test on 2019-02-14.
 */
@Controller
@RequestMapping("/api/v1/")
public class AdController {

    @Autowired AdService service;
    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(value="ad", method = RequestMethod.GET)
    public ResponseEntity get(@RequestParam AdType adType, @RequestParam(required = false) String equiTarget, @RequestParam(required = false) String sidoTarget, @RequestParam(required = false) String gugunTarget) {
        List<Ad> adList =   service.getByAdType(adType, equiTarget, sidoTarget, gugunTarget);

        if(adList  ==  null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<AdDto.Response> content = adList.parallelStream()
                .map(newAd -> modelMapper.map(newAd , AdDto.Response.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(content, HttpStatus.OK);
    }
}