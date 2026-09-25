package com.singular.book.vo;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OpenLibraryAuthorSearchResponseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("numFound")
    private Long numFound;

    @JsonProperty("start")
    private Long start;

    @JsonProperty("numFoundExact")
    private Boolean numFoundExact;

    @JsonProperty("docs")
    private List<AuthorAPIVO> docs;

    public OpenLibraryAuthorSearchResponseVO() {
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

    public List<AuthorAPIVO> getDocs() {
        return docs;
    }

    public void setDocs(List<AuthorAPIVO> docs) {
        this.docs = docs;
    }
}