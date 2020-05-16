package com.example;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Linmo
 * @create 2020/5/15 21:30
 */
public class PasswordEncoder {
    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("acmesecret"));
    }
}
