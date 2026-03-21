package dev.javarush.feeder.content;

import dev.javarush.feeder.feed.Enclosure;
import dev.javarush.feeder.feed.FeedEntry;
import dev.javarush.feeder.feed.FeedEntryContent;
import dev.javarush.feeder.feed.Person;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Aggregate Root representing user-specific state and a copy of a feed entry.
 */
public class UserFeedEntry {
    private final String id;
    private final String userId;
    private final URI feedUri;
    private final URI feedEntryUri;
    private final String title;
    private final String link;
    private final String description;
    private final LocalDateTime publishedDate;
    private final LocalDateTime updatedDate;
    private final List<Person> authors;
    private final List<String> categories;
    private final String comments;
    private final List<FeedEntryContent> contents;
    private final List<Person> contributors;
    private final List<Enclosure> enclosures;
    private boolean read;

    public UserFeedEntry(String userId, URI feedUri, FeedEntry feedEntry) {
        this.id = UUID.randomUUID().toString();
        this.userId = Objects.requireNonNull(userId);
        this.feedUri = Objects.requireNonNull(feedUri);
        this.feedEntryUri = URI.create(feedEntry.uri());
        this.title = feedEntry.title();
        this.link = feedEntry.link();
        this.description = feedEntry.description();
        this.publishedDate = feedEntry.publishedDate();
        this.updatedDate = feedEntry.updatedDate();
        this.authors = List.copyOf(feedEntry.authors());
        this.categories = List.copyOf(feedEntry.categories());
        this.comments = feedEntry.comments();
        this.contents = List.copyOf(feedEntry.contents());
        this.contributors = List.copyOf(feedEntry.contributors());
        this.enclosures = List.copyOf(feedEntry.enclosures());
        this.read = false;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public URI getFeedUri() {
        return feedUri;
    }

    public URI getFeedEntryUri() {
        return feedEntryUri;
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

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public List<Person> getAuthors() {
        return authors;
    }

    public List<String> getCategories() {
        return categories;
    }

    public String getComments() {
        return comments;
    }

    public List<FeedEntryContent> getContents() {
        return contents;
    }

    public List<Person> getContributors() {
        return contributors;
    }

    public List<Enclosure> getEnclosures() {
        return enclosures;
    }

    public boolean isRead() {
        return read;
    }

    public void markAsRead() {
        this.read = true;
    }

    public void markAsUnread() {
        this.read = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFeedEntry that = (UserFeedEntry) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
