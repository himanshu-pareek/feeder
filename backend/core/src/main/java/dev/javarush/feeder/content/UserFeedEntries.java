package dev.javarush.feeder.content;

import java.util.Collection;

public record UserFeedEntries (int page, int size, Collection<UserFeedEntry> items) {
}
