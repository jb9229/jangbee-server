package com.jangbee;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Properties;

/**
 * Created by test on 2015-10-29.
 */
@SpringBootApplication
public class Application {

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
//        return application.sources(Application.class);
//    }


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JavaMailSenderImpl javaMailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setPort(587);
        javaMailSender.setDefaultEncoding("UTF-8");
        javaMailSender.setUsername("jb9229@gmail.com");
        javaMailSender.setPassword("wjdwlsqja83");


        Properties javaMailProperties   =   new Properties();
        javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");
        javaMailProperties.setProperty("mail.smtp.auth", "true");

        javaMailSender.setJavaMailProperties(javaMailProperties);

        return javaMailSender;
    }
}
