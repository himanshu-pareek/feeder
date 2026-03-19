package dev.javarush.feeder.feed;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

public class Feed {

    private URI uri;
    private final String title;
    private final String link;
    private final String description;
    private LocalDateTime publishedDate;
    private List<String> authors;
    private String copyright;
    private List<FeedEntry> entries;
    private String language;
    private String feedType;
    private String managingEditor;
    private String webMaster;


    public Feed(URI uri, String title, String link, String description) {
        this.uri = uri;
        this.title = title;
        this.link = link;
        this.description = description;
    }

    public Feed(Feed other) {
        this.uri = other.uri;
        this.title = other.title;
        this.link = other.link;
        this.description = other.description;
        this.publishedDate = other.publishedDate;
        this.authors = other.authors != null ? List.copyOf(other.authors) : null;
        this.copyright = other.copyright;
        this.entries = other.entries != null ? List.copyOf(other.entries) : null;
        this.language = other.language;
        this.feedType = other.feedType;
        this.managingEditor = other.managingEditor;
        this.webMaster = other.webMaster;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public List<FeedEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<FeedEntry> entries) {
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
