package com.singular.book.vo;

import jakarta.validation.constraints.NotBlank;

public class LoginVO {

    @NotBlank(message = "{user.validation.login.required}")
    private String login;

    @NotBlank(message = "{user.validation.password.required}")
    private String password;

    public LoginVO() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}