package com.singular.book.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.singular.book.entity.Book;
import com.singular.book.entity.UserApp;
import com.singular.book.entity.UserBook;
import com.singular.book.enums.GenreEnum;
import com.singular.book.mapper.BookMapper;
import com.singular.book.repository.AuthorRepository;
import com.singular.book.repository.BookRepository;
import com.singular.book.repository.GenreRepository;
import com.singular.book.repository.UserAppRepository;
import com.singular.book.repository.UserBookRepository;
import com.singular.book.vo.BookResponseVO;
import com.singular.book.vo.BookVO;
import com.singular.book.vo.UserBookResponseVO;

@Service
public class BookService {

	private final BookRepository bookRepository;

	private final UserBookRepository userBookRepository;

	private final UserAppRepository userAppRepository;

	private final AuthorRepository authorRepository;

	private final GenreRepository genreRepository;

	private final BookMapper bookMapper;

	public BookService(BookRepository bookRepository, UserBookRepository userBookRepository,
			UserAppRepository userAppRepository, AuthorRepository authorRepository, GenreRepository genreRepository,
			BookMapper bookMapper) {
		this.bookRepository = bookRepository;
		this.userBookRepository = userBookRepository;
		this.userAppRepository = userAppRepository;
		this.authorRepository = authorRepository;
		this.genreRepository = genreRepository;
		this.bookMapper = bookMapper;
	}

	@Transactional
	public BookResponseVO create(BookVO vo, Long userId) {
		
		UserApp user = findUserOrThrow(userId);
		
		validateDuplicatedBook(vo);


		List<Long> authorIds = vo.getAuthorIds() != null ? vo.getAuthorIds() : Collections.emptyList();		
		List<Long> listIdGenre = vo.getGenres() != null? vo.getGenres().stream().map(GenreEnum::getId).toList()  : Collections.emptyList();	
		
		Book book = bookMapper.mapToEntity(vo, user, authorRepository.findAllById(authorIds),  genreRepository.findAllById(listIdGenre));


		Book savedBook = bookRepository.save(book);

		UserBook userBook = new UserBook(user, savedBook, user, true, // REGISTERED_BY
				vo.getReadBy(), // READ_BY
				vo.getWrittenBy() // WRITTEN_BY
		);
		userBookRepository.save(userBook);

		BookResponseVO responseVO = bookMapper.mapToVO(book, userBook);
		return responseVO;
	}
	
	@Transactional
	public BookResponseVO update(Long bookId, BookVO vo, Long userId) {

		findBookOrThrow(bookId);
		UserApp user = findUserOrThrow(userId);
		validateBookUpdate(vo, bookId);

		List<Long> authorIds = vo.getAuthorIds() != null ? vo.getAuthorIds() : Collections.emptyList();
		List<Long> listIdGenre = vo.getGenres() != null ? vo.getGenres().stream().map(GenreEnum::getId).toList()
				: Collections.emptyList();

		UserBook userBook = updateOrCreateUserbookOnBookUpdate(vo, user, bookId);

		Book book = bookMapper.mapToEntity(vo, user, authorRepository.findAllById(authorIds),
				genreRepository.findAllById(listIdGenre));
		book.setId(bookId);

		Book updatedBook = bookRepository.save(book);

		BookResponseVO responseVO = bookMapper.mapToVO(updatedBook, userBook);

		return responseVO;
	}

	private UserBook updateOrCreateUserbookOnBookUpdate(BookVO vo, UserApp user, Long bookId) {
		Optional<UserBook> userBookOptional = userBookRepository.findByUserIdAndBookId(user.getId(), bookId);

		UserBook userBook;

		if (userBookOptional.isPresent()) {
			userBook = userBookOptional.get();
			userBook.setReadBy(vo.getReadBy());
			userBook.setWrittenBy(vo.getWrittenBy());
			userBook.setUpdatedAt(LocalDateTime.now());

		} else {
			userBook = new UserBook(user, new Book(bookId), user, false, vo.getReadBy(), vo.getWrittenBy());
		}

		userBook = userBookRepository.save(userBook);

		return userBook;
	}

	@Transactional
	public void  updateReadBy(Long bookId, Boolean readBy, Long userId) {
		UserBook userBook = getOrCreateUserBook(userId, bookId);

		userBook.setReadBy(readBy != null ? readBy : false);
		userBook.setUpdatedBy(userAppRepository.getReferenceById(userId));
		userBook.setUpdatedAt(LocalDateTime.now());

		userBookRepository.save(userBook);
		
	}

	@Transactional
	public void updateWrittenBy(Long bookId, Boolean writtenBy, Long userId) {
		UserBook userBook = getOrCreateUserBook(userId, bookId);

		userBook.setWrittenBy(writtenBy != null ? writtenBy : false);
		userBook.setUpdatedBy(userAppRepository.getReferenceById(userId));
		userBook.setUpdatedAt(LocalDateTime.now());

		userBookRepository.save(userBook);
		
	}

