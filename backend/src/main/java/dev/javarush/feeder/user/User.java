package dev.javarush.feeder.user;

import dev.javarush.feeder.subscription.Subscription;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * User Aggregate Root.
 */
public class User {
    private final String id;
    private final Set<Subscription> subscriptions;

    public User(String id) {
        this(id, Collections.emptySet());
    }

    public User(String id, Collection<Subscription> subscriptions) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.subscriptions = new HashSet<>(subscriptions);
    }

    public String getId() {
        return id;
    }

    public Collection<Subscription> getSubscriptions() {
        return Set.copyOf(subscriptions);
    }

    /**
     * Subscribes the user to a feed by its URI.
     * Returns true if newly subscribed, false if already subscribed.
     */
    public boolean subscribeTo(URI feedUri) {
        Objects.requireNonNull(feedUri, "feedUri must not be null");
        if (isSubscribedTo(feedUri)) {
            return false;
        }
        return this.subscriptions.add(new Subscription(feedUri));
    }

    public boolean unsubscribeFrom(URI feedUri) {
        return this.subscriptions.removeIf(sub -> sub.feedUri().equals(feedUri));
    }

    public boolean isSubscribedTo(URI feedUri) {
        return this.subscriptions.stream()
            .anyMatch(sub -> sub.feedUri().equals(feedUri));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
