package com.jangbee.work;

import com.google.firebase.database.*;
import com.jangbee.accounts.AccountDto;
import com.jangbee.ad.AdDto;
import com.jangbee.common.JBBadRequestException;
import com.jangbee.expo.ExpoNotiData;
import com.jangbee.expo.ExpoNotificationService;
import com.jangbee.firm.Firm;
import com.jangbee.firm.FirmService;
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
import java.util.concurrent.CountDownLatch;
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
    private FirmService firmService;

    @Autowired
    ExpoNotificationService expoNotificationService;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(value="work", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody @Valid WorkDto.Create create, BindingResult result) {

        if(result.hasErrors()){
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
                     expoNotificationService.sendMulti(tokenList, "[ "+work.getEquipment()+" ]일감 등록됨", "시작일: "+dateFormat.format(work.getStartDate())+", 장소: "+work.getAddress(), ExpoNotiData.NOTI_WORK_REGISTER);
//                     done.countDown();
                 }

                 @Override
                 public void onCancelled(DatabaseError databaseError) {
//                     done.countDown();
                 }
             });

//            try {
//                done.await(); //it will wait till the response is received from firebase.
//            } catch(InterruptedException e) {
//                e.printStackTrace();
//            }
        }


        return new ResponseEntity<>(modelMapper.map(work, WorkDto.Response.class), HttpStatus.OK);
    }

    @RequestMapping(value="works/firm/working", method = RequestMethod.GET)
    public ResponseEntity getFirmWorkList(@RequestParam String equipment, @RequestParam String accountId) {
        List<Work> works = service.getFirmWorkingList(equipment, accountId);

        List<WorkDto.Response> content = works.parallelStream()
                .map(work -> modelMapper.map(work , WorkDto.Response.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(content, HttpStatus.OK);
    }
}
