package com.web.usue_eer.payload.request;

import jakarta.validation.constraints.NotBlank;

public class SignInRequest {
    @NotBlank(message = "Логин не должен быть пустым")
    private String username;

    @NotBlank(message = "Пароль не должен быть пустым")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
