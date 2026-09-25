package com.singular.book.entity;


import java.io.Serializable;

import com.singular.book.enums.UserStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "USER_STATUS")
public class UserStatus implements Serializable {


	private static final long serialVersionUID = -674862186289819519L;

	@Id
    @Column(name = "STATUS_ID")
    private Long id;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    public UserStatus() {
    }


    public UserStatus(UserStatusEnum enumCode) {
        if (enumCode != null) {
            this.id = enumCode.getId();
            this.name = enumCode.getDescription();
        }
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

    public UserStatusEnum getUserStatusEnum() {
        UserStatusEnum statusEnum = null;
        if (getId() != null) {
            statusEnum = UserStatusEnum.fromId(getId());
        }
        return statusEnum;
    }
}