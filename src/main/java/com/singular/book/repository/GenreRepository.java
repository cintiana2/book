package com.singular.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.singular.book.entity.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
}