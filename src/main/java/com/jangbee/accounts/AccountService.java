package com.jangbee.accounts;

import java.util.Date;

import com.jangbee.ad.AdService;
import com.jangbee.client_evalu.ClientEvalu;
import com.jangbee.client_evalu.ClientEvaluService;
import com.jangbee.firm.FirmService;
import com.jangbee.work.WorkService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository repository;
    @Autowired
    private FirmService firmService;
    @Autowired
    private WorkService workService;
    @Autowired
    private ClientEvaluService clientEvaluService;
    @Autowired
    private AdService adService;

    public boolean deleteAccount(String accountId) {
        boolean result = false;
        if (firmService.deleteFirm(accountId) >= 0) {result = true;}
        workService.deleteByAccountId(accountId);
        clientEvaluService.deleteByAccountId(accountId);
        adService.deleteByAccountId(accountId);

        return result;
    }
}
