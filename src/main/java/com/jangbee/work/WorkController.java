package com.jangbee.work;

import com.google.firebase.database.*;
import com.jangbee.accounts.AccountDto;
import com.jangbee.common.JBBadRequestException;
import com.jangbee.expo.ExpoNotiData;
import com.jangbee.expo.ExpoNotificationService;
import com.jangbee.firm.Firm;
import com.jangbee.firm.FirmDto;
import com.jangbee.firm.FirmService;
import com.jangbee.firm_evalu.FirmEvaluService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    @Autowired
    private WorkApplicantService workApplicantService;

    @Autowired
    FirmEvaluService firmEvaluService;

    @Autowired private FirmService firmService;

    @Autowired
    ExpoNotificationService expoNotificationService;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(value="work", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody @Valid WorkDto.Create create, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            throw new JBBadRequestException();
        }

        Work work = service.create(create);

        if(work != null){
            // notice to those euqipment
            List<Firm> equiFirmList =  firmService.findEuipFirm(work.getEquipment());
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/");

//            CountDownLatch done = new CountDownLatch(1);

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {
                     List tokenList = new ArrayList();

                     for(Firm firm: equiFirmList){
                         DataSnapshot dSnapshot  = dataSnapshot.child(firm.getAccountId());
                         AccountDto.FirebaseUser user = dSnapshot.getValue(AccountDto.FirebaseUser.class); // for(DataSnapshot ds : dataSnapshot.getChildren()) {} .child("Address")
                         tokenList.add(user.getExpoPushToken());
                     }


                     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                     expoNotificationService.sendMulti(tokenList, "[ "+work.getEquipment()+" ] 일감 등록됨", "#시작일: "+dateFormat.format(work.getStartDate())+", #장소: "+work.getAddress(), ExpoNotiData.NOTI_WORK_REGISTER);
//                     done.countDown();
                 }

                 @Override
                 public void onCancelled(DatabaseError databaseError) {
                 }
             });
        }


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

    @RequestMapping(value="works/firm/accept", method = RequestMethod.PUT)
    public ResponseEntity acceptWork(@RequestBody @Valid WorkDto.Accept accept, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            throw new JBBadRequestException();
        }

        boolean result = service.acceptWork(accept);

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
