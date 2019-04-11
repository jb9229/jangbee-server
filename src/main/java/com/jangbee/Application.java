package com.jangbee;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Properties;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

/**
 * Created by test on 2015-10-29.
 */
@EnableScheduling
@SpringBootApplication
public class Application {

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
//        return application.sources(Application.class);
//    }

    static String FBDB_BASE_URL="https://jangbee-inpe21.firebaseio.com";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource("/firebase-authentication.json").getInputStream()))
                    .setDatabaseUrl(FBDB_BASE_URL)
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Bean
//    public DatabaseReference firebaseDatabse() {
//        DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
//        return firebase;
//    }

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
