package com.singular.book.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.singular.book.entity.Author;
import com.singular.book.entity.UserApp;
import com.singular.book.exceptions.BusinessException;
import com.singular.book.mapper.AuthorMapper;
import com.singular.book.repository.AuthorRepository;
import com.singular.book.repository.UserAppRepository;
import com.singular.book.vo.AuthorVO;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final UserAppRepository userAppRepository;
    private final AuthorMapper authorMapper;
    private final MessageSource messageSource;

    public AuthorService(AuthorRepository authorRepository,
                         UserAppRepository userAppRepository,
                         AuthorMapper authorMapper,
                         MessageSource messageSource) {
        this.authorRepository = authorRepository;
        this.userAppRepository = userAppRepository;
        this.authorMapper = authorMapper;
        this.messageSource = messageSource;
    }

    @Transactional
    public AuthorVO create(AuthorVO authorVo) {
        validateDuplicatedName(authorVo.getName());

        UserApp user = findUserOrThrow(authorVo.getUpdatedById());

        Author author = authorMapper.mapToEntity(authorVo, user);
        LocalDateTime now = LocalDateTime.now();
        author.setCreatedAt(now);
        author.setUpdatedAt(now);

        Author savedAuthor = authorRepository.save(author);
        return authorMapper.mapToVO(savedAuthor);
    }

    @Transactional
    public AuthorVO update(Long authorId, AuthorVO authorVo) {
        Author author = findAuthorOrThrow(authorId);
        validateNameUpdate(authorVo.getName(), author);

        UserApp user = findUserOrThrow(authorVo.getUpdatedById());

        author.setName(authorVo.getName() != null ? authorVo.getName().trim() : null);
        author.setBirthDate(authorVo.getBirthDate());
        author.setCountry(authorVo.getCountry());
        author.setUpdatedAt(LocalDateTime.now());
        author.setUpdatedBy(user);

        Author updatedAuthor = authorRepository.save(author);
        return authorMapper.mapToVO(updatedAuthor);
    }

    @Transactional(readOnly = true)
    public AuthorVO findById(Long authorId) {
        Author author = findAuthorOrThrow(authorId);
        return authorMapper.mapToVO(author);
    }

    @Transactional(readOnly = true)
    public Page<AuthorVO> findAll( @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) 
    Pageable pageable) {
        Page<Author> authors = authorRepository.findAll(pageable);
        return authors.map(authorMapper::mapToVO);
    }

    @Transactional(readOnly = true)
    public Page<AuthorVO> findByName(String name, Pageable pageable) {
        if (name == null || name.isBlank()) {
            return findAll(pageable);
        }
        return authorRepository.findByNameContainingIgnoreCase(name.trim(), pageable)
                .map(authorMapper::mapToVO);
    }
    

    @Transactional
    public void delete(Long authorId) {
        findAuthorOrThrow(authorId);

        if (authorRepository.existsAssociatedBook(authorId)) {
            throw new BusinessException(getMessage("author.validation.has-associated-books"));
        }

        authorRepository.deleteById(authorId);
    }
    
    @Transactional(readOnly = true)
    public List<AuthorVO> findAll() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream()
                .map(authorMapper::mapToVO)
                .collect(Collectors.toList());
    }

    // --- Métodos Auxiliares e Validações ---

    private String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    private Author findAuthorOrThrow(Long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new BusinessException(
                        getMessage("author.validation.author.not-found", authorId)));
    }

    private UserApp findUserOrThrow(Long userId) {
        if (userId == null) {
            throw new BusinessException(getMessage("author.validation.user.required"));
        }
        return userAppRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(
                        getMessage("author.validation.user.not-found", userId)));
    }

    private void validateDuplicatedName(String name) {
        if (name != null && !name.isBlank() && authorRepository.existsByNameIgnoreCase(name.trim())) {
            throw new BusinessException(getMessage("author.validation.name.exists"));
        }
    }

    private void validateNameUpdate(String newName, Author currentAuthor) {
        if (newName != null && !newName.isBlank()
                && !currentAuthor.getName().equalsIgnoreCase(newName.trim())
                && authorRepository.existsByNameIgnoreCase(newName.trim())) {
            throw new BusinessException(getMessage("author.validation.name.exists-other"));
        }
    }
}