package com.example.cr.user.controller;

import com.example.cr.user.request.UserRequest;
import com.example.cr.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/count")
    public long count() {
        return userService.count();
    }

    @PostMapping("/register")
    public int register(UserRequest request) {
        return userService.register(request);
    }
}
