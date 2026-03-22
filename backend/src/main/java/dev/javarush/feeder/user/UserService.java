package dev.javarush.feeder.user;

import com.google.common.eventbus.EventBus;
import dev.javarush.feeder.feed.Feed;
import dev.javarush.feeder.feed.FeedService;
import dev.javarush.feeder.user.events.UserSubscribedEvent;
import java.net.URI;
import java.util.Objects;

public class UserService {

    private final UserRepository userRepository;
    private final FeedService feedService;
    private final EventBus eventBus;

    public UserService(UserRepository userRepository,
                       FeedService feedService,
                       EventBus eventBus) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.feedService = Objects.requireNonNull(feedService);
        this.eventBus = Objects.requireNonNull(eventBus);
    }

    /**
     * Subscribes a user to a feed and publishes UserSubscribedEvent.
     */
    public void subscribe(String userId, URI feedUri) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

        if (user.isSubscribedTo(feedUri)) {
            throw new AlreadySubscribedException("User is already subscribed to feed: " + feedUri);
        }

        Feed feed = feedService.getOrCreateFeed(feedUri);

        if (user.subscribeTo(feedUri)) {
            userRepository.save(user);
            eventBus.post(new UserSubscribedEvent(userId, feed));
        }
    }
}
