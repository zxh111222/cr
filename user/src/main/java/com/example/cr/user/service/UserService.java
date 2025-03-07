package com.example.cr.user.service;

import com.example.cr.user.entity.User;
import com.example.cr.user.entity.UserExample;
import com.example.cr.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public long count() {
        return userMapper.countByExample(null);
    }

    public int register(String mobile) {
        // 去数据库查询手机是否存在
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andMobileEqualTo(mobile);
        List<User> users = userMapper.selectByExample(userExample);
        boolean empty = users.isEmpty();
        if (!empty) {
            throw new RuntimeException("该手机号已注册！");
        }

        User user = new User();
        user.setId(System.currentTimeMillis());
        user.setMobile(mobile);
        return userMapper.insert(user);
    }

}
