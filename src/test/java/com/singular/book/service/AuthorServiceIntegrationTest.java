package com.singular.book.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.singular.book.entity.Author;
import com.singular.book.entity.Book;
import com.singular.book.entity.UserApp;
import com.singular.book.entity.UserStatus;
import com.singular.book.repository.AuthorRepository;
import com.singular.book.repository.UserAppRepository;
import com.singular.book.repository.UserStatusRepository;
import com.singular.book.vo.AuthorVO;

import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthorServiceIntegrationTest {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private UserAppRepository userAppRepository;

    @Autowired
    private UserStatusRepository userStatusRepository;

    private UserApp defaultUser;

    @BeforeEach
    void setUp() {
        // Assegura um status de usuário ATIVO no banco de testes
        UserStatus status = userStatusRepository.findById(1L).orElseGet(() -> {
            UserStatus s = new UserStatus();
            s.setId(1L);
            s.setName("ATIVO");
            return userStatusRepository.save(s);
        });

        // Assegura a existência de um usuário responsável pelas alterações (updatedBy)
        defaultUser = userAppRepository.findByLogin("autor.admin").orElseGet(() -> {
            UserApp user = new UserApp();
            user.setName("Admin Autor");
            user.setLogin("autor.admin");
            user.setPassword("123456");
            user.setStatus(status);
            return userAppRepository.save(user);
        });
    }

    @Test
    @DisplayName("Deve salvar autor na base de dados e preencher datas de auditoria e updatedBy")
    void shouldCreateAuthorAndPersistInDatabase() {
        AuthorVO vo = new AuthorVO();
        vo.setName("Machado de Assis");
        vo.setBirthDate(LocalDate.of(1839, 6, 21));
        vo.setCountry("Brasil");
        vo.setUpdatedById(defaultUser.getId());

        AuthorVO createdVo = authorService.create(vo);

        assertNotNull(createdVo.getId());
        assertEquals("Machado de Assis", createdVo.getName());
        assertEquals("Brasil", createdVo.getCountry());
        assertNotNull(createdVo.getCreatedAt());
        assertNotNull(createdVo.getUpdatedAt());
        assertEquals(defaultUser.getId(), createdVo.getUpdatedById());

        // Validação direta no banco de dados via repositório
        Author persistedEntity = authorRepository.findById(createdVo.getId()).orElse(null);
        assertNotNull(persistedEntity);
        assertEquals("Machado de Assis", persistedEntity.getName());
        assertEquals(defaultUser.getId(), persistedEntity.getUpdatedBy().getId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cadastrar autor com nome duplicado")
    void shouldThrowExceptionWhenCreatingDuplicateAuthorName() {
        Author existingAuthor = new Author();
        existingAuthor.setName("Clarice Lispector");
        existingAuthor.setCountry("Brasil");
        existingAuthor.setUpdatedBy(defaultUser);
        authorRepository.save(existingAuthor);

        AuthorVO newVo = new AuthorVO();
        newVo.setName("clarice lispector"); // Validação Case-Insensitive
        newVo.setCountry("Brasil");
        newVo.setUpdatedById(defaultUser.getId());

        assertThrows(IllegalArgumentException.class, () -> authorService.create(newVo));
    }

    @Test
    @DisplayName("Deve atualizar dados do autor e alterar a data de atualização e o usuário responsável")
    void shouldUpdateAuthorInDatabase() {
        Author author = new Author();
        author.setName("Jorge Amado");
        author.setCountry("Brasil");
        author.setUpdatedBy(defaultUser);
        Author savedAuthor = authorRepository.save(author);

        AuthorVO updateVo = new AuthorVO();
        updateVo.setName("Jorge Amado Modificado");
        updateVo.setBirthDate(LocalDate.of(1912, 8, 10));
        updateVo.setCountry("Brasil");
        updateVo.setUpdatedById(defaultUser.getId());

        AuthorVO updatedResult = authorService.update(savedAuthor.getId(), updateVo);

        assertEquals("Jorge Amado Modificado", updatedResult.getName());

        Author databaseAuthor = authorRepository.findById(savedAuthor.getId()).orElseThrow();
        assertEquals("Jorge Amado Modificado", databaseAuthor.getName());
        assertNotNull(databaseAuthor.getUpdatedAt());
    }

    @Test
    @DisplayName("Deve buscar autor por ID cadastrado")
    void shouldFindAuthorById() {
        Author author = new Author();
        author.setName("Graciliano Ramos");
        author.setCountry("Brasil");
        author.setUpdatedBy(defaultUser);
        Author savedAuthor = authorRepository.save(author);

        AuthorVO foundVo = authorService.findById(savedAuthor.getId());

        assertNotNull(foundVo);
        assertEquals("Graciliano Ramos", foundVo.getName());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar autor por ID inexistente")
    void shouldThrowExceptionWhenAuthorNotFound() {
        assertThrows(EntityNotFoundException.class, () -> authorService.findById(999L));
    }

    @Test
    @DisplayName("Deve listar todos os autores salvos")
    void shouldFindAllAuthors() {
        Author author1 = new Author();
        author1.setName("Autor Um");
        author1.setUpdatedBy(defaultUser);

        Author author2 = new Author();
        author2.setName("Autor Dois");
        author2.setUpdatedBy(defaultUser);

        authorRepository.saveAll(List.of(author1, author2));

        List<AuthorVO> authorList = authorService.findAll();

        assertTrue(authorList.size() >= 2);
    }

    @Test
    @DisplayName("Deve excluir autor que não possui livros vinculados")
    void shouldDeleteAuthorWithoutBooks() {
        Author author = new Author();
        author.setName("Autor Sem Livros");
        author.setUpdatedBy(defaultUser);
        Author savedAuthor = authorRepository.save(author);

        authorService.delete(savedAuthor.getId());

        assertTrue(authorRepository.findById(savedAuthor.getId()).isEmpty());
    }

    @Test
    @DisplayName("Deve impedir exclusão de autor que possui livro vinculado")
    void shouldThrowExceptionWhenDeletingAuthorWithAssociatedBook() {
        Author author = new Author();
        author.setName("Autor Com Livro");
        author.setUpdatedBy(defaultUser);
        Author savedAuthor = authorRepository.save(author);

        // Cria e salva um livro associado a este autor
        Book book = new Book();
        book.setTitle("Livro do Autor");
        book.setLanguage("Português");
        book.setIsbn("978-0000000000");
        book.getAuthors().add(savedAuthor);
        //bookRepository.save(book);

        // Tenta excluir e verifica se lança a exceção esperada
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authorService.delete(savedAuthor.getId())
        );

        assertEquals("Não é possível excluir o autor pois ele possui livros vinculados.", exception.getMessage());
    }
}