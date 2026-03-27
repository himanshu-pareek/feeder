package dev.javarush.feeder.content.event;

import dev.javarush.feeder.content.UserFeedEntryService;
import dev.javarush.feeder.feed.Feed;
import dev.javarush.feeder.feed.FeedService;
import dev.javarush.feeder.event.FeedSubscriptionEvent;
import java.util.Objects;
import org.springframework.context.event.EventListener;

/**
 * Listener that handles UserSubscribedEvent by delegating to UserFeedEntryService.
 */
public class FeedSubscriptionEventListener {

    private final UserFeedEntryService userFeedEntryService;
    private final FeedService feedService;

    public FeedSubscriptionEventListener(
        UserFeedEntryService userFeedEntryService,
        FeedService feedService
    ) {
        this.userFeedEntryService = Objects.requireNonNull(userFeedEntryService);
        this.feedService = Objects.requireNonNull(feedService);
    }

    @EventListener(FeedSubscriptionEventListener.class)
    public void handleFeedSubscription(FeedSubscriptionEvent event) {
        System.out.println("Handling subscription event");
        Feed feed = this.feedService.getFeed(event.feedUri());
        userFeedEntryService.createEntriesForUser(event.userId(), feed);
    }
}
