package com.jangbee.security;

/**
 * Created by test on 2016-09-24.
 */

import com.jangbee.accounts.Account;
import com.jangbee.accounts.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    AccountRepository accountRepository;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account     =   this.accountRepository.findByEmail(email);

        if (account == null)
        {
            throw new UsernameNotFoundException(email);
        }

//        if (account.getAuthMailkey() != null)
//        {
//            throw new AccountNotAuthEmailException("[" + email + "] 해당 계정을 찾을 수 없습니다.");
//        }

        return new UserDetailsImpl(account);
    }
}