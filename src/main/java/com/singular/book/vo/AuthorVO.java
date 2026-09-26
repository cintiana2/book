package com.singular.book.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AuthorVO {

    private Long id;

    @NotBlank(message = "O nome do autor é obrigatório.")
    private String name;

    private LocalDate birthDate;

    private String country;

    @NotNull(message = "O ID do usuário responsável pela alteração é obrigatório.")
    private Long updatedById;

    private String updatedByName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public AuthorVO() {
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(Long updatedById) {
        this.updatedById = updatedById;
    }

    public String getUpdatedByName() {
        return updatedByName;
    }

    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}