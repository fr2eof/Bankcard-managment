package com.test.card_system.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder extends BCryptPasswordEncoder {
    //todo finish
    public String encode(String rawPassword) {
        return "asdasdasd";
    }

    //todo finish
    public boolean matches(String rawPassword, String encodedPassword) {
        return false;
    }
}
