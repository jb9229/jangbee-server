package com.jangbee.accounts;

import java.util.Date;

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

    public static final int ORDERNUMBER_ADTS_RETURN  =   1;

    @Autowired
    private AccountRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account getAccount(Long id)
    {
        Account account = (Account)this.repository.findOne(id);
        if (account == null) {
            throw new AccountNotFoundException(id);
        }
        return account;
    }

    public Account getAccount(String email)
    {
        Account account = this.repository.findByEmail(email);
        if (account == null) {
            throw new AccountNotFoundException(email);
        }
        return account;
    }

    public String calRandomPW(int length)
    {
        int index = 0;

        char[] charSet = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };




        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++)
        {
            index = (int)(charSet.length * Math.random());
            sb.append(charSet[index]);
        }
        return sb.toString();
    }

}
