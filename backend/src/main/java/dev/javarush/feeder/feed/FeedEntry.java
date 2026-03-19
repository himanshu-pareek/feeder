package dev.javarush.feeder.feed;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record FeedEntry(
    String uri,
    String title,
    String link,
    String description,
    LocalDateTime publishedDate,
    LocalDateTime updatedDate,
    List<Person> authors,
    List<String> categories,
    String comments,
    List<Content> contents,
    List<Person> contributors,
    List<Enclosure> enclosures
) {
    public FeedEntry {
        authors = authors != null ? List.copyOf(authors) : Collections.emptyList();
        categories = categories != null ? List.copyOf(categories) : Collections.emptyList();
        contents = contents != null ? List.copyOf(contents) : Collections.emptyList();
        contributors = contributors != null ? List.copyOf(contributors) : Collections.emptyList();
        enclosures = enclosures != null ? List.copyOf(enclosures) : Collections.emptyList();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String uri;
        private String title;
        private String link;
        private String description;
        private LocalDateTime publishedDate;
        private LocalDateTime updatedDate;
        private List<Person> authors = new ArrayList<>();
        private List<String> categories = new ArrayList<>();
        private String comments;
        private List<Content> contents = new ArrayList<>();
        private List<Person> contributors = new ArrayList<>();
        private List<Enclosure> enclosures = new ArrayList<>();

        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder link(String link) {
            this.link = link;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder publishedDate(LocalDateTime publishedDate) {
            this.publishedDate = publishedDate;
            return this;
        }

        public Builder updatedDate(LocalDateTime updatedDate) {
            this.updatedDate = updatedDate;
            return this;
        }

        public Builder authors(List<Person> authors) {
            this.authors = new ArrayList<>(authors);
            return this;
        }

        public Builder categories(List<String> categories) {
            this.categories = new ArrayList<>(categories);
            return this;
        }

        public Builder comments(String comments) {
            this.comments = comments;
            return this;
        }

        public Builder contents(List<Content> contents) {
            this.contents = new ArrayList<>(contents);
            return this;
        }

        public Builder contributors(List<Person> contributors) {
            this.contributors = new ArrayList<>(contributors);
            return this;
        }

        public Builder enclosures(List<Enclosure> enclosures) {
            this.enclosures = new ArrayList<>(enclosures);
            return this;
        }

        public FeedEntry build() {
            return new FeedEntry(
                uri, title, link, description, publishedDate, updatedDate,
                authors, categories, comments, contents, contributors, enclosures
            );
        }
    }
}
