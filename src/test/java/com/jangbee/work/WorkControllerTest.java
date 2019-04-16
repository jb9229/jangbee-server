package com.jangbee.work;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Created by test on 2019-04-15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class WorkControllerTest {
    MockMvc mockMvc = null;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    private FilterChainProxy filterChainProxy;

    @Autowired
    ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(filterChainProxy)
                .build();

        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource("/firebase-authentication.json").getInputStream()))
                    .setDatabaseUrl("https://jangbee-inpe21.firebaseio.com")
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static WorkDto.Create createWorkDto() {
        WorkDto.Create createDto = new WorkDto.Create();
        createDto.setEquipment("크레인");
        createDto.setAddress("오산 외삼미동");
        createDto.setAddressDetail("잘 찾아와");
        createDto.setAddrLatitude(35.690);
        createDto.setAddrLongitude(120.2434);
        createDto.setDetailRequest("망고 일임");
        createDto.setPeriod((float)0.8);
        createDto.setStartDate(new Date());
        return createDto;
    }

    @Test
    public void create() throws Exception {
        WorkDto.Create createDto =   createWorkDto();

        ResultActions result = mockMvc.perform(post("/api/v1/work")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        result.andDo(print());

    }

}