package dev.javarush.feeder.api.config;

import dev.javarush.feeder.api.event.FeedSyncEventListener;
import dev.javarush.feeder.api.event.FeedSyncEventPublisher;
import dev.javarush.feeder.content.UserFeedEntryRepository;
import dev.javarush.feeder.content.UserFeedEntryService;
import dev.javarush.feeder.api.event.FeedSubscriptionEventListener;
import dev.javarush.feeder.content.use_case.FeedEntriesGetAction;
import dev.javarush.feeder.content.use_case.UserFeedEntriesSyncAction;
import dev.javarush.feeder.event.EventPublisher;
import dev.javarush.feeder.event.FeedSubscribe;
import dev.javarush.feeder.event.FeedSync;
import dev.javarush.feeder.feed.FeedFetcher;
import dev.javarush.feeder.feed.FeedRepository;
import dev.javarush.feeder.feed.FeedService;
import dev.javarush.feeder.feed.rome.RomeFeedFetcher;
import dev.javarush.feeder.feed.use_case.FeedSyncAction;
import dev.javarush.feeder.memory.content.InMemoryUserFeedEntryRepository;
import dev.javarush.feeder.memory.feed.InMemoryFeedRepository;
import dev.javarush.feeder.memory.user.InMemoryUserRepository;
import dev.javarush.feeder.user.UserRepository;
import dev.javarush.feeder.user.UserService;
import dev.javarush.feeder.api.event.UserFeedSubscriptionEventPublisher;
import dev.javarush.feeder.user.use_case.FeedSubscriptionAction;
import dev.javarush.feeder.user.use_case.UserRegistrationAction;
import dev.javarush.feeder.postgresql.content.JdbcUserFeedEntryRepository;
import dev.javarush.feeder.postgresql.feed.JdbcFeedRepository;
import dev.javarush.feeder.postgresql.user.JdbcUserRepository;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class ProjectConfig {

    @Bean
    @ConditionalOnProperty(name = "repository.type", havingValue = "memory", matchIfMissing = true)
    public UserRepository userRepository() {
        return new InMemoryUserRepository();
    }

    @Bean
    @ConditionalOnProperty(name = "repository.type", havingValue = "jdbc")
    public UserRepository jdbcUserRepository(DataSource dataSource) {
        return new JdbcUserRepository(dataSource);
    }

    @Bean
    @ConditionalOnProperty(name = "repository.type", havingValue = "memory", matchIfMissing = true)
    public FeedRepository feedRepository() {
        return new InMemoryFeedRepository();
    }

    @Bean
    @ConditionalOnProperty(name = "repository.type", havingValue = "jdbc")
    public FeedRepository jdbcFeedRepository(DataSource dataSource) {
        return new JdbcFeedRepository(dataSource);
    }

    @Bean
    @ConditionalOnProperty(name = "repository.type", havingValue = "memory", matchIfMissing = true)
    public UserFeedEntryRepository userFeedEntryRepository() {
        return new InMemoryUserFeedEntryRepository();
    }

    @Bean
    @ConditionalOnProperty(name = "repository.type", havingValue = "jdbc")
    public UserFeedEntryRepository jdbcUserFeedEntryRepository(DataSource dataSource) {
        return new JdbcUserFeedEntryRepository(dataSource);
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
    FeedSubscriptionEventListener userSubscribedListener(UserFeedEntryService userFeedEntryService, FeedService feedService) {
      return new FeedSubscriptionEventListener(userFeedEntryService, feedService);
    }

    @Bean
    UserFeedEntriesSyncAction userFeedEntriesSyncAction(
        UserService userService,
        FeedService feedService,
        UserFeedEntryService userFeedEntryService
    ) {
        return new UserFeedEntriesSyncAction(
            userService,
            userFeedEntryService,
            feedService
        );
    }

    @Bean
    FeedSyncEventListener feedSyncEventListener(UserFeedEntriesSyncAction userFeedEntriesSyncAction) {
        return new FeedSyncEventListener(userFeedEntriesSyncAction);
    }

    @Bean
    EventPublisher<FeedSync> feedSyncEventPublisher() {
        return new FeedSyncEventPublisher();
    }

    @Bean
    FeedSyncAction feedSyncAction(FeedService feedService, EventPublisher<FeedSync> feedSyncEventPublisher) {
        return new FeedSyncAction(feedService, feedSyncEventPublisher);
    }
}
