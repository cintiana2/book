package com.singular.book.vo;

import java.io.Serializable;
import java.util.List;

import com.singular.book.enums.GenreEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BookVO implements Serializable {

	private static final long serialVersionUID = -4231463063241534796L;

	@NotBlank(message = "{book.validation.title.required}")
	@Size(max = 200, message = "{book.validation.title.size}")
	private String title;

	@Size(max = 200, message = "{book.validation.originalTitle.size}")
	private String originalTitle;

	@Size(max = 20, message = "{book.validation.isbn.size}")
	private String isbn;

	private List<Long> authorIds;
	private List<GenreEnum> genres;

	private Boolean readBy = false;
	private Boolean writtenBy = false;

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

	public List<Long> getAuthorIds() {
		return authorIds;
	}

	public void setAuthorIds(List<Long> authorIds) {
		this.authorIds = authorIds;
	}

	public List<GenreEnum> getGenres() {
		return genres;
	}

	public void setGenres(List<GenreEnum> genres) {
		this.genres = genres;
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
}