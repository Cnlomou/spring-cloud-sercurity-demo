package com.example.controller;

import com.example.pojo.Content;
import com.example.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Linmo
 * @create 2020/5/16 13:47
 */
@RestController
public class ContentController {
    @Autowired
    private ContentService contentService;
    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('SYSADMIN')")
    List<Content> findAll(){
        return contentService.findAll();
    }

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("admin"));
    }
}
