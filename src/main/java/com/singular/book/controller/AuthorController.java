package com.singular.book.controller;

import com.singular.book.service.AuthorService;
import com.singular.book.vo.AuthorVO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<AuthorVO> create(@Valid @RequestBody AuthorVO authorVo) {
        AuthorVO response = authorService.create(authorVo);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorVO> update(@PathVariable("id") Long authorId,
                                           @Valid @RequestBody AuthorVO authorVo) {
        AuthorVO response = authorService.update(authorId, authorVo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorVO> findById(@PathVariable("id") Long authorId) {
        AuthorVO response = authorService.findById(authorId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AuthorVO>> findAll() {
        List<AuthorVO> response = authorService.findAll();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long authorId) {
        authorService.delete(authorId);
        return ResponseEntity.noContent().build();
    }
}