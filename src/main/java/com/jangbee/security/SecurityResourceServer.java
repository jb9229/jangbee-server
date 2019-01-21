package com.jangbee.security;

/**
 * Created by test on 2016-09-24.
 */

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class SecurityResourceServer extends ResourceServerConfigurerAdapter {
    public static final String RESOURCE_ID = "REST_SERVICE";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(RESOURCE_ID);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .antMatchers("/api/v1/**").hasRole("USER")
                .antMatchers("/api/**").hasRole("USER")
                .antMatchers("/api/vi/*").hasRole("USER")
                .anyRequest().permitAll();
//                .and()
//                .requiresChannel()        //https
//                .anyRequest().requiresSecure();

//        http.portMapper().http(8080).mapsTo(8443);
    }
}
