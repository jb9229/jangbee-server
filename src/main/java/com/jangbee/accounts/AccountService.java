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


    public Account createAccount(AccountDto.Create dto)
    {
        Account account = this.modelMapper.map(dto, Account.class);

        String username = dto.getUsername();
        if (this.repository.findByUsername(username) != null) {
            throw new UserDuplicatedException(username);
        }
        account.setPassword(this.passwordEncoder.encode(account.getPassword()));

        Date date = new Date();
        account.setJoined(date);
        account.setUpdated(date);

        return this.repository.save(account);
    }

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

    public Account updateAccount(Account account, AccountDto.Update updateDto)
    {
        account.setUsername(updateDto.getUsername());

        return (Account)this.repository.save(account);
    }

    public Account updateRiceAccount(Account account, AccountDto.RiceUpdate updateDto)
    {
        account.setRiceTol(updateDto.getRiceTol());
        account.setRiceMonth(updateDto.getRiceMonth());
        account.setRiceYear(updateDto.getRiceYear());
        account.setRiceTemp(updateDto.getRiceTemp());

        return (Account)this.repository.save(account);
    }

    public void deleteAccount(Long id)
    {
        this.repository.delete(getAccount(id));
    }

    public int addRice(Account account, AccountDto.RiceUpdate riceUpdate, int rice)
    {
        riceUpdate.setRiceTol(account.getRiceTol() + rice);
        riceUpdate.setRiceMonth(account.getRiceMonth() + rice);
        riceUpdate.setRiceYear(account.getRiceYear() + rice);

        riceUpdate.setRiceTemp(account.getRiceTemp() + rice);


        int donationRice    =   0;
        if (riceUpdate.getRiceTemp() > 100)
        {
            double donationRiceOri = riceUpdate.getRiceTemp() / 100;

            donationRice = (int)donationRiceOri;

            donationRice *= 100;



            riceUpdate.setRiceTemp(account.getRiceTemp() - donationRice);
        }
        else
        {
            donationRice = 0;
        }
        return donationRice;
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
