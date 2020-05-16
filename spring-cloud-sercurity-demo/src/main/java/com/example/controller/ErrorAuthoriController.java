package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * @author Linmo
 * @create 2020/5/15 17:31
 */
@SessionAttributes("authorizationRequest")
@RestController
public class ErrorAuthoriController {

    @GetMapping("/login")
    public String login(){
        return "login page";
    }
}
