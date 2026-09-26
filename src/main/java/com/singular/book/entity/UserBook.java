package com.singular.book.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "USER_BOOK")
public class UserBook implements Serializable {



	private static final long serialVersionUID = -3836232998367106276L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user_book")
    @SequenceGenerator(name = "seq_user_book", sequenceName = "SEQ_USER_BOOK", allocationSize = 1)
    @Column(name = "USER_BOOK_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_APP_ID", nullable = false)
    private UserApp user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ID", nullable = false)
    private Book book;

    @Column(name = "REGISTERED_BY", nullable = false)
    private Boolean registeredBy = false;

    @Column(name = "READ_BY", nullable = false)
    private Boolean readBy = false;

    @Column(name = "WRITTEN_BY", nullable = false)
    private Boolean writtenBy = false;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;
    
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY")
	private UserApp updatedBy;

    public UserBook() {
    }

    public UserBook(UserApp user, Book book, UserApp updatedBy, Boolean registeredBy, 
    		Boolean readBy, Boolean writtenBy) {
        this.user = user;
        this.book = book;
        this.updatedBy = updatedBy;
        this.registeredBy = registeredBy != null ? registeredBy : false;
        this.readBy = readBy != null ? readBy : false;
        this.writtenBy = writtenBy != null ? writtenBy : false;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserApp getUser() {
        return user;
    }

    public void setUser(UserApp user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Boolean getRegisteredBy() {
        return registeredBy;
    }

    public void setRegisteredBy(Boolean registeredBy) {
        this.registeredBy = registeredBy;
    }

    public Boolean getReadBy() {
        return readBy;
    }

    public void setReadBy(Boolean readBy) {
        this.readBy = readBy;
    }

    public Boolean getWrittenBy() {
        return writtenBy;
    }

    public void setWrittenBy(Boolean writtenBy) {
        this.writtenBy = writtenBy;
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
    
    @Override
   	public boolean equals(Object o) {
   		if (this == o) return true;
   		if (o == null || getClass() != o.getClass()) return false;
   		UserBook userBook = (UserBook) o;
   		return id != null && Objects.equals(id, userBook.id);
   	}

   	@Override
   	public int hashCode() {
   		return getClass().hashCode();
   	}

  }