package com.sails.client_connect.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskDairyManagementController {
    @GetMapping("/hello")
    public String sayHello()
    {
        return "Hello World";
    }
}
