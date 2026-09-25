package com.singular.book.integration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.singular.book.exceptions.BusinessException;
import com.singular.book.vo.OpenLibraryAuthorSearchResponseVO;
import com.singular.book.vo.OpenLibrarySearchResponseVO;

@SpringBootTest
class OpenLibraryIntegrationTest {


    @Test
    @DisplayName("Deve buscar livro por termo genérico via chamada real à OpenLibrary")
    void testFindByAnyRealIntegration() throws BusinessException {
        // Executa a chamada real usando o método de instância injetado pelo Spring
        OpenLibrarySearchResponseVO response = OpenLibraryIntegration.findByAny("the lord of the rings");

        // Validações do contrato de resposta
        assertNotNull(response, "A resposta da API não deve ser nula");
        assertNotNull(response.getDocs(), "A lista 'docs' não deve ser nula");
        assertFalse(response.getDocs().isEmpty(), "A busca deve retornar ao menos um resultado");
        
        // Valida se os dados do primeiro livro foram mapeados nos VOs
        assertNotNull(response.getDocs().get(0).getTitle(), "O título do livro deve estar preenchido");
        
        // Busca url da capa do livro
        String urlCover = OpenLibraryIntegration.findFirstUrlCover(response);
        System.out.println("Url capa: " +urlCover);
        assertTrue(urlCover.toUpperCase().contains("HTTP"));
        
    }

    @Test
    @DisplayName("Deve buscar livro por título via chamada estática singleton")
    void testFindByTitleStaticIntegration() throws BusinessException {
        // Executa a chamada real utilizando o método estático singleton
        OpenLibrarySearchResponseVO response = OpenLibraryIntegration.findByTitle("Dom Casmurro");

        assertNotNull(response, "A resposta da API não deve ser nula");
        assertNotNull(response.getDocs(), "A lista de livros retornados não deve ser nula");
        assertTrue(response.getDocs().stream()
                .anyMatch(book -> book.getTitle() != null && book.getTitle().toLowerCase().contains("casmurro")),
                "Deveria conter 'Dom Casmurro' nos resultados");
    }

    @Test
    @DisplayName("Deve buscar autor via chamada real à OpenLibrary")
    void testFindByAuthorRealIntegration() throws BusinessException {
        OpenLibrarySearchResponseVO response = OpenLibraryIntegration.findByAuthor("Tolkien");

        assertNotNull(response);
        assertNotNull(response.getDocs());
        assertFalse(response.getDocs().isEmpty());
    }
    
    @Test
    @DisplayName("Deve buscar autores via chamada estática e validar o retorno de Mark Twain")
    void testFindAuthorsStaticIntegration() throws BusinessException {
        OpenLibraryAuthorSearchResponseVO response = OpenLibraryIntegration.findAuthors("twain");

        assertNotNull(response);
        assertNotNull(response.getDocs());

        boolean hasMarkTwain = response.getDocs().stream()
                .anyMatch(author -> "Mark Twain".equalsIgnoreCase(author.getName()));

        assertTrue(hasMarkTwain, "Deveria encontrar 'Mark Twain' na lista de autores");
    }
}