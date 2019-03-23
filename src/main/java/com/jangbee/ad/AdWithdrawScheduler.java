package com.jangbee.ad;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jangbee.accounts.AccountDto;
import com.jangbee.expo.ExpoNotiData;
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
@Component
public class AdWithdrawScheduler {
    @Autowired
    AdService adservice;
    @Autowired AdRepository adRepository;

    @Autowired
    ExpoNotificationService expoNotificationService;

    @Value( "${expo.admin.pushtoken}" )
    private String expoAdminPushtoken;

    @Scheduled(cron = "0 1 3 * * ?")
//    @Scheduled(cron = "30 * * * * ?")
    public void checkRenewalAccToken(){
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, -20);
        Date beforeTwentyDay = cal.getTime();
        List<Ad> asList = adRepository.findAll();


        // Ad Loop
        List<String> atDisMsgSendUserList  = new ArrayList();
        asList.parallelStream().forEach((ad) -> {
            FirebaseDatabase.getInstance().getReference("users/" +ad.getAccountId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    AccountDto.FirebaseUser user = dataSnapshot.getValue(AccountDto.FirebaseUser.class); // for(DataSnapshot ds : dataSnapshot.getChildren()) {} .child("Address")
                    // 1년 토큰 만료 확인
                    if(user.getObAcceTokenDiscDate().after(beforeTwentyDay)){
                        if(atDisMsgSendUserList.contains(user.getExpoPushToken())) { return; }
                        expoNotificationService.sendSingle(user.getExpoPushToken(), "광고비 이체통장 재인증 요청", "보안상 1년마다 이체통장 재인증을 받아야 합니다", ExpoNotiData.NOTI_OBAT_DISCARD);
                        atDisMsgSendUserList.add(user.getExpoPushToken());
                    }

                    // 3개월 토큰 갱신 확인
                    if(user.getObAcceTokenExpDate().after(now)){
                        try {
                            boolean result = adservice.refreshObtoken(ad);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // 광고비 이체
                    Calendar beforeTenDayCal = Calendar.getInstance();
                    beforeTenDayCal.add(Calendar.DAY_OF_MONTH, 10);
                    if(ad.getNextWithdrawDate().before(ad.getEndDate()) && ad.getNextWithdrawDate().after(now)){
                        boolean result = adservice.obTransferWithdraw(ad.getFintechUseNum(), ad.getPrice());

                        if(!result){
                            expoNotificationService.sendSingle(user.getExpoPushToken(), "광고비 입금 실패", "등록 계좌의 잔액 및 통장 재인증이 필요한지 확인 해 주세요", ExpoNotiData.NOTI_OBAT_FAIL_WITHDRAW);
                            cal.add(Calendar.DAY_OF_MONTH, +7);
                            Date afterWeekDay = cal.getTime();
                            if(ad.getNextWithdrawDate().after(afterWeekDay)){
                                adRepository.delete(ad);
                            }
                        }else{
                            Calendar nextMonthCal = Calendar.getInstance();
                            nextMonthCal.add(Calendar.MONTH, 1);

                            if(ad.getEndDate().after(nextMonthCal.getTime())){ // if late month it will be same day not after
                                ad.setNextWithdrawDate(nextMonthCal.getTime());
                            }else{
                                ad.setNextWithdrawDate(null);
                            }

                            adRepository.saveAndFlush(ad);
                        }
                    }else if(ad.getEndDate().after(beforeTenDayCal.getTime())){
                        long calDate = ad.getEndDate().getTime() - now.getTime();
                        long calDateDays = calDate / ( 24*60*60*1000);

                        if( (8 < calDateDays && calDateDays <= 9) || (4 < calDateDays && calDateDays <= 5) || (2 < calDateDays && calDateDays <= 3) || (0 < calDateDays && calDateDays <= 1)){
                            expoNotificationService.sendSingle(user.getExpoPushToken(), "광고일 기간 만료", "곧 장비콜 광고가 만료 됩니다, 기간연장 가능 합니다(만료일 후 광고 삭제됨)", null);
                        }
                    }else if(ad.getEndDate().after(now)){
                        adRepository.delete(ad);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    expoNotificationService.sendSingle(expoAdminPushtoken, "Firebase DB 조회 오류", "광고 토크폐기일 체크를 위한 사용자 조회 실패, 관리자에게 문의해 주세요", null);
                }
            });
        });
    }
}