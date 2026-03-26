package dev.javarush.feeder.config;

import dev.javarush.feeder.content.memory.InMemoryUserFeedEntryRepository;
import dev.javarush.feeder.content.UserFeedEntryRepository;
import dev.javarush.feeder.content.UserFeedEntryService;
import dev.javarush.feeder.content.event.UserSubscribedListener;
import dev.javarush.feeder.content.use_case.FeedEntriesGetAction;
import dev.javarush.feeder.feed.FeedFetcher;
import dev.javarush.feeder.feed.FeedRepository;
import dev.javarush.feeder.feed.FeedService;
import dev.javarush.feeder.feed.memory.InMemoryFeedRepository;
import dev.javarush.feeder.feed.rome.RomeFeedFetcher;
import dev.javarush.feeder.user.memory.InMemoryUserRepository;
import dev.javarush.feeder.user.UserRepository;
import dev.javarush.feeder.user.UserService;
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
    public UserSubscribedListener userSubscribedListener(UserFeedEntryService userFeedEntryService, FeedService feedService) {
        UserSubscribedListener listener = new UserSubscribedListener(userFeedEntryService, feedService);
        return listener;
    }
}
