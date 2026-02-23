package com.example.vibeapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@Controller
public class VibeApp {
    public static void main(String[] args) {
        SpringApplication.run(VibeApp.class, args);
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/api/hello")
    @ResponseBody
    public String hello() {
        return "Hello, Vibe!";
    }
}
