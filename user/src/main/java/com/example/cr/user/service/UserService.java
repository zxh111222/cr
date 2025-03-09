package com.example.cr.user.service;

import cn.hutool.core.util.RandomUtil;
import com.example.cr.common.util.SnowflakeUtil;
import com.example.cr.user.entity.User;
import com.example.cr.user.entity.UserExample;
import com.example.cr.common.exception.UserAlreadyExistsException;
import com.example.cr.user.mapper.UserMapper;
import com.example.cr.user.request.SendCodeRequest;
import com.example.cr.user.request.UserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

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

    public void sendCode(SendCodeRequest request) {
        // 从 request 获取输入手机号和验证码
        String mobile = request.getMobile();
        UserExample userExample = new UserExample();
        userExample.createCriteria().andMobileEqualTo(mobile);
        List<User> users = userMapper.selectByExample(userExample);
        if (users.isEmpty()) {
            // 如果为空，即这个手机号没有注册过
            User user = new User();
            user.setId(SnowflakeUtil.getId());
            user.setMobile(mobile);
            userMapper.insert(user);
            log.info("未注册过的手机号：{}，自动插入", mobile);
        }

        // 随机得到一串验证码
        String code = RandomUtil.randomString(4);
        log.info("生成验证码：{}，手机号：{}，业务场景：{}", code, mobile, "登录");

        // 验证码发送给目标手机
        // td-1：保存到验证码历史记录表
        // 建议的表字段 (id, code, mobile, 有效期, 已使用?, 业务类型[登录/忘记密码...], 生成时间, 使用时间)
        // 暂时通过打印日志模拟
        log.info("【模拟】已经保存到验证码历史记录表。验证码：{}，手机号：{}", code, mobile);

        // td-2：对接真实的短信发送服务
        log.info("【模拟】短信发送成功。验证码：{}，手机号：{}", code, mobile);

        // 检查验证码是否匹配
    }
}
