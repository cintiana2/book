package com.singular.book.vo;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangePasswordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "{user.validation.currentPassword.required}")
    private String currentPassword;

    @NotBlank(message = "{user.validation.newPassword.required}")
    @Size(min = 6, max = 20, message = "{user.validation.newPassword.size}")
    private String newPassword;

    public ChangePasswordVO() {
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}