package com.jangbee.ad;

import com.jangbee.common.JBBadRequestException;
import org.json.JSONException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.Min;
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

    @RequestMapping(value="ad", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody @Valid AdDto.Create create, BindingResult result) throws JSONException {
        if(result.hasErrors()){
            throw new JBBadRequestException();
        }

        //TODO 첫달 이체 진행
        if(!service.obTransferWithdraw(create.getFintechUseNum(), create.getPrice())){throw new AdPriceWithdrawException();}

        Ad newAd  =   service.createAd(create);

        return new ResponseEntity<>(modelMapper.map(newAd, AdDto.Response.class), HttpStatus.CREATED);
    }

    @RequestMapping(value="ad", method = RequestMethod.GET)
    public ResponseEntity get(@RequestParam AdLocation adLocation, @RequestParam(required = false) String equiTarget, @RequestParam(required = false) String sidoTarget, @RequestParam(required = false) String gugunTarget) {
        List<Ad> adList =   service.getByAdLocation(adLocation, equiTarget, sidoTarget, gugunTarget);

        if(adList  ==  null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<AdDto.Response> content = adList.parallelStream()
                .map(newAd -> modelMapper.map(newAd , AdDto.Response.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(content, HttpStatus.OK);
    }

    @RequestMapping(value="ads", method = RequestMethod.GET)
    public ResponseEntity getList(@RequestParam String accountId) {
        List<Ad> adList =   service.getByAccountId(accountId);

        if(adList  ==  null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<AdDto.Response> content = adList.parallelStream()
                .map(newAd -> modelMapper.map(newAd , AdDto.Response.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(content, HttpStatus.OK);
    }

    @RequestMapping(value="ad/booked", method = RequestMethod.GET)
    public ResponseEntity getBookedAdType() {
        List<Integer> adTypeList =   service.getBookedAdType();

        if(adTypeList  ==  null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(adTypeList, HttpStatus.OK);
    }

    /**
     * 장비 타켓 중복확인 함수
     * @param equipment
     * @return
     */
    @RequestMapping(value="ad/target/equipment", method = RequestMethod.GET)
    public ResponseEntity existEquiTargetAd(@RequestParam String equipment) {
        Ad equipTargetAd =   service.getByEquiTarget(equipment);

        if(equipTargetAd == null){return new ResponseEntity<>(HttpStatus.NO_CONTENT);}

        AdDto.Response responseAd = modelMapper.map(equipTargetAd , AdDto.Response.class);

        return new ResponseEntity<>(responseAd, HttpStatus.OK);
    }

    /**
     * 지역 타켓광고 중복확인 함수
     * @param equipment
     * @return
     */
    @RequestMapping(value="ad/target/local", method = RequestMethod.GET)
    public ResponseEntity existLocalTargetAd(@RequestParam String equipment, @RequestParam String sido, @RequestParam String gungu) {
        Ad localTargetAd =   service.getByLocalTarget(equipment, sido, gungu);

        if(localTargetAd == null){return new ResponseEntity<>(HttpStatus.NO_CONTENT);}

        AdDto.Response responseAd = modelMapper.map(localTargetAd , AdDto.Response.class);
        return new ResponseEntity<>(responseAd, HttpStatus.OK);
    }


    @RequestMapping(value="ad", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestBody @Valid AdDto.Update update, BindingResult result) throws JSONException {
        if(result.hasErrors()){
            throw new JBBadRequestException();
        }

        Ad newAd  =   service.updateAd(update);

        return new ResponseEntity<>(modelMapper.map(newAd, AdDto.Response.class), HttpStatus.OK);
    }

    @RequestMapping(value="ad/fintechusenum", method = RequestMethod.PUT)
    public ResponseEntity updateFintechUseNum(@RequestParam String accountId, @RequestParam String newFintechUseNum) throws JSONException {
        boolean result  =   service.updateAdFintechUseNum(newFintechUseNum, accountId);

        if(result)
            return new ResponseEntity<>(result, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    @RequestMapping(value="ad", method = RequestMethod.DELETE)
    public ResponseEntity terminate(@RequestParam Long id) throws JSONException {


        Ad newAd  =   service.terminateAd(id);

        return new ResponseEntity<>(modelMapper.map(newAd, AdDto.Response.class), HttpStatus.OK);
    }
}
