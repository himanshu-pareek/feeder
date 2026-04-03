package dev.javarush.feeder.user;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

/**
 * Interface for User Repository.
 */
public interface UserRepository {
    void save(User user);
    Optional<User> findById(String id);

  Collection<User> findAllSubscribedTo(URI feedUri);
}
