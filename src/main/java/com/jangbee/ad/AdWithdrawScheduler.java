package com.jangbee.ad;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jangbee.accounts.AccountDto;
import com.jangbee.common.Email;
import com.jangbee.common.EmailSender;
import com.jangbee.expo.ExpoNotiData;
import com.jangbee.expo.ExpoNotificationService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
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
    @Autowired
    EmailSender emailSender;
    @Autowired AdRepository adRepository;

    @Autowired
    ExpoNotificationService expoNotificationService;

    @Value( "${expo.admin.pushtoken}" )
    private String expoAdminPushtoken;

    @Value( "${admin.email}" )
    private String adminEmail;

//    @Scheduled(cron = "30 * * * * *")
    @Scheduled(cron = "30 40 09 * * *")
    public void checkRenewalAccToken(){
        System.out.println("=== Start Scheduling: checkRenewalAccToken ===");
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, +20);
        Date afterTwentyDay = cal.getTime();
        List<Ad> asList = adRepository.findAll();


        // Ad Loop
        List<String> atDisMsgSendUserList  = new ArrayList();
        asList.parallelStream().forEach((ad) -> {
            System.out.println("=== Start Ad Check: "+ad.getAccountId()+" ===");
            FirebaseDatabase.getInstance().getReference("users/" +ad.getAccountId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("=== Get Firebase datasnapshot: "+dataSnapshot.toString()+" ===");
                    AccountDto.FirebaseUser user = dataSnapshot.getValue(AccountDto.FirebaseUser.class); // for(DataSnapshot ds : dataSnapshot.getChildren()) {} .child("Address")
                    System.out.println("=== Get Firebase user ExpDate: "+user.getObAccTokenExpDate()+" ===");
                    // 1년 토큰 만료 확인
                    Date discardDate = user.parseObAccTokenDiscDate();   // For Test Environment Null Check
                    Date expireDate = user.parseObAccTokenExpDate(); // For Test Environment Null Check
                    if(discardDate != null && afterTwentyDay.after(discardDate)){
                        if(atDisMsgSendUserList.contains(user.getExpoPushToken())) { return; }
                        expoNotificationService.sendSingle(user.getExpoPushToken(), "광고비 이체통장 재인증 요청", "보안상 1년마다 이체통장 재인증을 받아야 합니다", ExpoNotiData.NOTI_OBAT_DISCARD);
                        atDisMsgSendUserList.add(user.getExpoPushToken());
                    }

                    // 3개월 토큰 갱신 확인
                    if(expireDate != null && now.after(expireDate)){
                        try {
                            boolean result = adservice.refreshObtoken(user.getObAccessToken(), user.getObRefreshToken(), ad);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // 광고비 이체
                    Calendar afterTenDayCal = Calendar.getInstance();
                    afterTenDayCal.add(Calendar.DAY_OF_MONTH, 10);
                    if(ad.getNextWithdrawDate().before(ad.getEndDate()) && now.after(ad.getNextWithdrawDate())){
                        boolean result = adservice.obTransferWithdraw(user.getObAccessToken(), ad.getFintechUseNum(), ad.getPrice());

                        if(!result){
                            Calendar afterSevenCal = Calendar.getInstance();
                            afterSevenCal.setTime(ad.getNextWithdrawDate());
                            afterSevenCal.add(Calendar.DAY_OF_MONTH, +7);
                            Date afterSevenDateFromWithdraw = afterSevenCal.getTime();
                            if(now.after(afterSevenDateFromWithdraw)){
                                adRepository.delete(ad.getId());
                            }else{
                                expoNotificationService.sendSingle(user.getExpoPushToken(), "광고비 입금 실패", "등록 계좌의 잔액 및 통장 재인증이 필요한지 확인 해 주세요", ExpoNotiData.NOTI_OBAT_FAIL_WITHDRAW);
                                sendWithdrawFailEmail(ad);
                            }
                        }else{
                            Calendar nextMonthCal = Calendar.getInstance();
                            nextMonthCal.setTime(ad.getNextWithdrawDate());
                            nextMonthCal.add(Calendar.MONTH, 1);

                            ad.setNextWithdrawDate(nextMonthCal.getTime());


                            adRepository.saveAndFlush(ad);
                        }
                    }else if(!now.after(ad.getEndDate()) && afterTenDayCal.getTime().after(ad.getEndDate())){
                        long calDate = ad.getEndDate().getTime() - now.getTime();
                        long calDateDays = calDate / ( 24*60*60*1000);

                        if( (8 < calDateDays && calDateDays <= 9) || (4 < calDateDays && calDateDays <= 5) || (2 < calDateDays && calDateDays <= 3) || (0 < calDateDays && calDateDays <= 1)){
                            expoNotificationService.sendSingle(user.getExpoPushToken(), "광고일 기간 만료", "곧 장비콜 광고가 만료 됩니다, 기간연장 가능 합니다(만료일 후 광고 삭제됨)", null);
                        }
                    }else if(now.after(ad.getEndDate())){
                        adRepository.delete(ad.getId());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    expoNotificationService.sendSingle(expoAdminPushtoken, "Firebase DB 조회 오류", "광고 토크폐기일 체크를 위한 사용자 조회 실패, 관리자에게 문의해 주세요", null);
                }
            });
        });
    }

    private void sendWithdrawFailEmail(Ad ad) {
        Email email = new Email();
        email.setSender(adminEmail);
        email.setReceiver(adminEmail);
        email.setSubject("["+ad.getAccountId()+"] 광고비 이체 실패");
        String eamilContents = "<p># 광고비 이체실패 정보</p> <p>db id: "+ad.getId()+"</P><p>전화번호: "+ad.getTelNumber()+"</p><p> 광고타이틀: "+ad.getTitle()+"</p><p> 광고서브타이틀: "+ad.getSubTitle()+"</p><p> 계좌Fintech Number: "+ad.getFintechUseNum()+"</p>";
        email.setContent(eamilContents);
        try {
            emailSender.sendMail(email);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}