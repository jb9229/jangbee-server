package com.jangbee.client_evalu;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Created by test on 2019-03-27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ClientEvaluControllerTest {
    MockMvc mockMvc = null;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    private FilterChainProxy filterChainProxy;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(filterChainProxy)
                .build();
    }

//    .contentType(MediaType.APPLICATION_JSON)
//    .content(objectMapper.writeValueAsString(createDto)));
    @Test
    public void existTelNumber() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/v1/client/evaluation/exist/telnumber")
                .param("telNumber", "0101112222"));

        result.andDo(print());
        result.andExpect(content().string("true"));

        result = mockMvc.perform(get("/api/v1/client/evaluation/exist/telnumber")
                .param("telNumber", "0101111111"));

        result.andDo(print());
        result.andExpect(content().string("false"));

    }

}