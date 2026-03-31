package dev.javarush.feeder.user;

import dev.javarush.feeder.feed.Feed;
import dev.javarush.feeder.user.exception.AlreadySubscribedException;
import dev.javarush.feeder.user.exception.UserAlreadyExistException;
import dev.javarush.feeder.user.exception.UserNotFoundException;
import java.util.Objects;
import java.util.Optional;

public class UserService {

  private final UserRepository userRepository;

  public UserService(
      UserRepository userRepository
  ) {
    this.userRepository = Objects.requireNonNull(userRepository);
  }

  /**
   * Subscribes a user to a feed and publishes UserSubscribedEvent.
   */
  public void subscribe(User user, Feed feed) {
    if (user.isSubscribedTo(feed)) {
      throw new AlreadySubscribedException("User is already subscribed to feed: " + feed.getUri());
    }
    if (user.subscribeTo(feed)) {
      userRepository.save(user);
    }
  }

  public User getUser(String userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
  }

  public void createUser(User user) {
    Optional<User> existingUser = this.userRepository.findById(user.getId());
    if (existingUser.isPresent()) {
      throw new UserAlreadyExistException("User already exist: " + user.getId());
    }
    this.userRepository.save(user);
  }
}
