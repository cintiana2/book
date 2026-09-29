package com.singular.book.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.singular.book.service.BookService;
import com.singular.book.vo.BookVO;
import com.singular.book.vo.BookResponseVO;
import com.singular.book.vo.UserBookResponseVO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponseVO> create(
            @Valid @RequestBody BookVO vo,
            @RequestHeader("X-User-Id") Long userId) {
        
        BookResponseVO createdBook = bookService.create(vo, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseVO> update(
            @PathVariable Long id,
            @Valid @RequestBody BookVO vo,
            @RequestHeader("X-User-Id") Long userId) {

        BookResponseVO updatedBook = bookService.update(id, vo, userId);
        return ResponseEntity.ok(updatedBook);
    }

    // Altera o READ_BY recebendo o booleano via Query Param: PATCH /api/books/10/read-by?value=true
    @PatchMapping("/{id}/read-by")
    public ResponseEntity<UserBookResponseVO> updateReadBy(
            @PathVariable Long id,
            @RequestParam(name = "value") Boolean value,
            @RequestHeader("X-User-Id") Long userId) {

        UserBookResponseVO result = bookService.updateReadBy(id, value, userId);
        return ResponseEntity.ok(result);
    }

    // Altera o WRITTEN_BY recebendo o booleano via Query Param: PATCH /api/books/10/written-by?value=true
    @PatchMapping("/{id}/written-by")
    public ResponseEntity<UserBookResponseVO> updateWrittenBy(
            @PathVariable Long id,
            @RequestParam(name = "value") Boolean value,
            @RequestHeader("X-User-Id") Long userId) {

        UserBookResponseVO result = bookService.updateWrittenBy(id, value, userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseVO> findById(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        
        BookResponseVO book = bookService.findById(id, userId);
        return ResponseEntity.ok(book);
    }

    @GetMapping
    public ResponseEntity<Page<BookResponseVO>> findAll(Pageable pageable) {
        Page<BookResponseVO> books = bookService.findAll(pageable);
        return ResponseEntity.ok(books);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}