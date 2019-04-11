package com.jangbee.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by test on 2019-03-08.
 */
@Service
public class RestTemplateUtils<T> {
    public ResponseEntity<T> postForObject(String url, Object jsonDatStr, String authToken, Class<T> typeParameterClass, MediaType contentType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(contentType);
        if(authToken != null){headers.add("Authorization", "Bearer " + authToken);}

        HttpEntity param= new HttpEntity(jsonDatStr, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<T> result = restTemplate.postForEntity(url, param, typeParameterClass);

        return result;
    }
}
