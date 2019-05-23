package com.jangbee.work;

import com.jangbee.common.JBBadRequestException;
import com.jangbee.common.JBUnexpectException;
import com.jangbee.coupon.Coupon;
import com.jangbee.coupon.CouponLackException;
import com.jangbee.coupon.CouponService;
import com.jangbee.firm.Firm;
import com.jangbee.firm.FirmDto;
import com.jangbee.firm.FirmService;
import com.jangbee.firm_evalu.FirmEvaluService;
import com.jangbee.openbank.OpenbankService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by test on 2019-04-15.
 */
@RestController
@RequestMapping("/api/v1/")
public class WorkController {
    @Autowired
    private WorkService service;

    @Autowired private FirmService firmService;

    @Autowired OpenbankService openBankService;

    @Autowired
    private WorkApplicantService workApplicantService;

    @Autowired
    FirmEvaluService firmEvaluService;
    @Autowired
    CouponService couponService;

    @Autowired
    WorkRepository workRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value( "${openbank.matching_fee.firm_work}" )
    private Integer firmWorkMatchingFee;

    @RequestMapping(value="work", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody @Valid WorkDto.Create create, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            throw new JBBadRequestException();
        }

        Work work = service.create(create);


        return new ResponseEntity<>(modelMapper.map(work, WorkDto.FirmResponse.class), HttpStatus.OK);
    }

    @RequestMapping(value="work", method = RequestMethod.PUT)
    public ResponseEntity create(@RequestBody @Valid WorkDto.Update update, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new JBBadRequestException();
        }

        boolean result = service.updateWork(update);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value="works/firm/open", method = RequestMethod.GET)
    public ResponseEntity getOpenFirmWorkList(@RequestParam String equipment, @RequestParam String accountId) {
        List<Work> works = service.getOpenFirmWorkList(equipment, accountId);
        List<Long> applicantWorkIdList = service.getApplicantWorkIdList(accountId);

        List<WorkDto.FirmResponse> content = works.parallelStream()
                .map(work -> {
                    WorkDto.FirmResponse res = modelMapper.map(work , WorkDto.FirmResponse.class);
                    if(applicantWorkIdList.contains(res.getId())){res.setApplied(true);}
                    if(work.isFirmRegister() && new Date().after(work.getGuaranteeTime())) {res.setGuarTimeExpire(true);}
                    return res;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(content, HttpStatus.OK);
    }

    @RequestMapping(value="works/firm/matched", method = RequestMethod.GET)
    public ResponseEntity getMatchedFirmWorkList(@RequestParam String equipment, @RequestParam String accountId) {
        List<Work> works = service.getMatchedFirmWorkList(equipment, accountId);

        List<WorkDto.FirmMatchedResponse> content = works.parallelStream()
                .map(work -> modelMapper.map(work , WorkDto.FirmMatchedResponse.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(content, HttpStatus.OK);
    }

    @RequestMapping(value="works/firm/apply", method = RequestMethod.PUT)
    public ResponseEntity applyWork(@RequestBody @Valid WorkDto.Apply apply, BindingResult result) {
        if(result.hasErrors()){
            throw new JBBadRequestException();
        }

        Work work = service.applyWork(apply);

        WorkDto.FirmResponse content = modelMapper.map(work , WorkDto.FirmResponse.class);

        return new ResponseEntity<>(content, HttpStatus.OK);
    }

    @RequestMapping(value="firm_works/firm/apply", method = RequestMethod.PUT)
    public ResponseEntity applyFirmWork(@RequestBody @Valid WorkDto.Apply apply, BindingResult result) {
        if(result.hasErrors()){
            throw new JBBadRequestException();
        }

        if (apply.isCoupon()){
            Coupon c = couponService.getCoupon(apply.getAccountId());

            if(c == null || c.getCpCount() < 2) {
                throw new CouponLackException();
            }
        }

        Work work = workRepository.findOne(apply.getWorkId());
        if (work == null) {throw new JBUnexpectException();}
        Date guaranteeTime = work.getGuaranteeTime();

        if (new Date().after(guaranteeTime)) {
            throw new FirmWorkExpireException();
        }

        WorkApplicant newWorkApplicant = service.applyFirmWork(apply);

        if (apply.isCoupon()) {
            couponService.useCoupon(apply.getAccountId(), 2);
        } else {
            boolean withdrawResult = openBankService.withdraw(apply.getAuthToken(), apply.getFintechUseNum(), "장비콜 일감매칭비", firmWorkMatchingFee);
            if (!withdrawResult) {
                service.cancelApplyFirmWork(newWorkApplicant);
                throw new FirmworkWithdrawException();
            }
        }

        service.acceptFirmWork(work, newWorkApplicant);

        WorkDto.FirmResponse content = modelMapper.map(work , WorkDto.FirmResponse.class);

        return new ResponseEntity<>(content, HttpStatus.OK);
    }

    @RequestMapping(value="works/firm/accept", method = RequestMethod.PUT)
    public ResponseEntity acceptWork(@RequestBody @Valid WorkDto.Accept accept, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            throw new JBBadRequestException();
        }

        if (accept.isCoupon()){
            Coupon c = couponService.getCoupon(accept.getAccountId());

            if(c == null || c.getCpCount() < 2) {
                throw new CouponLackException();
            }
        }

        boolean result = service.acceptWork(accept);

        if (accept.isCoupon()){
            couponService.useCoupon(accept.getAccountId(), 2);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value="works/firm/abandon", method = RequestMethod.PUT)
    public ResponseEntity abandonWork(@RequestBody @Valid WorkDto.Abandon abandon, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            throw new JBBadRequestException();
        }

        boolean result = service.abandonWork(abandon);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value="works/client/select", method = RequestMethod.PUT)
    public ResponseEntity selectWork(@RequestBody @Valid WorkDto.Select select, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            throw new JBBadRequestException();
        }

        boolean result = service.selectedFirm(select);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value="works/client/select/cancel", method = RequestMethod.PUT)
    public ResponseEntity cancelSelectWork(@RequestParam Long workId) {

        boolean result = service.cancelSelectedFirm(workId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value="works/client/open", method = RequestMethod.GET)
    public ResponseEntity getOpenClientWorkList(@RequestParam String accountId) {
        List<Work> works = service.getOpenClientWorkList(accountId);

        List<WorkDto.ClientResponse> content = works.parallelStream()
                .map(work -> {
                    WorkDto.ClientResponse res = modelMapper.map(work , WorkDto.ClientResponse.class);
                    Long applicantCount = service.getApplicantCount(work.getId());
                    res.setApplicantCount(applicantCount);
                    res.setOverAcceptTime(WorkDto.isOverAcceptTime(res.getWorkState(), res.getSelectNoticeTime()));
                    if(work.isFirmRegister() && new Date().after(work.getGuaranteeTime())) {res.setGuarTimeExpire(true);}
                    return res;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(content, HttpStatus.OK);
    }

    @RequestMapping(value="works/client/matched", method = RequestMethod.GET)
    public ResponseEntity getMatchedClientWorkList(@RequestParam String accountId) {
        List<Work> works = service.getMatchedClientWorkList(accountId);

        List<WorkDto.ClientResponse> content = works.parallelStream()
                .map(work -> {
                    WorkDto.ClientResponse res = modelMapper.map(work , WorkDto.ClientResponse.class);

                    if(firmEvaluService.getFirmEvaluByWorkId(work.getId()) != null){
                        res.setFirmEstimated(true);
                    }
                    return res;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(content, HttpStatus.OK);
    }


    @RequestMapping(value="works/applicants", method = RequestMethod.GET)
    public ResponseEntity getAppliFirmList(@RequestParam Long workId) {
        List<String> appliAccountIdList = workApplicantService.getAccountIdList(workId);

        List<Firm> firmList = firmService.getFirmList(appliAccountIdList);

        List<FirmDto.Response> content = firmList.parallelStream()
                .map(work -> modelMapper.map(work , FirmDto.Response.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(content, HttpStatus.OK);
    }
}
