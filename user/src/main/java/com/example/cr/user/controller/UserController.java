package com.example.cr.user.controller;

import com.example.cr.user.request.UserRequest;
import com.example.cr.user.response.R;
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
    public R<Long> count() {
        return R.ok(userService.count());
    }

    @PostMapping("/register")
    public R<Integer> register(UserRequest request) {
        return R.ok(userService.register(request));
    }
}
