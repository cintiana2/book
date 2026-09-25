package com.singular.book.vo;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BookAPIVO implements Serializable {

	private static final long serialVersionUID = -3071974364684161138L;

	@JsonProperty("key")
    private String key;

    @JsonProperty("title")
    private String title;

    @JsonProperty("author_key")
    private List<String> authorKey;

    @JsonProperty("author_name")
    private List<String> authorName;

    @JsonProperty("first_publish_year")
    private Integer firstPublishYear;

    @JsonProperty("edition_count")
    private Integer editionCount;

    @JsonProperty("cover_edition_key")
    private String coverEditionKey;

    @JsonProperty("cover_i")
    private Long coverI;

    @JsonProperty("cover_height")
    private Integer coverHeight;

    @JsonProperty("cover_width")
    private Integer coverWidth;

    @JsonProperty("has_fulltext")
    private Boolean hasFulltext;

    @JsonProperty("public_scan_b")
    private Boolean publicScanB;

    @JsonProperty("ebook_access")
    private String ebookAccess;

    @JsonProperty("ia")
    private List<String> ia;

    @JsonProperty("ia_collection")
    private List<String> iaCollection;

    @JsonProperty("language")
    private List<String> language;

    @JsonProperty("lending_edition_s")
    private String lendingEditionS;

    @JsonProperty("lending_identifier_s")
    private String lendingIdentifierS;

    @JsonProperty("series_key")
    private List<String> seriesKey;

    @JsonProperty("series_name")
    private List<String> seriesName;

    @JsonProperty("series_position")
    private List<String> seriesPosition;

    public BookAPIVO() {
    }

    // Getters e Setters

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthorKey() {
        return authorKey;
    }

    public void setAuthorKey(List<String> authorKey) {
        this.authorKey = authorKey;
    }

    public List<String> getAuthorName() {
        return authorName;
    }

    public void setAuthorName(List<String> authorName) {
        this.authorName = authorName;
    }

    public Integer getFirstPublishYear() {
        return firstPublishYear;
    }

    public void setFirstPublishYear(Integer firstPublishYear) {
        this.firstPublishYear = firstPublishYear;
    }

    public Integer getEditionCount() {
        return editionCount;
    }

    public void setEditionCount(Integer editionCount) {
        this.editionCount = editionCount;
    }

    public String getCoverEditionKey() {
        return coverEditionKey;
    }

    public void setCoverEditionKey(String coverEditionKey) {
        this.coverEditionKey = coverEditionKey;
    }

    public Long getCoverI() {
        return coverI;
    }

    public void setCoverI(Long coverI) {
        this.coverI = coverI;
    }

    public Integer getCoverHeight() {
        return coverHeight;
    }

    public void setCoverHeight(Integer coverHeight) {
        this.coverHeight = coverHeight;
    }

    public Integer getCoverWidth() {
        return coverWidth;
    }

    public void setCoverWidth(Integer coverWidth) {
        this.coverWidth = coverWidth;
    }

    public Boolean getHasFulltext() {
        return hasFulltext;
    }

    public void setHasFulltext(Boolean hasFulltext) {
        this.hasFulltext = hasFulltext;
    }

    public Boolean getPublicScanB() {
        return publicScanB;
    }

    public void setPublicScanB(Boolean publicScanB) {
        this.publicScanB = publicScanB;
    }

    public String getEbookAccess() {
        return ebookAccess;
    }

    public void setEbookAccess(String ebookAccess) {
        this.ebookAccess = ebookAccess;
    }

    public List<String> getIa() {
        return ia;
    }

    public void setIa(List<String> ia) {
        this.ia = ia;
    }

    public List<String> getIaCollection() {
        return iaCollection;
    }

    public void setIaCollection(List<String> iaCollection) {
        this.iaCollection = iaCollection;
    }

    public List<String> getLanguage() {
        return language;
    }

    public void setLanguage(List<String> language) {
        this.language = language;
    }

    public String getLendingEditionS() {
        return lendingEditionS;
    }

    public void setLendingEditionS(String lendingEditionS) {
        this.lendingEditionS = lendingEditionS;
    }

    public String getLendingIdentifierS() {
        return lendingIdentifierS;
    }

    public void setLendingIdentifierS(String lendingIdentifierS) {
        this.lendingIdentifierS = lendingIdentifierS;
    }

    public List<String> getSeriesKey() {
        return seriesKey;
    }

    public void setSeriesKey(List<String> seriesKey) {
        this.seriesKey = seriesKey;
    }

    public List<String> getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(List<String> seriesName) {
        this.seriesName = seriesName;
    }

    public List<String> getSeriesPosition() {
        return seriesPosition;
    }

    public void setSeriesPosition(List<String> seriesPosition) {
        this.seriesPosition = seriesPosition;
    }
}