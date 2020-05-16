package com.example;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Linmo
 * @create 2020/5/15 22:57
 */
public class Oauth2Test {
    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("oauth2"));
    }
}
