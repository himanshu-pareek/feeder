package dev.javarush.feeder.event;

import java.net.URI;

/**
 * Event published when a user successfully subscribes to a feed.
 */
public record FeedSubscribe(String userId, URI feedUri) {}
