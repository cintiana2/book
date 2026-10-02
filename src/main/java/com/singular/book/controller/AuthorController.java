package com.singular.book.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.singular.book.service.AuthorService;
import com.singular.book.vo.AuthorVO;

import jakarta.validation.Valid;

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
    public ResponseEntity<Page<AuthorVO>> findAll(
            @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        
        Page<AuthorVO> response = authorService.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-name")
    public ResponseEntity<Page<AuthorVO>> findByName(
            @RequestParam(required = false) String name,
            @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        
        Page<AuthorVO> authors = authorService.findByName(name, pageable);
        return ResponseEntity.ok(authors);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long authorId) {
        authorService.delete(authorId);
        return ResponseEntity.noContent().build();
    }
}