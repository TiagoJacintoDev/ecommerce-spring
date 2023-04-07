package com.tiago.ecommerce.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@UtilityClass
public class EncodingUtils {
    public String encode(String password) {
        var passwordEncoder = new BCryptPasswordEncoder();
        var encodedPassword = passwordEncoder.encode(password);

        if(passwordEncoder.matches(password, encodedPassword)){
            return encodedPassword;
        }

        return password;
    }
}
