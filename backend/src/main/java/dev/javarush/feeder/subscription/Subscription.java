package dev.javarush.feeder.subscription;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Value Object representing a subscription to a feed.
 */
public record Subscription(URI feedUri, LocalDateTime subscribedAt) {
    public Subscription {
        Objects.requireNonNull(feedUri, "feedUri must not be null");
        Objects.requireNonNull(subscribedAt, "subscribedAt must not be null");
    }

    public Subscription(URI feedUri) {
        this(feedUri, LocalDateTime.now());
    }
}
