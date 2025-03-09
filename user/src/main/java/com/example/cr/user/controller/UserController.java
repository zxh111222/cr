package com.example.cr.user.controller;

import com.example.cr.user.request.SendCodeRequest;
import com.example.cr.user.request.UserRequest;
import com.example.cr.common.response.R;
import com.example.cr.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public R<Integer> register(@Valid @RequestBody UserRequest request) {
        return R.ok(userService.register(request));
    }

    @PostMapping("/send-code")
    public R<Object>  sendConde(@Valid @RequestBody SendCodeRequest request) {
        userService.sendCode(request);
        return R.ok();
    }
}
