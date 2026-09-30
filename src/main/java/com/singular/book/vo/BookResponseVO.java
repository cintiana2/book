package com.singular.book.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.singular.book.enums.GenreEnum;

public class BookResponseVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String title;
	private String originalTitle;
	private String isbn;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String updatedByUserName;
	private List<AuthorVO> authors;
	private List<GenreEnum> genres;


	private UserBookResponseVO userBook;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOriginalTitle() {
		return originalTitle;
	}

	public void setOriginalTitle(String originalTitle) {
		this.originalTitle = originalTitle;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
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

	public String getUpdatedByUserName() {
		return updatedByUserName;
	}

	public void setUpdatedByUserName(String updatedByUserName) {
		this.updatedByUserName = updatedByUserName;
	}

	public List<AuthorVO> getAuthors() {
		return authors;
	}

	public void setAuthors(List<AuthorVO> authors) {
		this.authors = authors;
	}

	public List<GenreEnum> getGenres() {
		return genres;
	}

	public void setGenres(List<GenreEnum> genres) {
		this.genres = genres;
	}

	public UserBookResponseVO getUserBook() {
		return userBook;
	}

	public void setUserBook(UserBookResponseVO userBook) {
		this.userBook = userBook;
	}
}