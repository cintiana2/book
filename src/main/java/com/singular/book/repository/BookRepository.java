package com.singular.book.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.singular.book.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Verifica se já existe um livro cadastrado com o ISBN informado (usado na criação).
     */
    boolean existsByIsbn(String isbn);

    /**
     * Verifica se já existe outro livro cadastrado com o mesmo ISBN (usado na edição).
     */
    boolean existsByIsbnAndIdNot(String isbn, Long id);

    /**
     * Consulta para verificar a duplicidade de livro com o mesmo Título e a mesma lista exata de Autores.
     * 
     * Compara o título ignorando maiúsculas/minúsculas e valida se a quantidade de autores correspondentes
     * na lista é igual ao total de autores associados ao livro.
     */
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) = LOWER(:title) " +
           "AND (SELECT COUNT(a) FROM b.authors a WHERE a.id IN :authorIds) = :authorCount " +
           "AND (SELECT COUNT(a) FROM b.authors a) = :authorCount")
    List<Book> findByTitleAndAuthorIds(
            @Param("title") String title, 
            @Param("authorIds") List<Long> authorIds, 
            @Param("authorCount") long authorCount);
    
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    
    @Query("SELECT DISTINCT b FROM Book b JOIN b.authors a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :authorName, '%'))")
    Page<Book> findByAuthorNameLike(@Param("authorName") String authorName, Pageable pageable);

    
    @Query("SELECT DISTINCT b FROM Book b JOIN b.authors a WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')) "
    		+ "AND LOWER(a.name) LIKE LOWER(CONCAT('%', :authorName, '%'))")
    Page<Book> findByTitleAndAuthorNameLike(@Param("title") String title, @Param("authorName") String authorName, Pageable pageable);

   
    Optional<Book> findByIsbn(String isbn);
}