package dev.javarush.feeder.user.web;

import dev.javarush.feeder.feed.Feed;
import dev.javarush.feeder.feed.FeedService;
import dev.javarush.feeder.user.User;
import dev.javarush.feeder.user.UserRepository;
import dev.javarush.feeder.user.UserService;
import java.net.URI;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserRestController {

    private final UserService userService;
    private final FeedService feedService;
    private final UserRepository userRepository;

    public UserRestController(UserService userService, FeedService feedService, UserRepository userRepository) {
        this.userService = userService;
        this.feedService = feedService;
        this.userRepository = userRepository;
    }

    @PostMapping("/{userId}/subscribe")
    public void subscribe(@PathVariable String userId, @RequestParam String feedUri) {
        User user = userService.getUser(userId);
        Feed feed = feedService.getOrCreateFeed(URI.create(feedUri));
        userService.subscribe(user, feed);
    }

    // Helper endpoint to create users for testing
    @PostMapping("/{userId}")
    public void createUser(@PathVariable String userId) {
        userRepository.save(new User(userId));
    }
}
