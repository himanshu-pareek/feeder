package dev.javarush.feeder.feed;

import java.time.LocalDateTime;
import java.util.Collection;

public class Feed {

    private String uri;
    private String title;
    private String link;
    private String description;
    private LocalDateTime publishedDate;
    private Collection<String> authors;
    private String copyright;
    private Collection<FeedEntry> entries;
    private String language;
    private String feedType;
    private String managingEditor;
    private String webMaster;


    public Feed(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
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

    public Collection<String> getAuthors() {
        return authors;
    }

    public void setAuthors(Collection<String> authors) {
        this.authors = authors;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public Collection<FeedEntry> getEntries() {
        return entries;
    }

    public void setEntries(Collection<FeedEntry> entries) {
        this.entries = entries;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    public String getManagingEditor() {
        return managingEditor;
    }

    public void setManagingEditor(String managingEditor) {
        this.managingEditor = managingEditor;
    }

    public String getWebMaster() {
        return webMaster;
    }

    public void setWebMaster(String webMaster) {
        this.webMaster = webMaster;
    }

    @Override
    public String toString() {
        return "Feed{" +
            "uri='" + uri + '\'' +
            ", title='" + title + '\'' +
            ", link='" + link + '\'' +
            ", description='" + description + '\'' +
            ", publishedDate=" + publishedDate +
            ", authors=" + authors +
            ", copyright='" + copyright + '\'' +
            ", entries=" + entries +
            ", language='" + language + '\'' +
            ", feedType='" + feedType + '\'' +
            ", managingEditor='" + managingEditor + '\'' +
            ", webMaster='" + webMaster + '\'' +
            '}';
    }
}
