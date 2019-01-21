package com.jangbee.accounts;

import org.springframework.security.core.AuthenticationException;

public class AccountNotAuthEmailException extends AuthenticationException {
    String email;

    public AccountNotAuthEmailException(String email) {
        super(email);
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }
}
