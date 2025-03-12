package com.example.cr.common.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "jwt-secret-key")
public class CustomJWTUtils {
    private static final Logger log = LoggerFactory.getLogger(CustomJWTUtils.class);

    @Value("${jwt-secret-key}")
    public String key;

    public String createToken(Long id, String mobile) {
        Map<String, Object> payload = new HashMap<>();

        // 业务内容
        payload.put("id", id);
        payload.put("mobile", mobile);

        DateTime now = DateTime.now();
        DateTime expTime = now.offsetNew(DateField.HOUR, 24);
        // 额外的几个时间值
        payload.put(JWTPayload.ISSUED_AT, now);
        payload.put(JWTPayload.EXPIRES_AT, expTime);
        payload.put(JWTPayload.NOT_BEFORE, now);

        String token = JWTUtil.createToken(payload, key.getBytes());
        log.info("生成 JWT token = {}, id = {}, mobile = {}", token, id, mobile);

        return token;
    }

    public boolean validate(String token) {
        JWT jwt = JWTUtil.parseToken(token).setKey(key.getBytes());
        boolean validate = jwt.validate(0);
        log.info("JWT token 校验结果 = {}", validate);
        return validate;
    }

    public JSONObject getJSONObject(String token) {
        JWT jwt = JWTUtil.parseToken(token).setKey(key.getBytes());
        JSONObject payloads = jwt.getPayloads();

        // 返回给前端之前，删除额外增加的几个时间值
        payloads.remove(JWTPayload.ISSUED_AT);
        payloads.remove(JWTPayload.EXPIRES_AT);
        payloads.remove(JWTPayload.NOT_BEFORE);

        log.info("从 JWT token 中获取的原始内容 = {}", payloads);
        return payloads;
    }
}
