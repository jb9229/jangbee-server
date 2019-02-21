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

    public static final String REGIST_MAIL_TITLE        =   "[십시일반]가입을 축하 드립니다, 이메일 인증을 해 주세요";
    public static final String INIT_MAIL_TITLE          =   "[십시일반] 비밀번호 초기화 했습니다";


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
        authenMail.setContent("<p> 십시일반 가입을 축하 드립니다, 하기 링크로 이메일 인증을 완료 해 주세요.</p> <a href='http://tenspoon.elasticbeanstalk.com/api/v1/accounts/auth/" + newAccount.getEmail() + "/" + authMailKey + "'>이메일 인증하러 가기</a>");

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


        //TODO findByBow 문제 해결
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
        email.setContent("<div>안녕하세요. 십시일반 입니다." +
                "<br><br>" +
                "고객님의 비밀번호를 초기화 했습니다." +
                "<br><br>" +
                "<ul><li><b>초기화 비번: " + randomPW + "<b></li></ul>" +
                "<br><br>" +
                "<p>감사합니다.</p>" +
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
        errorResponse.setMessage("["+ e.getUsername() + "] 중복된 username 입니다.");
        errorResponse.setCode("duplicated.username.exception");

        return errorResponse;
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerAccountNotFoundException(AccountNotFoundException e){
        ErrorResponse errorResponse  =   new ErrorResponse();
        errorResponse.setMessage("["+ e.getId()+"]에 해당하는 계정이 없습니다.");
        errorResponse.setCode("account.not.found.exception");

        return errorResponse;
    }

    @ExceptionHandler(MessagingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerMessagingException(MessagingException e){
        ErrorResponse errorResponse  =   new ErrorResponse();
        errorResponse.setMessage("메일 전송에 실패 했습니다.");
        errorResponse.setCode("mail.send.fail.exception");

        return errorResponse;
    }

}
