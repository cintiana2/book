package com.singular.book.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.singular.book.entity.Author;
import com.singular.book.entity.Book;
import com.singular.book.entity.Genre;
import com.singular.book.entity.UserApp;
import com.singular.book.entity.UserBook;
import com.singular.book.enums.GenreEnum;
import com.singular.book.vo.AuthorVO;
import com.singular.book.vo.BookResponseVO;
import com.singular.book.vo.BookVO;
import com.singular.book.vo.UserBookResponseVO;

@Component
public class BookMapper {

	public BookResponseVO mapToVO(Book book, UserBook userBook) {
		if (book == null) {
			return null;
		}

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

		if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
			vo.setAuthors(new ArrayList<AuthorVO>());
			for (Author author : book.getAuthors()) {
				AuthorVO authorVo = new AuthorVO();
				authorVo.setId(author.getId());
				// ... mapeamento de Author
				vo.getAuthors().add(authorVo);
			}
		}

		if (book.getGenres() != null && !book.getGenres().isEmpty()) {
			vo.setGenres(new ArrayList<GenreEnum>());
			for (Genre genre : book.getGenres()) {
				GenreEnum genreEnum = GenreEnum.fromId(genre.getId());
				vo.getGenres().add(genreEnum);
			}
		}
        if(userBook != null) {
        	vo.setUserBook(mapToUserBookVO(userBook));
        }
		return vo;
	}
	
	public UserBookResponseVO mapToUserBookVO(UserBook ub) {
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



	public Book mapToEntity(BookVO vo, UserApp user, List<Author> authors, List<Genre> genres) {
		if (vo == null) {
			return null;
		}

		Book book = new Book();
		book.setTitle(vo.getTitle());
		book.setOriginalTitle(vo.getOriginalTitle());
		book.setIsbn(vo.getIsbn());
		book.setCreatedAt(LocalDateTime.now());
		book.setUpdatedAt(LocalDateTime.now());
		book.setUpdatedBy(user);

		if (authors != null && !authors.isEmpty()) {
			book.setAuthors(authors);
		}

		if (genres != null && !genres.isEmpty()) {
			book.setGenres(genres);
		}

		return book;
	}

}