package com.example.cr.user.service;

import com.example.cr.common.util.SnowflakeUtil;
import com.example.cr.user.entity.User;
import com.example.cr.user.entity.UserExample;
import com.example.cr.common.exception.UserAlreadyExistsException;
import com.example.cr.user.mapper.UserMapper;
import com.example.cr.user.request.UserRequest;
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

    public int register(UserRequest request) {
        String mobile = request.getMobile();
        // 去数据库查询手机是否存在
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andMobileEqualTo(mobile);
        List<User> users = userMapper.selectByExample(userExample);
        boolean empty = users.isEmpty();
        if (!empty) {
            throw new UserAlreadyExistsException("该手机号已注册！");
        }

        User user = new User();
        user.setId(SnowflakeUtil.getId());
        user.setMobile(mobile);
        return userMapper.insert(user);
    }

}
