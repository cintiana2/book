package com.singular.book.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class BookResponseVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String title;
	private String originalTitle;
	private String isbn;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String updatedByUserName;
	private List<String> authorNames;
	private List<String> genreNames;

	// Novo campo com as informações de relação do utilizador com o livro
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

	public List<String> getAuthorNames() {
		return authorNames;
	}

	public void setAuthorNames(List<String> authorNames) {
		this.authorNames = authorNames;
	}

	public List<String> getGenreNames() {
		return genreNames;
	}

	public void setGenreNames(List<String> genreNames) {
		this.genreNames = genreNames;
	}

	public UserBookResponseVO getUserBook() {
		return userBook;
	}

	public void setUserBook(UserBookResponseVO userBook) {
		this.userBook = userBook;
	}
}