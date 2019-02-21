package com.jangbee.accounts;

import com.jangbee.common.Email;
import com.jangbee.common.EmailSender;
import com.jangbee.common.ErrorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by test on 2015-10-18.
 */
@RestController
@RequestMapping("/api/v1/")
public class AccountController {
    public static final int INIT_PASSWORD_DIGIT     =   8;

    public static final String REGIST_MAIL_TITLE        =   "[�ʽ��Ϲ�]������ ���� �帳�ϴ�, �̸��� ������ �� �ּ���";
    public static final String INIT_MAIL_TITLE          =   "[�ʽ��Ϲ�] ��й�ȣ �ʱ�ȭ �߽��ϴ�";


    @Autowired
    private MessageSource messageSource;


    @Autowired
    private AccountService service;

    @Autowired
    private AccountRepository repository;

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private EmailSender emailSender;



    @RequestMapping(value="/accounts", method = RequestMethod.POST)
    public ResponseEntity createAccount(@RequestBody @Valid AccountDto.Create create, BindingResult result) throws MessagingException {

        if(result.hasErrors())
        {
            ErrorResponse errorResponse =   new ErrorResponse();

            errorResponse.setMessage(result.toString());
            errorResponse.setCode("bed.request");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        double authMailKey  =   Math.random();
        create.setAuthMailKey(authMailKey);

        Account newAccount  =   service.createAccount(create);



        Email authenMail    =   new Email();
        authenMail.setSubject(REGIST_MAIL_TITLE);
        authenMail.setReceiver(newAccount.getEmail());
        authenMail.setContent("<p> �ʽ��Ϲ� ������ ���� �帳�ϴ�, �ϱ� ��ũ�� �̸��� ������ �Ϸ� �� �ּ���.</p> <a href='http://tenspoon.elasticbeanstalk.com/api/v1/accounts/auth/" + newAccount.getEmail() + "/" + authMailKey + "'>�̸��� �����Ϸ� ����</a>");

        emailSender.sendMail(authenMail);


        return new ResponseEntity<>(modelMapper.map(newAccount, AccountDto.Response.class), HttpStatus.CREATED);
    }


    @RequestMapping(value = "/accounts/auth/{email}/{key}", method = RequestMethod.GET)
    public ResponseEntity authAccountMail(@PathVariable String email, @PathVariable Double key){
        Account account         =   service.getAccount(email);

        account.setAuthMailkey(null);

        Account updateAccount   =   repository.save(account);

        return new ResponseEntity<>(modelMapper.map(updateAccount, AccountDto.Response.class),
                HttpStatus.OK);

    }

    @RequestMapping(value="/accounts/bowl/{bowlId}", method = GET)
    public ResponseEntity getBowlAccounts(@PathVariable Long bowlId, Pageable pageable){


        //TODO findByBow ���� �ذ�
//        Page<Account> page              =      repository.findByBowl(bowlId, pageable);

        Page<Account> page              = null;

        List<AccountDto.Response> content = page.getContent().parallelStream()
                .map(newAccount -> modelMapper.map(newAccount, AccountDto.Response.class))
                .collect(Collectors.toList());


        PageImpl<AccountDto.Response> result    =   new PageImpl<>(content, pageable, page.getTotalElements());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @RequestMapping(value="/accounts", method = GET)
    public ResponseEntity getAccounts(Account account, Pageable pageable){

        Specification<Account> spec     =   Specifications.where(AccountSpecs.emailEqual(account.getEmail()));//spec  =   spec.and()


        Page<Account> page              =      repository.findAll(spec, pageable);


        List<AccountDto.Response> content = page.getContent().parallelStream()
                .map(newAccount -> modelMapper.map(newAccount, AccountDto.Response.class))
                .collect(Collectors.toList());


        PageImpl<AccountDto.Response> result    =   new PageImpl<>(content, pageable, page.getTotalElements());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @RequestMapping(value="/accounts/{id}", method = GET )
    public ResponseEntity getAccount(@PathVariable Long id) {
        Account account =   service.getAccount(id);

        AccountDto.Response response        =   modelMapper.map(account, AccountDto.Response.class);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @RequestMapping(value="/accounts/password/", method = PUT)
    public ResponseEntity initPassword(@RequestParam("email") String emailAdd) throws Exception{

        Account account             =   service.getAccount(emailAdd);


        String randomPW             =   service.calRandomPW(INIT_PASSWORD_DIGIT);

        AccountDto.Update update    =   new AccountDto.Update();
        update.setPassword(randomPW);
        update.setUsername(account.getUsername());


        System.out.println(">>>>>>"+emailAdd);

        Email email     =   new Email();
        email.setSubject(INIT_MAIL_TITLE);
        email.setContent("<div>�ȳ��ϼ���. �ʽ��Ϲ� �Դϴ�." +
                "<br><br>" +
                "������ ��й�ȣ�� �ʱ�ȭ �߽��ϴ�." +
                "<br><br>" +
                "<ul><li><b>�ʱ�ȭ ���: " + randomPW + "<b></li></ul>" +
                "<br><br>" +
                "<p>�����մϴ�.</p>" +
                "</div>");
        email.setReceiver(emailAdd);

        emailSender.sendMail(email);


        Account updateAccount = service.updateAccount(account, update);


        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping(value="/accounts/{id}", method = PUT)
    public ResponseEntity updateAccount(@PathVariable Long id, @RequestBody @Valid AccountDto.Update updateDto, BindingResult result){

        if(result.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Account account     =   repository.findOne(id);

        if(account  ==  null)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Account updateAccount     =   service.updateAccount(account, updateDto);

        return new ResponseEntity<>(modelMapper.map(updateAccount, AccountDto.Response.class),
                HttpStatus.OK);
    }


    @RequestMapping(value="/accounts/{id}", method = DELETE)
    public ResponseEntity deleteAccount(@PathVariable Long id){
        service.deleteAccount(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



    //Exception Handler Method
    @ExceptionHandler(UserDuplicatedException.class)@ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerUserDuplicatedException(UserDuplicatedException e){
        ErrorResponse   errorResponse   =   new ErrorResponse();
        errorResponse.setMessage("["+ e.getUsername() + "] �ߺ��� username �Դϴ�.");
        errorResponse.setCode("duplicated.username.exception");

        return errorResponse;
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerAccountNotFoundException(AccountNotFoundException e){
        ErrorResponse errorResponse  =   new ErrorResponse();
        errorResponse.setMessage("["+ e.getId()+"]�� �ش��ϴ� ������ �����ϴ�.");
        errorResponse.setCode("account.not.found.exception");

        return errorResponse;
    }

    @ExceptionHandler(MessagingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerMessagingException(MessagingException e){
        ErrorResponse errorResponse  =   new ErrorResponse();
        errorResponse.setMessage("���� ���ۿ� ���� �߽��ϴ�.");
        errorResponse.setCode("mail.send.fail.exception");

        return errorResponse;
    }

}
