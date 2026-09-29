package com.singular.book.vo;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BookVO implements Serializable {


	private static final long serialVersionUID = -4231463063241534796L;

	@NotBlank(message = "O título é obrigatório")
	@Size(max = 200, message = "O título deve ter no máximo 200 caracteres")
	private String title;

	@Size(max = 200, message = "O título original deve ter no máximo 200 caracteres")
	private String originalTitle;

	@Size(max = 20, message = "O ISBN deve ter no máximo 20 caracteres")
	private String isbn;

	private List<Long> authorIds;
	private List<Long> genreIds;

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

	public List<Long> getGenreIds() {
		return genreIds;
	}

	public void setGenreIds(List<Long> genreIds) {
		this.genreIds = genreIds;
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