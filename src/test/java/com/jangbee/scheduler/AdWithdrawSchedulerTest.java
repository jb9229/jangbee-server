package com.jangbee.scheduler;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.jangbee.ad.AdRepository;
import com.jangbee.ad.AdService;
import com.jangbee.expo.ExpoNotificationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by test on 2019-03-13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AdWithdrawSchedulerTest {
    static String FBDB_BASE_URL="https://jangbee-inpe21.firebaseio.com";

    @Autowired
    AdWithdrawScheduler scheduler;

    @Autowired
    AdService adservice;
    @Autowired
    AdRepository adRepository;

    @Autowired
    ExpoNotificationService expoNotificationService;

    @Test
    public void checkRenewalAccToken() throws Exception {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource("/firebase-authentication.json").getInputStream()))
                    .setDatabaseUrl(FBDB_BASE_URL)
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
        }
        scheduler.checkRenewalAccToken();

        Thread.sleep(50000);
    }

}