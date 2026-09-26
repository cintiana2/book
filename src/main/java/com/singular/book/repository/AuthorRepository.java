package com.singular.book.repository;

import com.singular.book.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    boolean existsByNameIgnoreCase(String name);

    Optional<Author> findByNameIgnoreCase(String name);

        @Query("SELECT COUNT(b) > 0 FROM Book b JOIN b.authors a WHERE a.id = :authorId")
    boolean existsAssociatedBook(@Param("authorId") Long authorId);
}