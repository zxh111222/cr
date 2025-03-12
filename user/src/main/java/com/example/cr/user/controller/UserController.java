package com.example.cr.user.controller;

import com.example.cr.common.response.LoginResponse;
import com.example.cr.common.util.CustomJWTUtils;
import com.example.cr.user.request.LoginRequest;
import com.example.cr.user.request.SendCodeRequest;
import com.example.cr.user.request.UserRequest;
import com.example.cr.common.response.R;
import com.example.cr.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@CrossOrigin("http://localhost:5173")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    CustomJWTUtils customJWTUtils;

    @GetMapping("/count")
    public R<Long> count() {
        return R.ok(userService.count());
    }

    @PostMapping("/register")
    public R<Integer> register(@Valid @RequestBody UserRequest request) {
        return R.ok(userService.register(request));
    }

    @PostMapping("/send-code")
    public R<Object> sendConde(@Valid @RequestBody SendCodeRequest request) {
        userService.sendCode(request);
        return R.ok();
    }

    @PostMapping("/login")
    public R<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = userService.login(request);
        // 能走到这里代表登录成功，给用户发放凭证
        String token = customJWTUtils.createToken(loginResponse.getId(), loginResponse.getMobile());
        loginResponse.setToken(token);
        return R.ok(loginResponse);
    }
}
