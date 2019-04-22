package com.jangbee.common;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jangbee.accounts.AccountDto;
import com.jangbee.expo.ExpoNotiData;
import com.jangbee.expo.ExpoNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by test on 2019-04-19.
 */
@Service
public class JangbeeNoticeService {
    @Autowired
    ExpoNotificationService expoNotificationService;

    public void noticeCommonMSG(String accountId, String msgTitle, String msgBody, String noticeType) {
        FirebaseDatabase.getInstance().getReference("users/" +accountId).addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    AccountDto.FirebaseUser user = dataSnapshot.getValue(AccountDto.FirebaseUser.class);
                    expoNotificationService.sendSingle(user.getExpoPushToken(), msgTitle, msgBody, noticeType);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }
}
