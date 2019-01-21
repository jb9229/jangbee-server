package com.jangbee.security;

/**
 * Created by test on 2016-09-24.
 */

import com.jangbee.accounts.Account;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserDetailsImpl extends User {

    public UserDetailsImpl(Account account) {
        super(account.getEmail(), account.getPassword(), authorities(account));
    }

    private static Collection<? extends GrantedAuthority> authorities(Account account) {
        List<GrantedAuthority> authorities = new ArrayList();

        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (account.isAdmin())
        {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return authorities;
    }
}