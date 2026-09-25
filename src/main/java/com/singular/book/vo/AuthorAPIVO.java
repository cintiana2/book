package com.singular.book.vo;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorAPIVO implements Serializable {

	private static final long serialVersionUID = -27003716609194743L;

	@JsonProperty("key")
    private String key;

    @JsonProperty("name")
    private String name;

    @JsonProperty("alternate_names")
    private List<String> alternateNames;

    @JsonProperty("birth_date")
    private String birthDate;

    @JsonProperty("death_date")
    private String deathDate;

    @JsonProperty("top_work")
    private String topWork;

    @JsonProperty("work_count")
    private Integer workCount;

    @JsonProperty("type")
    private String type;

    @JsonProperty("top_subjects")
    private List<String> topSubjects;

    @JsonProperty("ratings_average")
    private Double ratingsAverage;

    @JsonProperty("ratings_sortable")
    private Double ratingsSortable;

    @JsonProperty("ratings_count")
    private Integer ratingsCount;

    @JsonProperty("want_to_read_count")
    private Integer wantToReadCount;

    @JsonProperty("already_read_count")
    private Integer alreadyReadCount;

    @JsonProperty("currently_reading_count")
    private Integer currentlyReadingCount;

    @JsonProperty("stopped_reading_count")
    private Integer stoppedReadingCount;

    @JsonProperty("readinglog_count")
    private Integer readinglogCount;

    @JsonProperty("_root_")
    private String root;

    @JsonProperty("_version_")
    private Long version;

    public AuthorAPIVO() {
    }

    // Getters e Setters

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAlternateNames() {
        return alternateNames;
    }

    public void setAlternateNames(List<String> alternateNames) {
        this.alternateNames = alternateNames;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(String deathDate) {
        this.deathDate = deathDate;
    }

    public String getTopWork() {
        return topWork;
    }

    public void setTopWork(String topWork) {
        this.topWork = topWork;
    }

    public Integer getWorkCount() {
        return workCount;
    }

    public void setWorkCount(Integer workCount) {
        this.workCount = workCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getTopSubjects() {
        return topSubjects;
    }

    public void setTopSubjects(List<String> topSubjects) {
        this.topSubjects = topSubjects;
    }

    public Double getRatingsAverage() {
        return ratingsAverage;
    }

    public void setRatingsAverage(Double ratingsAverage) {
        this.ratingsAverage = ratingsAverage;
    }

    public Double getRatingsSortable() {
        return ratingsSortable;
    }

    public void setRatingsSortable(Double ratingsSortable) {
        this.ratingsSortable = ratingsSortable;
    }

    public Integer getRatingsCount() {
        return ratingsCount;
    }

    public void setRatingsCount(Integer ratingsCount) {
        this.ratingsCount = ratingsCount;
    }

    public Integer getWantToReadCount() {
        return wantToReadCount;
    }

    public void setWantToReadCount(Integer wantToReadCount) {
        this.wantToReadCount = wantToReadCount;
    }

    public Integer getAlreadyReadCount() {
        return alreadyReadCount;
    }

    public void setAlreadyReadCount(Integer alreadyReadCount) {
        this.alreadyReadCount = alreadyReadCount;
    }

    public Integer getCurrentlyReadingCount() {
        return currentlyReadingCount;
    }

    public void setCurrentlyReadingCount(Integer currentlyReadingCount) {
        this.currentlyReadingCount = currentlyReadingCount;
    }

    public Integer getStoppedReadingCount() {
        return stoppedReadingCount;
    }

    public void setStoppedReadingCount(Integer stoppedReadingCount) {
        this.stoppedReadingCount = stoppedReadingCount;
    }

    public Integer getReadinglogCount() {
        return readinglogCount;
    }

    public void setReadinglogCount(Integer readinglogCount) {
        this.readinglogCount = readinglogCount;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}