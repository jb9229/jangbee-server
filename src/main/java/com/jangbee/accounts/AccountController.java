package com.jangbee.accounts;

import com.jangbee.common.Email;
import com.jangbee.common.EmailSender;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

/**
 * Created by test on 2015-10-18.
 */
@RestController
@RequestMapping("/api/v1/")
public class AccountController {
    @Autowired
    AccountService service;

    @Autowired
    EmailSender emailSender;
    @Value( "${admin.email}" )
    private String adminEmail;

    @RequestMapping(value="account", method = RequestMethod.DELETE)
    public ResponseEntity terminate(@RequestParam String accountId) throws JSONException, MessagingException {

        boolean result  =   service.deleteAccount(accountId);

        if (!result) {
            Email email = new Email();
            email.setSender(adminEmail);
            email.setReceiver(adminEmail);
            email.setSubject("회원정보 삭제 실패");
            email.setContent("삭제실패 회원정보: "+accountId);
            emailSender.sendMail(email);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
