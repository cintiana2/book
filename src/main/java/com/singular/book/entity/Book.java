package com.singular.book.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/**
* Entidade principal representando um livro.
*/
@Entity
@Table(name = "BOOK")
public class Book {

	@Id
	@Column(name = "BOOK_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOK_SEQ_GEN")
	@SequenceGenerator(name = "BOOK_SEQ_GEN", sequenceName = "SEQ_TB_BOOK", allocationSize = 1)
	private Long id;

	@Column(name = "TITLE", nullable = false, length = 200)
	private String title;

   @Column(name = "LANGUAGE", nullable = false, length = 50)
   private String language;

   @Column(name = "ISBN", unique = true, length = 20)
   private String isbn;

   // Relacionamento muitos-para-muitos com Autores
   @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinTable(
       name = "BOOK_AUTHOR",
       joinColumns = @JoinColumn(name = "BOOK_ID"),
       inverseJoinColumns = @JoinColumn(name = "AUTHOR_ID")
   )
   private List<Author> authors = new ArrayList<>();

   // Relacionamento muitos-para-muitos com Gêneros
   @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinTable(
       name = "BOOK_GENRE",
       joinColumns = @JoinColumn(name = "BOOK_ID"),
       inverseJoinColumns = @JoinColumn(name = "GENRE_ID")
   )
   private List<Genre> genres = new ArrayList<>();

 
   public Book() {}

   public Book(Long id, String title, String language, String isbn) {
       this.id = id;
       this.title = title;
       this.language = language;
       this.isbn = isbn;
   }

   // Getters e Setters
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

   public String getLanguage() {
       return language;
   }

   public void setLanguage(String language) {
       this.language = language;
   }

   public String getIsbn() {
       return isbn;
   }

   public void setIsbn(String isbn) {
       this.isbn = isbn;
   }

   public List<Author> getAuthors() {
       return authors;
   }

   public void setAuthors(List<Author> authors) {
       this.authors = authors;
   }

   public List<Genre> getGenres() {
       return genres;
   }

   public void setGenres(List<Genre> genres) {
       this.genres = genres;
   }
}