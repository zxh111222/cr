package com.example.cr.user.controller;

import com.example.cr.common.response.R;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("test")
    public String test() {
        return "hello user module!";
    }

    @GetMapping("test1")
    public String test1() {
        return "Test1";
    }

}
