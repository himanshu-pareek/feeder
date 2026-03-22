package dev.javarush.feeder.user;

import com.google.common.eventbus.EventBus;
import dev.javarush.feeder.feed.Feed;
import dev.javarush.feeder.user.events.UserSubscribedEvent;
import java.util.Objects;

public class UserService {

    private final UserRepository userRepository;
    private final EventBus eventBus;

    public UserService(
        UserRepository userRepository,
        EventBus eventBus
    ) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.eventBus = Objects.requireNonNull(eventBus);
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
            eventBus.post(new UserSubscribedEvent(user.getId(), feed));
        }
    }

    public User getUser(String userId) {
      return userRepository.findById(userId)
          .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
    }
}
