package dev.javarush.feeder.content;

import com.google.common.eventbus.Subscribe;
import dev.javarush.feeder.user.events.UserSubscribedEvent;
import java.util.Objects;

/**
 * Listener that handles UserSubscribedEvent by delegating to UserFeedEntryService.
 */
public class UserSubscribedListener {

    private final UserFeedEntryService userFeedEntryService;

    public UserSubscribedListener(UserFeedEntryService userFeedEntryService) {
        this.userFeedEntryService = Objects.requireNonNull(userFeedEntryService);
    }

    @Subscribe
    public void handleUserSubscribed(UserSubscribedEvent event) {
        userFeedEntryService.createEntriesForUser(event.userId(), event.feed());
    }
}
