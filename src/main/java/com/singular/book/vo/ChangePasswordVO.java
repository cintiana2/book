package com.singular.book.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class ChangePasswordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "A senha atual é obrigatória.")
    private String currentPassword;

    @NotBlank(message = "A nova senha é obrigatória.")
    @Size(min = 6, max = 20, message = "A nova senha deve ter entre 6 e 20 caracteres.")
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