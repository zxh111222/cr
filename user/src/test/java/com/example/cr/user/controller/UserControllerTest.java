package com.example.cr.user.controller;

import com.example.cr.common.util.SnowflakeUtil;
import com.example.cr.user.entity.User;
import com.example.cr.user.mapper.UserMapper;
import com.example.cr.user.request.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void before() {
        // 0. 删除数据库所有数据
        userMapper.deleteByExample(null);

    }

    @AfterEach
    void after() {
        userMapper.deleteByExample(null);

    }

    @Test
    public void countTest() throws Exception {
        // Arrange/Act/Assert 或者 Given/When/Then
        // 1. 做准备
        User user = new User();
        int number = 8;
        for (int i = 0; i < number; i++) {
            user.setId(SnowflakeUtil.getId());
            user.setMobile(i + "0123456");
            userMapper.insert(user);
        }
        // 2. 真正的发起请求
        // 3. 验证一定返回十条数据
        mockMvc.perform(get("/user/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(number));
    }

    @Test
    void register() throws Exception {
        // 1.准备
        String mobile = "13345678910";
        UserRequest userRequest = new UserRequest();
        userRequest.setMobile(mobile);
        String content = objectMapper.writeValueAsString(userRequest);
        // 2.操作
        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk());
        // 3.验证
        User user = userMapper.selectByExample(null).get(0);
        Assertions.assertEquals(mobile, user.getMobile());
    }

    @Test
    void register_shouldThrowExceptionWhenMobileAlreadyRegistered() throws Exception {
        // 1.准备
        String mobile = "13345678910";
        User user = new User();
        user.setId(SnowflakeUtil.getId());
        user.setMobile(mobile);
        userMapper.insert(user);

        String content = objectMapper.writeValueAsString(user);
        // 2.操作
        // 3.验证
        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("该手机号已注册！"));

    }
}
