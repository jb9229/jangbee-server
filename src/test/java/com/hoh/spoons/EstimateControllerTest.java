package com.hoh.spoons;

import com.hoh.account.AccountControllerTest;
import com.jangbee.accounts.Account;
import com.jangbee.accounts.AccountDto;
import com.jangbee.accounts.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by jeong on 2016-02-23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class EstimateControllerTest {

    MockMvc mockMvc = null;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    private FilterChainProxy filterChainProxy;



    @Autowired
    AccountService accountService;


    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(filterChainProxy)
                .build();
    }


    @Test
    public void testAddRice() throws Exception {
        AccountDto.Create create        =   AccountControllerTest.accountCreateFixture();

        Account account                 =   accountService.createAccount(create);


        ResultActions result = mockMvc.perform(get("/api/v1/spoon/rice/add/" + account.getId() + "/150")
                .with(httpBasic(create.getEmail(), create.getPassword())));

        result.andDo(print());
        result.andExpect(status().isOk());
    }
}