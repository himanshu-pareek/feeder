package dev.javarush.feeder.api.config;

import dev.javarush.feeder.content.UserFeedEntryRepository;
import dev.javarush.feeder.content.UserFeedEntryService;
import dev.javarush.feeder.api.event.FeedSubscriptionEventListener;
import dev.javarush.feeder.content.use_case.FeedEntriesGetAction;
import dev.javarush.feeder.event.EventPublisher;
import dev.javarush.feeder.event.FeedSubscribe;
import dev.javarush.feeder.feed.FeedFetcher;
import dev.javarush.feeder.feed.FeedRepository;
import dev.javarush.feeder.feed.FeedService;
import dev.javarush.feeder.feed.rome.RomeFeedFetcher;
import dev.javarush.feeder.memory.content.InMemoryUserFeedEntryRepository;
import dev.javarush.feeder.memory.feed.InMemoryFeedRepository;
import dev.javarush.feeder.memory.user.InMemoryUserRepository;
import dev.javarush.feeder.user.UserRepository;
import dev.javarush.feeder.user.UserService;
import dev.javarush.feeder.api.event.UserFeedSubscriptionEventPublisher;
import dev.javarush.feeder.user.use_case.FeedSubscriptionAction;
import dev.javarush.feeder.user.use_case.UserRegistrationAction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

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
    public FeedEntriesGetAction getFeedEntries(UserFeedEntryService userFeedEntryService) {
        return new FeedEntriesGetAction(userFeedEntryService);
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserService(userRepository);
    }

    @Bean
    UserRegistrationAction userRegistrationAction(UserService userService) {
        return new UserRegistrationAction(userService);
    }

    @Bean
    EventPublisher<FeedSubscribe> feedSubscriptionEventEventPublisher() {
        return new UserFeedSubscriptionEventPublisher();
    }

    @Bean
    FeedSubscriptionAction feedSubscriptionAction(
        UserService userService,
        FeedService feedService,
        EventPublisher<FeedSubscribe> feedSubscriptionEventPublisher
    ) {
        return new FeedSubscriptionAction(
            userService,
            feedService,
            feedSubscriptionEventPublisher
        );
    }

    @Bean
    public FeedSubscriptionEventListener userSubscribedListener(UserFeedEntryService userFeedEntryService, FeedService feedService) {
      return new FeedSubscriptionEventListener(userFeedEntryService, feedService);
    }
}
