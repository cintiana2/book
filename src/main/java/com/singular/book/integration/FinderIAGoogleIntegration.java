package com.singular.book.integration;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.singular.book.exceptions.BusinessException;
import com.singular.book.vo.IABookResponseVO;

import jakarta.annotation.PostConstruct;

@Component
@PropertySource("classpath:ai.properties") 
public class FinderIAGoogleIntegration {

	private static final Logger logger = LoggerFactory.getLogger(FinderIAGoogleIntegration.class);

	private static final String DATA_TYPE = "application/json";
	private static final String INFORMATION_GEMINI = "gemini-3.8-flash";
	private static final Long TENTATIVE_NUMBERS = 10L;

	@Value("${gemini.api.key}")
	private String apiKey;

	@Value("${gemini.prompt.livro}")
	private String basePrompt;

	private Client client;
	private ObjectMapper objectMapper;

	private static FinderIAGoogleIntegration staticInstance;

	private GenerateContentResponse response;

	public FinderIAGoogleIntegration() {
	}

	@PostConstruct
	public void init() {
		this.objectMapper = new ObjectMapper();
		// Inicializa o cliente do Google utilizando a chave injetada automaticamente do ia.properties
		this.client = Client.builder().apiKey(apiKey).build();
		
		staticInstance = this;
	}

	public static IABookResponseVO findBookDataInIA(String bookName) throws BusinessException {
		if (staticInstance == null) {
			throw new BusinessException("Componente de integracao nao foi inicializado pelo container Spring.");
		}
		return staticInstance.callIA(bookName);
	}

	public IABookResponseVO callIA(String bookName) throws BusinessException {
		IABookResponseVO bookInfo = null;
		if (basePrompt == null || basePrompt.isBlank()) {
			logger.error("A propriedade do prompt nao foi encontrada ou esta vazia.");
			throw new BusinessException("Configuracao do prompt da IA nao encontrada.");
		}

		String finalPrompt = MessageFormat.format(basePrompt, bookName);
		GenerateContentConfig config = GenerateContentConfig.builder().responseMimeType(DATA_TYPE).build();

		
			logger.info("Iniciando busca real na IA do Google para o livro: '{}'", bookName);
			response = null;
			// tentar algumas vezes pois a app tem retornado erros
			for(int i = 0; i< TENTATIVE_NUMBERS ; ++i) {
				if(callAppAndLogError(response, i, finalPrompt, config)) {
					break;
				}
					
				
			}
			if (response != null && response.text() != null) {
			    try {
			        bookInfo = objectMapper.readValue(response.text(), IABookResponseVO.class);
			    } catch (JsonProcessingException e) { // Captura JsonMappingException e JsonParseException simultaneamente
			        logger.error("Erro no json de resposta.", e);
			        throw new BusinessException("Não foi encontrada a informação na IA.");
			    }
			}
			return bookInfo;
		
	}
	
	private boolean callAppAndLogError(GenerateContentResponse response, int tentativeNumber, String finalPrompt,
			GenerateContentConfig config) throws BusinessException {
		boolean sucess = false;

		try {
			response = client.models.generateContent(INFORMATION_GEMINI, finalPrompt, config);
			sucess = true;
		} catch (Exception e) {

			logger.error("Erro chamada api gemini, tentativa: " + tentativeNumber, e);
			if (tentativeNumber + 1 == TENTATIVE_NUMBERS) {
				throw new BusinessException("API do IA nao esta respondendo.");
			}

			try {
				// Aguarda 5000 milissegundos (5 segundos) antes do próximo laço
				Thread.sleep(5000);
			} catch (InterruptedException ie) {
				// Restaura o status de interrupção da thread
				Thread.currentThread().interrupt();
				throw new BusinessException("Execução interrompida durante a espera de tentativa.");
			}
		}
		return sucess;
	}
}
