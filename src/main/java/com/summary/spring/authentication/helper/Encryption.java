package com.summary.spring.authentication.helper;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class Encryption {

    @Bean
    public PasswordEncoder encoder() {
        SecureRandom secureRandom = new SecureRandom();
        return new BCryptPasswordEncoder(11, secureRandom);
    }
}
