package com.jangbee.scheduler;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jangbee.accounts.AccountDto;
import com.jangbee.ad.*;
import com.jangbee.expo.ExpoNotificationService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by test on 2019-03-08.
 */
//@Component
public class AdWithdrawScheduler {
    @Autowired
    AdService adservice;
    @Autowired AdRepository adRepository;

    @Autowired
    ExpoNotificationService expoNotificationService;

    @Value( "${expo.msg.title.arrivediscarddate}" )
    private String arriveDiscarddateExpoMsgTitle;

//    @Scheduled(cron = "0 1 3 * * ?")
//    @Scheduled(cron = "30 * * * * ?")
    public void checkRenewalAccToken(){
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, -25);
        Date beforeTwentyDay = cal.getTime();
        List<Ad> asList = adRepository.findAll();


        // Ad Loop
        List<String> atDisMsgSendUserList  = new ArrayList();
        label1: asList.parallelStream().forEach((ad) -> {
            // 오픈뱅크 토큰 폐기일 체크
            if(ad.getObAcctokenDiscdate().after(beforeTwentyDay)){
                FirebaseDatabase.getInstance().getReference("users/" +ad.getAccountId()).addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                       AccountDto.FirebaseUser user = dataSnapshot.getValue(AccountDto.FirebaseUser.class); // for(DataSnapshot ds : dataSnapshot.getChildren()) {} .child("Address")
                       if(atDisMsgSendUserList.contains(user.getExpoPushToken())) { return; }
                       expoNotificationService.sendSingle(user.getExpoPushToken(), "광고비 이체통장 재인증 요청", "보안상 1년마다 이체통장 재인증을 받아야 합니다");
                       atDisMsgSendUserList.add(user.getExpoPushToken());
                       // TODO notice 앱의 재인증 screen으로 navi
                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {
                       expoNotificationService.sendSingle("ExponentPushToken[7ClYvcCbF-fuDCUEnus-u9]", "Firebase DB 조회 오류", "광고 토크폐기일 체크를 위한 사용자 조회 실패, 관리자에게 문의해 주세요");
                   }
               });
            }

            // 오픈뱅크 토큰 만료일 체크
            if(ad.getObAcctokenExpdate().after(now)){
                try {
                    boolean result = adservice.refreshObtoken(ad);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // 광고비 월자동이체 체크
            if(ad.getNextWithdrawDate().after(now)){
                boolean result = adservice.obTransferWithdraw(ad.getFintechUseNum(), ad.getAdPrice());

                if(!result){
                    expoNotificationService.sendSingle("ExponentPushToken[7ClYvcCbF-fuDCUEnus-u9]", "광고비 이체 실패!", "이체 통장의 잔액을 확인 또는 전화문의해 주세요");
                    cal.add(Calendar.DAY_OF_MONTH, +32);
                    Date afterWeekDay = cal.getTime();
                    if(ad.getNextWithdrawDate().after(afterWeekDay)){
                        adRepository.delete(ad);
                    }
                }
            }
        });
    }
}
