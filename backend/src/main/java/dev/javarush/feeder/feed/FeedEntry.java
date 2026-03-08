package dev.javarush.feeder.feed;

import java.time.LocalDateTime;
import java.util.Collection;

public class FeedEntry {

    private String uri;
    private String title;
    private String link;
    private String description;
    private LocalDateTime publishedDate;
    private LocalDateTime updatedDate;
    private Collection<Person> authors;
    private Collection<String> categories;
    private String comments;
    private Collection<Content> contents;
    private Collection<Person> contributors;
    private Collection<Enclosure> enclosures;

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

    public Collection<Person> getAuthors() {
        return authors;
    }

    public void setAuthors(Collection<Person> authors) {
        this.authors = authors;
    }

    public Collection<String> getCategories() {
        return categories;
    }

    public void setCategories(Collection<String> categories) {
        this.categories = categories;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Collection<Content> getContents() {
        return contents;
    }

    public void setContents(Collection<Content> contents) {
        this.contents = contents;
    }

    public Collection<Person> getContributors() {
        return contributors;
    }

    public void setContributors(Collection<Person> contributors) {
        this.contributors = contributors;
    }

    public Collection<Enclosure> getEnclosures() {
        return enclosures;
    }

    public void setEnclosures(Collection<Enclosure> enclosures) {
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
