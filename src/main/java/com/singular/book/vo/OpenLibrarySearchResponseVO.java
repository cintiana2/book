package com.singular.book.vo;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OpenLibrarySearchResponseVO implements Serializable {


	private static final long serialVersionUID = -2763701661767052732L;

	@JsonProperty("numFound")
    private Long numFound;

    @JsonProperty("start")
    private Long start;

    @JsonProperty("numFoundExact")
    private Boolean numFoundExact;

    @JsonProperty("num_found")
    private Long numFoundAlt;

    @JsonProperty("documentation_url")
    private String documentationUrl;

    @JsonProperty("q")
    private String query;

    @JsonProperty("offset")
    private Integer offset;

    @JsonProperty("docs")
    private List<BookAPIVO> docs;

    public OpenLibrarySearchResponseVO() {
    }

    // Getters e Setters

    public Long getNumFound() {
        return numFound;
    }

    public void setNumFound(Long numFound) {
        this.numFound = numFound;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Boolean getNumFoundExact() {
        return numFoundExact;
    }

    public void setNumFoundExact(Boolean numFoundExact) {
        this.numFoundExact = numFoundExact;
    }

    public Long getNumFoundAlt() {
        return numFoundAlt;
    }

    public void setNumFoundAlt(Long numFoundAlt) {
        this.numFoundAlt = numFoundAlt;
    }

    public String getDocumentationUrl() {
        return documentationUrl;
    }

    public void setDocumentationUrl(String documentationUrl) {
        this.documentationUrl = documentationUrl;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public List<BookAPIVO> getDocs() {
        return docs;
    }

    public void setDocs(List<BookAPIVO> docs) {
        this.docs = docs;
    }
}