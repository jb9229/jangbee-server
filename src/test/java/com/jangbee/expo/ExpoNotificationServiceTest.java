package com.jangbee.expo;

import com.jangbee.utils.RestTemplateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by test on 2019-03-12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ExpoNotificationServiceTest {
    @Autowired
    RestTemplateUtils restTemplateUtils;

    @Autowired
    ExpoNotificationService expoNotificationService;

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
}