package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Linmo
 * @create 2020/5/15 17:48
 */
@RestController
public class ResourceController {

    @GetMapping("/users")
    public List<String> userList(){
        return Arrays.asList("user1","user2");
    }

    @GetMapping("/map")
    public Map<String,Principal> userList(Principal principal){
        return Collections.singletonMap(principal.getName(),principal);
    }
}
