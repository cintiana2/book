package com.singular.book.service;

import com.singular.book.entity.Author;
import com.singular.book.entity.UserApp;
import com.singular.book.repository.AuthorRepository;
import com.singular.book.repository.UserAppRepository;
import com.singular.book.vo.AuthorVO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final UserAppRepository userAppRepository;

    public AuthorService(AuthorRepository authorRepository, UserAppRepository userAppRepository) {
        this.authorRepository = authorRepository;
        this.userAppRepository = userAppRepository;
    }

    @Transactional
    public AuthorVO create(AuthorVO authorVo) {
        if (authorRepository.existsByNameIgnoreCase(authorVo.getName())) {
            throw new IllegalArgumentException("Já existe um autor cadastrado com este nome.");
        }

        UserApp user = userAppRepository.findById(authorVo.getUpdatedById())
                .orElseThrow(() -> new EntityNotFoundException("Usuário responsável não encontrado."));

        Author author = new Author();
        author.setName(authorVo.getName());
        author.setBirthDate(authorVo.getBirthDate());
        author.setCountry(authorVo.getCountry());
        
        // Atualização dos campos de auditoria
        LocalDateTime now = LocalDateTime.now();
        author.setCreatedAt(now);
        author.setUpdatedAt(now);
        author.setUpdatedBy(user);

        Author savedAuthor = authorRepository.save(author);
        return toVo(savedAuthor);
    }

    @Transactional
    public AuthorVO update(Long authorId, AuthorVO authorVo) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Autor não encontrado."));

        // Se o nome foi alterado, verifica se já existe outro autor com o mesmo nome
        if (!author.getName().equalsIgnoreCase(authorVo.getName()) && authorRepository.existsByNameIgnoreCase(authorVo.getName())) {
            throw new IllegalArgumentException("Já existe outro autor cadastrado com este nome.");
        }

        UserApp user = userAppRepository.findById(authorVo.getUpdatedById())
                .orElseThrow(() -> new EntityNotFoundException("Usuário responsável não encontrado."));

        author.setName(authorVo.getName());
        author.setBirthDate(authorVo.getBirthDate());
        author.setCountry(authorVo.getCountry());
        
        // Atualização dos campos de auditoria de alteração
        author.setUpdatedAt(LocalDateTime.now());
        author.setUpdatedBy(user);

        Author updatedAuthor = authorRepository.save(author);
        return toVo(updatedAuthor);
    }

    @Transactional(readOnly = true)
    public AuthorVO findById(Long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Autor não encontrado."));
        return toVo(author);
    }

    @Transactional(readOnly = true)
    public List<AuthorVO> findAll() {
        List<Author> authors = authorRepository.findAll();
        List<AuthorVO> voList = new ArrayList<>();

        for (int i = 0; i < authors.size(); i++) {
            voList.add(toVo(authors.get(i)));
        }

        return voList;
    }
    
    @Transactional
    public void delete(Long authorId) {
    	
        if (!authorRepository.existsById(authorId)) {
            throw new EntityNotFoundException("Autor não encontrado.");
        }

        if (authorRepository.existsAssociatedBook(authorId)) {
            throw new IllegalArgumentException("Não é possível excluir o autor pois ele possui livros vinculados.");
        }

        authorRepository.deleteById(authorId);
    }

    private AuthorVO toVo(Author author) {
        AuthorVO vo = new AuthorVO();
        vo.setId(author.getId());
        vo.setName(author.getName());
        vo.setBirthDate(author.getBirthDate());
        vo.setCountry(author.getCountry());
        vo.setCreatedAt(author.getCreatedAt());
        vo.setUpdatedAt(author.getUpdatedAt());

        if (author.getUpdatedBy() != null) {
            vo.setUpdatedById(author.getUpdatedBy().getId());
            vo.setUpdatedByName(author.getUpdatedBy().getName());
        }

        return vo;
    }
}