package com.singular.book.mapper;

import org.springframework.stereotype.Component;

import com.singular.book.entity.Author;
import com.singular.book.entity.UserApp;
import com.singular.book.vo.AuthorVO;

@Component
public class AuthorMapper {

    public Author mapToEntity(AuthorVO vo, UserApp updatedBy) {
        if (vo == null) {
            return null;
        }

        Author author = new Author();
        author.setId(vo.getId());
        author.setName(vo.getName() != null ? vo.getName().trim() : null);
        author.setBirthDate(vo.getBirthDate());
        author.setCountry(vo.getCountry());
        author.setUpdatedBy(updatedBy);

        return author;
    }

    public AuthorVO mapToVO(Author author) {
        if (author == null) {
            return null;
        }

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