package com.qa.springust;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class SpringustApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SpringustApplication.class, args);
    }

}
