package com.singular.book.entity;

import com.singular.book.enums.GenreEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "GENRE")
public class Genre {

    @Id
    @Column(name = "GENRE_ID")
    private Long id;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    

    public Genre() {
    }

    // Construtor auxiliar a partir do Enum
    public Genre(GenreEnum enumCode) {
        if (enumCode != null) {
            this.id = enumCode.getId();
            this.name = enumCode.getDescription();
        }
    }

    // Getters e Setters
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

	public GenreEnum getGenreEnum() {
		
		GenreEnum genreEnum = null;
		if(getId() != null) {
			genreEnum = GenreEnum.fromId(getId());
		}
		return genreEnum;
	}

    
}