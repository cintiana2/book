package com.singular.book.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

//Representa a tabela de autores 
@Entity 
@Table(name="AUTHOR")
public class Author {
	@Id
	@Column(name = "AUTHOR_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTHOR_SEQ_GEN")
	@SequenceGenerator(name = "AUTHOR_SEQ_GEN", sequenceName = "SEQ_TB_BOOK", allocationSize = 1)
	private Long id;

	@Column(name = "NAME", nullable = false, length = 150)
	private String name;

	@Column(name = "BIRTH_DATE")
	private LocalDate birthDate;

	@Column(name = "COUNTRY", length = 100)
	private String country;

	public Author() {
	}

	public Author(Long id, String name, LocalDate birthDate, String country) {
		this.id = id;
		this.name = name;
		this.birthDate = birthDate;
		this.country = country;
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

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}