package com.example.cr.user.controller;

import com.example.cr.user.entity.User;
import com.example.cr.user.entity.UserExample;
import com.example.cr.user.mapper.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserMapper userMapper;

    @Test
    public void countTest() throws Exception {
        // Arrange/Act/Assert 或者 Given/When/Then
        // 1. 做准备
        userMapper.deleteByExample(null);
        User user = new User();
        int number = 8;
        for (int i = 0; i < number; i++) {
            user.setId(System.currentTimeMillis() + i);
            user.setMobile(i + "0123456");
            userMapper.insert(user);
        }
        // 2. 真正的发起请求
        // 3. 验证一定返回十条数据
        mockMvc.perform(get("/user/count"))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(number)));
    }
}
