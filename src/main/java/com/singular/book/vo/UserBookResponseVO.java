package com.singular.book.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UserBookResponseVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long userId;
	private Long bookId;
	private Boolean registeredBy;
	private Boolean readBy;
	private Boolean writtenBy;
	private LocalDateTime updatedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
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

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}