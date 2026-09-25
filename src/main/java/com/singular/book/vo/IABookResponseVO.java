package com.singular.book.vo;

import java.io.Serializable;

public class IABookResponseVO implements Serializable {

	private static final long serialVersionUID = -7693381329152024428L;

	private String originalTitle;
	private String originalLanguage;

	public IABookResponseVO() {

	}

	public IABookResponseVO(String originalTitle, String originalLanguage) {
		super();
		this.originalTitle = originalTitle;
		this.originalLanguage = originalLanguage;
	}

	public String getOriginalTitle() {
		return originalTitle;
	}

	public void setOriginalTitle(String originalTitle) {
		this.originalTitle = originalTitle;
	}

	public String getOriginalLanguage() {
		return originalLanguage;
	}

	public void setOriginalLanguage(String originalLanguage) {
		this.originalLanguage = originalLanguage;
	}

}
