package com.example.cr.user.request;

import jakarta.validation.constraints.NotBlank;

public class UserRequest {

    @NotBlank
    String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
