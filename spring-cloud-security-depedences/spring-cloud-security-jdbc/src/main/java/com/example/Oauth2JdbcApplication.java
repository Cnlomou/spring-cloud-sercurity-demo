package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Linmo
 * @create 2020/5/15 22:29
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.example.mapper"})
public class Oauth2JdbcApplication {
    public static void main(String[] args) {
        SpringApplication.run(Oauth2JdbcApplication.class,args);
    }
}
