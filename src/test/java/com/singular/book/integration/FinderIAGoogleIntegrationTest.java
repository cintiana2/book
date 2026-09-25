package com.singular.book.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.singular.book.vo.IABookResponseVO;

@SpringBootTest
class FinderIAGoogleIntegrationTest {

    @Test
    @DisplayName("Deve retornar os dados reais do livro batendo na API do Gemini de forma automatica")
    void shouldReturnRealBookDataFromGoogleGeminiApi() throws Exception {
        // Cenario (Given)
        String bookName = "O Iluminado";

        // Execucao (When)
        IABookResponseVO result = FinderIAGoogleIntegration.findBookDataInIA(bookName);

        // Validacao (Then)
        assertNotNull(result, "O objeto de retorno real nao deveria ser nulo.");
        
        assertNotNull(result.getOriginalTitle(), "O titulo original retornado nao deve ser nulo.");
        assertTrue(result.getOriginalTitle().equalsIgnoreCase("The Shining"), 
                "O titulo original retornado pela IA deveria ser 'The Shining'. Obtido: " + result.getOriginalTitle());
                
        assertNotNull(result.getOriginalLanguage(), "O idioma original retornado nao deve ser nulo.");
        assertTrue(result.getOriginalLanguage().toLowerCase().contains("ingl"), 
                "O idioma original deveria identificar a lingua inglesa. Obtido: " + result.getOriginalLanguage());
    }
}
