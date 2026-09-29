package com.singular.book.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.singular.book.entity.Author;
import com.singular.book.entity.Book;
import com.singular.book.entity.Genre;
import com.singular.book.entity.UserApp;
import com.singular.book.entity.UserBook;
import com.singular.book.repository.AuthorRepository;
import com.singular.book.repository.BookRepository;
import com.singular.book.repository.GenreRepository;
import com.singular.book.repository.UserAppRepository;
import com.singular.book.repository.UserBookRepository;
import com.singular.book.vo.BookVO;
import com.singular.book.vo.BookResponseVO;
import com.singular.book.vo.UserBookResponseVO;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserBookRepository userBookRepository;

    @Autowired
    private UserAppRepository userAppRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Transactional
    public BookResponseVO create(BookVO vo, Long userId) {
        UserApp user = userAppRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado id: " + userId));

        if (vo.getIsbn() != null && !vo.getIsbn().isBlank() && bookRepository.existsByIsbn(vo.getIsbn())) {
            throw new IllegalArgumentException("Já existe um livro cadastrado com o ISBN informado.");
        }

        List<Long> authorIds = vo.getAuthorIds() != null ? vo.getAuthorIds() : Collections.emptyList();
        if (!authorIds.isEmpty()) {
            List<Book> existing = bookRepository.findByTitleAndAuthorIds(vo.getTitle().trim(), authorIds, authorIds.size());
            if (!existing.isEmpty()) {
                throw new IllegalArgumentException("Já existe um livro cadastrado com esse mesmo título e autor(es).");
            }
        }

        Book book = new Book();
        book.setTitle(vo.getTitle());
        book.setOriginalTitle(vo.getOriginalTitle());
        book.setIsbn(vo.getIsbn());
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());
        book.setUpdatedBy(user);

        if (!authorIds.isEmpty()) {
            List<Author> authors = authorRepository.findAllById(authorIds);
            book.setAuthors(authors);
        }

        if (vo.getGenreIds() != null && !vo.getGenreIds().isEmpty()) {
            List<Genre> genres = genreRepository.findAllById(vo.getGenreIds());
            book.setGenres(genres);
        }

        Book savedBook = bookRepository.save(book);

        UserBook userBook = new UserBook(
                user,
                savedBook,
                user,
                true,                 // REGISTERED_BY
                vo.getReadBy(),       // READ_BY
                vo.getWrittenBy()    // WRITTEN_BY
        );
        userBookRepository.save(userBook);

        BookResponseVO responseVO = mapToVO(savedBook);
        responseVO.setUserBook(mapToUserBookVO(userBook));
        return responseVO;
    }

    @Transactional
    public BookResponseVO update(Long bookId, BookVO vo, Long userId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado id: " + bookId));

        UserApp user = userAppRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado id: " + userId));

        if (vo.getIsbn() != null && !vo.getIsbn().isBlank() 
                && bookRepository.existsByIsbnAndIdNot(vo.getIsbn(), bookId)) {
            throw new IllegalArgumentException("O ISBN informado já pertence a outro livro.");
        }

        book.setTitle(vo.getTitle());
        book.setOriginalTitle(vo.getOriginalTitle());
        book.setIsbn(vo.getIsbn());
        book.setUpdatedAt(LocalDateTime.now());
        book.setUpdatedBy(user);

        if (vo.getAuthorIds() != null) {
            List<Author> authors = authorRepository.findAllById(vo.getAuthorIds());
            book.setAuthors(authors);
        }

        if (vo.getGenreIds() != null) {
            List<Genre> genres = genreRepository.findAllById(vo.getGenreIds());
            book.setGenres(genres);
        }

        Book updatedBook = bookRepository.save(book);
        BookResponseVO responseVO = mapToVO(updatedBook);

        userBookRepository.findByUserIdAndBookId(userId, bookId)
                .ifPresent(ub -> responseVO.setUserBook(mapToUserBookVO(ub)));

        return responseVO;
    }

    @Transactional
    public UserBookResponseVO updateReadBy(Long bookId, Boolean readBy, Long userId) {
        UserBook userBook = getOrCreateUserBook(userId, bookId);
        
        userBook.setReadBy(readBy != null ? readBy : false);
        userBook.setUpdatedBy(userAppRepository.getReferenceById(userId));
        userBook.setUpdatedAt(LocalDateTime.now());

        UserBook saved = userBookRepository.save(userBook);
        return mapToUserBookVO(saved);
    }

    @Transactional
    public UserBookResponseVO updateWrittenBy(Long bookId, Boolean writtenBy, Long userId) {
        UserBook userBook = getOrCreateUserBook(userId, bookId);
        
        userBook.setWrittenBy(writtenBy != null ? writtenBy : false);
        userBook.setUpdatedBy(userAppRepository.getReferenceById(userId));
        userBook.setUpdatedAt(LocalDateTime.now());

        UserBook saved = userBookRepository.save(userBook);
        return mapToUserBookVO(saved);
    }

    @Transactional(readOnly = true)
    public BookResponseVO findById(Long id, Long userId) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado id: " + id));

        BookResponseVO responseVO = mapToVO(book);

        Optional<UserBook> userBookOpt = userBookRepository.findByUserIdAndBookId(userId, id);

        if (userBookOpt.isPresent()) {
            responseVO.setUserBook(mapToUserBookVO(userBookOpt.get()));
        } else {
            UserBookResponseVO defaultUserBookVO = new UserBookResponseVO();
            defaultUserBookVO.setUserId(userId);
            defaultUserBookVO.setBookId(id);
            defaultUserBookVO.setRegisteredBy(false);
            defaultUserBookVO.setReadBy(false);
            defaultUserBookVO.setWrittenBy(false);
            responseVO.setUserBook(defaultUserBookVO);
        }

        return responseVO;
    }

    @Transactional(readOnly = true)
    public Page<BookResponseVO> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).map(this::mapToVO);
    }

    @Transactional
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new IllegalArgumentException("Livro não encontrado id: " + id);
        }
        bookRepository.deleteById(id);
    }

    private UserBook getOrCreateUserBook(Long userId, Long bookId) {
        return userBookRepository.findByUserIdAndBookId(userId, bookId)
                .orElseGet(() -> {
                    UserApp user = userAppRepository.findById(userId)
                            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado id: " + userId));
                    Book book = bookRepository.findById(bookId)
                            .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado id: " + bookId));

                    UserBook newUserBook = new UserBook();
                    newUserBook.setUser(user);
                    newUserBook.setBook(book);
                    newUserBook.setRegisteredBy(false);
                    newUserBook.setReadBy(false);
                    newUserBook.setWrittenBy(false);
                    newUserBook.setCreatedAt(LocalDateTime.now());
                    newUserBook.setUpdatedAt(LocalDateTime.now());
                    newUserBook.setUpdatedBy(user);
                    return newUserBook;
                });
    }

    private BookResponseVO mapToVO(Book book) {
        BookResponseVO vo = new BookResponseVO();
        vo.setId(book.getId());
        vo.setTitle(book.getTitle());
        vo.setOriginalTitle(book.getOriginalTitle());
        vo.setIsbn(book.getIsbn());
        vo.setCreatedAt(book.getCreatedAt());
        vo.setUpdatedAt(book.getUpdatedAt());

        if (book.getUpdatedBy() != null) {
            vo.setUpdatedByUserName(book.getUpdatedBy().getName());
        }

        if (book.getAuthors() != null) {
            vo.setAuthorNames(book.getAuthors().stream()
                    .map(Author::getName)
                    .collect(Collectors.toList()));
        }

        if (book.getGenres() != null) {
            vo.setGenreNames(book.getGenres().stream()
                    .map(Genre::getName)
                    .collect(Collectors.toList()));
        }

        return vo;
    }

    private UserBookResponseVO mapToUserBookVO(UserBook ub) {
        UserBookResponseVO vo = new UserBookResponseVO();
        vo.setId(ub.getId());
        vo.setUserId(ub.getUser().getId());
        vo.setBookId(ub.getBook().getId());
        vo.setRegisteredBy(ub.getRegisteredBy());
        vo.setReadBy(ub.getReadBy());
        vo.setWrittenBy(ub.getWrittenBy());
        vo.setUpdatedAt(ub.getUpdatedAt());
        return vo;
    }
}