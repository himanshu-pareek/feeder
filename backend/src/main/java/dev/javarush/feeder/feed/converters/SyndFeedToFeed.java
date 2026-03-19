package dev.javarush.feeder.feed.converters;

import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndPerson;
import dev.javarush.feeder.feed.Content;
import dev.javarush.feeder.feed.Enclosure;
import dev.javarush.feeder.feed.Feed;
import dev.javarush.feeder.feed.FeedEntry;
import dev.javarush.feeder.feed.Person;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SyndFeedToFeed {
    public static Feed convert(SyndFeed source) {
        Feed feed = new Feed(source.getUri() != null ? URI.create(source.getUri()) : null, source.getTitle(), source.getLink(), source.getDescription());
        feed.setPublishedDate(toUTCDate(source.getPublishedDate()));
        if (source.getAuthors() != null) {
            feed.setAuthors(source.getAuthors().stream()
                .map(SyndPerson::getName)
                .collect(Collectors.toList()));
        }
        feed.setCopyright(source.getCopyright());
        feed.setLanguage(source.getLanguage());
        feed.setFeedType(source.getFeedType());
        feed.setManagingEditor(source.getManagingEditor());
        feed.setWebMaster(source.getWebMaster());
        if (source.getEntries() != null) {
            feed.setEntries(source.getEntries().stream()
                .map(SyndEntryToFeedEntry::convert)
                .collect(Collectors.toList()));
        }
        return feed;
    }

    private static LocalDateTime toUTCDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime();
    }

    private static class SyndEntryToFeedEntry {
        static FeedEntry convert(SyndEntry syndEntry) {
            FeedEntry.Builder builder = FeedEntry.builder()
                .title(syndEntry.getTitle())
                .link(syndEntry.getLink())
                .uri(syndEntry.getUri());

            if (syndEntry.getDescription() != null) {
                builder = builder.description(syndEntry.getDescription().getValue());
            }

            builder = builder.publishedDate(toUTCDate(syndEntry.getPublishedDate()))
                .updatedDate(toUTCDate(syndEntry.getUpdatedDate()))
                .authors(convertPersons(syndEntry.getAuthors()))
                .contributors(convertPersons(syndEntry.getContributors()))
                .contents(convertContents(syndEntry.getContents()))
                .enclosures(convertEnclosures(syndEntry.getEnclosures()));

            if (syndEntry.getCategories() != null) {
                builder = builder.categories(syndEntry.getCategories().stream()
                    .map(SyndCategory::getName)
                    .collect(Collectors.toList()));
            }

            builder = builder.comments(syndEntry.getComments());

            return builder.build();
        }

        private static List<Content> convertContents(List<SyndContent> syndContents) {
            if (syndContents == null) {
                return Collections.emptyList();
            }
            return syndContents.stream()
                .map(c -> new Content(c.getType(), c.getValue()))
                .collect(Collectors.toList());
        }

        private static List<Enclosure> convertEnclosures(List<SyndEnclosure> syndEnclosures) {
            if (syndEnclosures == null) {
                return Collections.emptyList();
            }
            return syndEnclosures.stream()
                .map(e -> new Enclosure(e.getUrl(), e.getType(), e.getLength()))
                .collect(Collectors.toList());
        }

        private static List<Person> convertPersons(List<SyndPerson> syndPersons) {
            if (syndPersons == null) {
                return Collections.emptyList();
            }
            return syndPersons.stream()
                .map(p -> new Person(p.getName(), p.getEmail(), p.getUri()))
                .collect(Collectors.toList());
        }
    }
}
