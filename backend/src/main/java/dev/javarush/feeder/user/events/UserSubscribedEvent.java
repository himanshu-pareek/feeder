package dev.javarush.feeder.user.events;

import dev.javarush.feeder.feed.Feed;

/**
 * Event published when a user successfully subscribes to a feed.
 */
public record UserSubscribedEvent(String userId, Feed feed) {}
