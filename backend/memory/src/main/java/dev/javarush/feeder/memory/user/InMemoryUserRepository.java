package dev.javarush.feeder.memory.user;

import dev.javarush.feeder.user.User;
import dev.javarush.feeder.user.UserRepository;
import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of the User Repository.
 */
public class InMemoryUserRepository implements UserRepository {
    private final Map<String, User> users = new ConcurrentHashMap<>();

    @Override
    public void save(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Collection<User> findAllSubscribedTo(URI feedUri) {
        return users.values().stream()
            .filter(user ->
                    user.getSubscriptions().stream()
                        .anyMatch(subscription ->
                            subscription.feedUri().equals(feedUri)
                        )
            )
            .collect(Collectors.toList());
    }
}
