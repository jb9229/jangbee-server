package com.jangbee.expo;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jangbee.accounts.AccountDto;
import com.jangbee.firm.Firm;
import com.jangbee.firm.FirmRepository;
import com.jangbee.firm.FirmService;
import com.jangbee.utils.RestTemplateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

/**
 * Created by test on 2019-03-12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ExpoNotificationServiceTest {
    static String FBDB_BASE_URL="https://jangbee-inpe21.firebaseio.com";
    @Autowired
    RestTemplateUtils restTemplateUtils;

    @Autowired
    ExpoNotificationService expoNotificationService;

    @Autowired
    FirmRepository firmRepository;

    @Test
    public void sendSingle() throws Exception {
//        String token = "ExponentPushToken[7ClYvcCbF-fuDCUEnus-u9]";
//        String token = "ExponentPushToken[eimkOmCQ17ppKOc90zAJoI]"; //채범
        String token = "ExponentPushToken[BHXHaCGytnBJ-maSVBjRc6]"; // 01052023337
        List tokenList = new ArrayList();
        tokenList.add("ExponentPushToken[BHXHaCGytnBJ-maSVBjRc6]");
//        tokenList.add("ExponentPushToken[dRAH9uOaF1c2wX2_ZAaxVP]");
//        tokenList.add("ExponentPushToken[DbLfJjORTCLGWBTD6EFiWO]");
        String title = "Multi line Test";
        String body = "Is it sueccess? \n seconde line";
        String channelId = "channelId"; // android 8.0 later
//        expoNotificationService.sendSingle(token, title, body, ExpoNotiData.NOTI_OBAT_DISCARD);
        expoNotificationService.sendMulti(tokenList, "[ 5톤 카고크레인 ] 일감 올라옴", "#배차기간:\n"+"  2019. 05. 16 ~ 2019. 05. 16(오후)\n\n#배차장소:\n"+"  세종시 정부청사 건설현장", ExpoNotiData.NOTI_OBAT_DISCARD);
    }

    @Test
    public void sendFirmNotice() throws Exception {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource("/firebase-authentication.json").getInputStream()))
                    .setDatabaseUrl(FBDB_BASE_URL)
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final CountDownLatch counter = new CountDownLatch(1);
        List<String> firmAccountIdList = firmRepository.findAllAccountId();
        FirebaseDatabase.getInstance().getReference("users").orderByChild("userType").equalTo(2).addListenerForSingleValueEvent(
        new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> noneRegiFirmExpoTokenList = new ArrayList<String>();
                System.out.println("FB 가입 장비업체수: "+dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    AccountDto.FirebaseUser user = postSnapshot.getValue(AccountDto.FirebaseUser.class);

                    String accountId = postSnapshot.getKey();

                    if(!firmAccountIdList.contains(accountId) && user.getExpoPushToken() != null) {
                        noneRegiFirmExpoTokenList.add(user.getExpoPushToken());
                        System.out.println(accountId);
                    }
                }
                System.out.println("token Size:"+ noneRegiFirmExpoTokenList.size());
                for(String eToken : noneRegiFirmExpoTokenList) {
                    System.out.println(eToken);
                }
//                noneRegiFirmExpoTokenList = new ArrayList<String>();
                noneRegiFirmExpoTokenList.add("ExponentPushToken[BHXHaCGytnBJ-maSVBjRc6]");
                noneRegiFirmExpoTokenList.add("ExponentPushToken[eimkOmCQ17ppKOc90zAJoI]");
                expoNotificationService.sendMulti(noneRegiFirmExpoTokenList, "내장비 등록이 안되어 있습니다!!", "내정보 -> [내장비 등록] 메뉴로 가셔서 등록하세요\n\n1. 장비등록이 되어야 일감 콜 받을 수 있습니다.\n2. 장비등록이 되어야 악덕등록 알람을 받을 수 있습니다.\n\n※ 등록시 어려운점은 내정보 -> 카톡상담누르시면 도움 드리겠습니다.", null);
                counter.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        counter.await();
    }
}