package dev.javarush.feeder.user.events;

import dev.javarush.feeder.feed.Feed;
import java.net.URI;

/**
 * Event published when a user successfully subscribes to a feed.
 */
public record UserSubscribedEvent(String userId, URI feedUri) {}
