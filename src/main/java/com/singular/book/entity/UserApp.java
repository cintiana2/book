package com.singular.book.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "USER_APP")
public class UserApp implements Serializable{


	private static final long serialVersionUID = -3223415177818829997L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user")
    @SequenceGenerator(name = "seq_user", sequenceName = "SEQ_USER_APP", allocationSize = 1)
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "LOGIN", nullable = false, unique = true, length = 50)
    private String login;

    @Column(name = "PASSWORD", nullable = false, length = 255)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATUS_ID", nullable = false)
    private UserStatus status;

    public UserApp() {
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

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
    
    @Override
   	public boolean equals(Object o) {
   		if (this == o) return true;
   		if (o == null || getClass() != o.getClass()) return false;
   		UserApp userApp = (UserApp) o;
   		return id != null && Objects.equals(id, userApp.id);
   	}

   	@Override
   	public int hashCode() {
   		return getClass().hashCode();
   	}
    
}