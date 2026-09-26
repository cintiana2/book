package com.singular.book.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class UserAppVO implements Serializable {

  	private static final long serialVersionUID = 2257421265688421736L;

	private Long id;

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 100, message = "O nome não pode exceder 100 caracteres.")
    private String name;

    @NotBlank(message = "O login é obrigatório.")
    @Size(max = 50, message = "O login não pode exceder 50 caracteres.")
    private String login;

    // WRITE_ONLY garante que a senha seja recebida nas requisições, mas NUNCA serializada na resposta JSON
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 6, max = 20, message = "A senha deve ter entre 6 e 20 caracteres.")
    private String password;

    @NotNull(message = "O ID do status é obrigatório.")
    private Long statusId;

    private String statusDescription;

    public UserAppVO() {
    }

    public UserAppVO(Long id, String name, String login, Long statusId, String statusDescription) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.statusId = statusId;
        this.statusDescription = statusDescription;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }
}