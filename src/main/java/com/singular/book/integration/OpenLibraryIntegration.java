package com.singular.book.integration;

import java.net.URI;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.singular.book.exceptions.BusinessException;
import com.singular.book.vo.OpenLibraryAuthorSearchResponseVO;
import com.singular.book.vo.OpenLibrarySearchResponseVO;

import jakarta.annotation.PostConstruct;

@Component
@PropertySource("classpath:url.properties")
public class OpenLibraryIntegration {

	private static final Logger logger = LoggerFactory.getLogger(OpenLibraryIntegration.class);

	@Value("${url.integration.open.library.by.any}")
	private String urlByAny;

	@Value("${url.integration.open.library.by.title}")
	private String urlByTitle;

	@Value("${url.integration.open.library.by.author}")
	private String urlByAuthor;

	@Value("${url.integration.open.library.authors}")
	private String urlAuthors;

	private RestClient restClient;

	private static OpenLibraryIntegration staticInstance;

	public OpenLibraryIntegration() {
	}

	@PostConstruct
	public void init() {
		this.restClient = RestClient.create();
		staticInstance = this;
	}

	// Métodos Estáticos

	public static OpenLibrarySearchResponseVO findByAny(String query) throws BusinessException {
		checkInitialization();
		return staticInstance.searchByAny(query);
	}

	public static OpenLibrarySearchResponseVO findByTitle(String title) throws BusinessException {
		checkInitialization();
		return staticInstance.searchByTitle(title);
	}

	public static OpenLibrarySearchResponseVO findByAuthor(String author) throws BusinessException {
		checkInitialization();
		return staticInstance.searchByAuthor(author);
	}

	public static OpenLibraryAuthorSearchResponseVO findAuthors(String authorQuery) throws BusinessException {
		checkInitialization();
		return staticInstance.searchAuthors(authorQuery);
	}

	private static void checkInitialization() throws BusinessException {
		if (staticInstance == null) {
			throw new BusinessException("Componente de integracao OpenLibrary nao foi inicializado pelo container Spring.");
		}
	}

	// Métodos de Instância

	public OpenLibrarySearchResponseVO searchByAny(String query) throws BusinessException {
		String formattedUrl = MessageFormat.format(urlByAny, query);
		return executeGet(formattedUrl, OpenLibrarySearchResponseVO.class);
	}

	public OpenLibrarySearchResponseVO searchByTitle(String title) throws BusinessException {
		String formattedUrl = MessageFormat.format(urlByTitle, title);
		return executeGet(formattedUrl, OpenLibrarySearchResponseVO.class);
	}

	public OpenLibrarySearchResponseVO searchByAuthor(String author) throws BusinessException {
		String formattedUrl = MessageFormat.format(urlByAuthor, author);
		return executeGet(formattedUrl, OpenLibrarySearchResponseVO.class);
	}

	public OpenLibraryAuthorSearchResponseVO searchAuthors(String authorQuery) throws BusinessException {
		String formattedUrl = MessageFormat.format(urlAuthors, authorQuery);
		return executeGet(formattedUrl, OpenLibraryAuthorSearchResponseVO.class);
	}

	private <T> T executeGet(String urlString, Class<T> responseType) throws BusinessException {
		try {
			logger.info("Executando integração OpenLibrary URL: {}", urlString);

			URI uri = URI.create(urlString.replace(" ", "%20"));

			return restClient.get()
					.uri(uri)
					.retrieve()
					.body(responseType);

		} catch (Exception e) {
			logger.error("Erro na comunicação com a API da OpenLibrary. URL: {}", urlString, e);
			throw new BusinessException("Falha na chamada da Open Library API.");
		}
	}
}