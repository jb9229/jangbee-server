package com.jangbee.accounts;

public class AccountNotFoundException extends RuntimeException {
    Long id;
    String email;


    //Constuctor
    public AccountNotFoundException(Long id){this.id = id;}

    public AccountNotFoundException(String email)
    {
        this.email = email;
    }


    //Method
    public Long getId()
    {
        return this.id;
    }

    public String getEmail()
    {
        return this.email;
    }
}
