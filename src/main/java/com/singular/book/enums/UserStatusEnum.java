package com.singular.book.enums;

import java.util.Arrays;

public enum UserStatusEnum {
	
    ACTIVE(1L, "Ativo"),
    INACTIVE(2L, "Inativo"),
    BLOCKED(3L, "Bloqueado");

    private final Long id;
    private final String description;

    UserStatusEnum(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public static UserStatusEnum fromId(Long id) {
        if (id == null) return null;
        return Arrays.stream(values())
                .filter(status -> status.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}