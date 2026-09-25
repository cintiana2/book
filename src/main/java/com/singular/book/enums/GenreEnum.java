package com.singular.book.enums;

public enum GenreEnum {

    BRAZILIAN_LITERATURE(1L, "Literatura Brasileira"),
    ROMANCE(2L, "Romance"),
    DYSTOPIA(3L, "Distopia"),
    FANTASY(4L, "Fantasia"),
    SCIENCE_FICTION(5L, "Ficção Científica"),
    EROTIC(6L, "Erótico"),
    DRAMA(7L, "Drama"),
    TECHNICAL(8L, "Técnico");

    private final Long id;
    private final String description;

    GenreEnum(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

     public static GenreEnum fromId(Long id) {
        if (id == null) {
            return null;
        }
        for (GenreEnum genre : GenreEnum.values()) {
            if (genre.getId().equals(id)) {
                return genre;
            }
        }
        throw new IllegalArgumentException("Nenhum gênero encontrado com o ID: " + id);
    }
}