	@Transactional(readOnly = true)
	public BookResponseVO findById(Long id, Long userId) {
		Book book = findBookOrThrow(id);
		
		Optional<UserBook> userBookOpt = userBookRepository.findByUserIdAndBookId(userId, id);
		UserBook userBook = userBookOpt.isPresent() ?  userBookOpt.get() : null;

		BookResponseVO responseVO = bookMapper.mapToVO(book, userBook);
		

		if ( !userBookOpt.isPresent()) {		
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
	    Page<Book> books = bookRepository.findAll(pageable);

	    return books.map(book -> bookMapper.mapToVO(book, null));
	}

	@Transactional
	public void delete(Long id) {
		findBookOrThrow(id);
		bookRepository.deleteById(id);
	}

	private UserBook getOrCreateUserBook(Long userId, Long bookId) {
		return userBookRepository.findByUserIdAndBookId(userId, bookId).orElseGet(() -> {
			UserApp user = findUserOrThrow(userId);
			Book book = findBookOrThrow(bookId);

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
	
	// fazer métodos para buscar pelo titulo, autor via like, pelo titulo e autor também like e pelo isbn
	
	@Transactional(readOnly = true)
	public Page<BookResponseVO> findByTitle(String title, Pageable pageable) {
	    if (title == null || title.isBlank()) {
	        return findAll(pageable);
	    }
	    return bookRepository.findByTitleContainingIgnoreCase(title.trim(), pageable)
	            .map(book -> bookMapper.mapToVO(book, null));
	}

	@Transactional(readOnly = true)
	public Page<BookResponseVO> findByAuthorName(String authorName, Pageable pageable) {
	    if (authorName == null || authorName.isBlank()) {
	        return findAll(pageable);
	    }
	    return bookRepository.findByAuthorNameLike(authorName.trim(), pageable)
	            .map(book -> bookMapper.mapToVO(book, null));
	}

	@Transactional(readOnly = true)
	public Page<BookResponseVO> findByTitleAndAuthorName(String title, String authorName, Pageable pageable) {
	    if ((title == null || title.isBlank()) && (authorName == null || authorName.isBlank())) {
	        return findAll(pageable);
	    }
	    if (title == null || title.isBlank()) {
	        return findByAuthorName(authorName, pageable);
	    }
	    if (authorName == null || authorName.isBlank()) {
	        return findByTitle(title, pageable);
	    }

	    return bookRepository.findByTitleAndAuthorNameLike(title.trim(), authorName.trim(), pageable)
	            .map(book -> bookMapper.mapToVO(book, null));
	}

	@Transactional(readOnly = true)
	public BookResponseVO findByIsbn(String isbn) {
	  
	    Book book = returnAndvalidationISBN(isbn);

	    return bookMapper.mapToVO(book, null);
	}
	
	// Validações
	
	private Book returnAndvalidationISBN(String isbn) {
		
		  if (isbn == null || isbn.isBlank()) {
		        throw new IllegalArgumentException("O ISBN deve ser informado.");
		    }
		    Book book = bookRepository.findByIsbn(isbn.trim())
		            .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado para o ISBN: " + isbn));
		    
		    return book;
	}
	
	
	
	private UserApp findUserOrThrow(Long userId) {
        return userAppRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado id: " + userId));
    }
	
	private void validateDuplicatedBook(BookVO vo) {
	    checkIsbnAvailability(vo.getIsbn());
	    checkTitleAndAuthorUniqueness(vo.getTitle(), vo.getAuthorIds());
	}

	private void checkIsbnAvailability(String isbn) {
	    if (isbn != null && !isbn.isBlank() && bookRepository.existsByIsbn(isbn)) {
	        throw new IllegalArgumentException("Já existe um livro cadastrado com o ISBN informado.");
	    }
	}

	private void checkTitleAndAuthorUniqueness(String title, List<Long> authorIds) {
	    if (authorIds != null && !authorIds.isEmpty() && title != null) {
	        List<Book> existing = bookRepository.findByTitleAndAuthorIds(
	                title.trim(), 
	                authorIds, 
	                authorIds.size()
	        );
	        if (!existing.isEmpty()) {
	            throw new IllegalArgumentException("Já existe um livro cadastrado com esse mesmo título e autor(es).");
	        }
	    }
	}
	
	private Book findBookOrThrow(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado id: " + bookId));
    }
	
	private void validateBookUpdate(BookVO vo, Long bookId) {
        checkIsbnAvailabilityForUpdate(vo.getIsbn(), bookId);
    }

    private void checkIsbnAvailabilityForUpdate(String isbn, Long bookId) {
        if (isbn != null && !isbn.isBlank() && bookRepository.existsByIsbnAndIdNot(isbn, bookId)) {
            throw new IllegalArgumentException("O ISBN informado já pertence a outro livro.");
        }
    }
    
}