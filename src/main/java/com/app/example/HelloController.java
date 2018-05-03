package com.app.example;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@EnableAutoConfiguration
public class HelloController 
{
    @RequestMapping("/restCtr1")
    @ResponseBody
    public String home() {
        return "Spring Hello World!";
    }
    
    
    
}