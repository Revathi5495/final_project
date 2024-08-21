package com.sails.client_connect.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class UserController {

    @GetMapping("/hello")
    public String sayHello(){
        return "hello";
    }

    @GetMapping("/hello-world")
    public String sayHello2(){
        return "hello World";
    }

}
