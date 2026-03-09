package dev.javarush.feeder.feed;

import java.time.LocalDateTime;
import java.util.List;

public class FeedEntry {

    private String uri;
    private String title;
    private String link;
    private String description;
    private LocalDateTime publishedDate;
    private LocalDateTime updatedDate;
    private List<Person> authors;
    private List<String> categories;
    private String comments;
    private List<Content> contents;
    private List<Person> contributors;
    private List<Enclosure> enclosures;

    public FeedEntry(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<Person> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Person> authors) {
        this.authors = authors;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    public List<Person> getContributors() {
        return contributors;
    }

    public void setContributors(List<Person> contributors) {
        this.contributors = contributors;
    }

    public List<Enclosure> getEnclosures() {
        return enclosures;
    }

    public void setEnclosures(List<Enclosure> enclosures) {
        this.enclosures = enclosures;
    }

    @Override
    public String toString() {
        return "FeedEntry{" +
            "uri='" + uri + '\'' +
            ", title='" + title + '\'' +
            ", link='" + link + '\'' +
            ", description='" + description + '\'' +
            ", publishedDate=" + publishedDate +
            ", updatedDate=" + updatedDate +
            ", authors=" + authors +
            ", categories=" + categories +
            ", comments='" + comments + '\'' +
            ", contents=" + contents +
            ", contributors=" + contributors +
            ", enclosures=" + enclosures +
            '}';
    }
}
