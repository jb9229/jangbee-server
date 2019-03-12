package com.jangbee.expo;

import com.jangbee.utils.RestTemplateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
        String token = "ExponentPushToken[7ClYvcCbF-fuDCUEnus-u9]";
        String title = "Spring Test";
        String body = "Is it sueccess?";
        expoNotificationService.sendSingle(token, title, body);
    }

}