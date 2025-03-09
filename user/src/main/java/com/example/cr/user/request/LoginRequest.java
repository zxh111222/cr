package com.example.cr.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LoginRequest {

    @NotBlank
    @Size(min = 11, max = 11, message = "手机号必须是11位")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    String mobile;

    @Size(min = 4, max = 4, message = "验证码不符合规范")
    String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
