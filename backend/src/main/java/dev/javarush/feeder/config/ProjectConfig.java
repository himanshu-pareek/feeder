package dev.javarush.feeder.config;

import com.google.common.eventbus.EventBus;
import dev.javarush.feeder.content.InMemoryUserFeedEntryRepository;
import dev.javarush.feeder.content.UserFeedEntryRepository;
import dev.javarush.feeder.content.UserFeedEntryService;
import dev.javarush.feeder.content.UserSubscribedListener;
import dev.javarush.feeder.feed.FeedFetcher;
import dev.javarush.feeder.feed.FeedRepository;
import dev.javarush.feeder.feed.FeedService;
import dev.javarush.feeder.feed.InMemoryFeedRepository;
import dev.javarush.feeder.feed.RomeFeedFetcher;
import dev.javarush.feeder.user.InMemoryUserRepository;
import dev.javarush.feeder.user.UserRepository;
import dev.javarush.feeder.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

    @Bean
    public EventBus eventBus() {
        return new EventBus();
    }

    @Bean
    public UserRepository userRepository() {
        return new InMemoryUserRepository();
    }

    @Bean
    public FeedRepository feedRepository() {
        return new InMemoryFeedRepository();
    }

    @Bean
    public UserFeedEntryRepository userFeedEntryRepository() {
        return new InMemoryUserFeedEntryRepository();
    }

    @Bean
    public FeedFetcher feedFetcher() {
        return new RomeFeedFetcher();
    }

    @Bean
    public FeedService feedService(FeedRepository feedRepository, FeedFetcher feedFetcher) {
        return new FeedService(feedRepository, feedFetcher);
    }

    @Bean
    public UserFeedEntryService userFeedEntryService(UserFeedEntryRepository userFeedEntryRepository) {
        return new UserFeedEntryService(userFeedEntryRepository);
    }

    @Bean
    public UserService userService(UserRepository userRepository, EventBus eventBus) {
        return new UserService(userRepository, eventBus);
    }

    @Bean
    public UserSubscribedListener userSubscribedListener(EventBus eventBus, UserFeedEntryService userFeedEntryService) {
        UserSubscribedListener listener = new UserSubscribedListener(userFeedEntryService);
        eventBus.register(listener);
        return listener;
    }
}
