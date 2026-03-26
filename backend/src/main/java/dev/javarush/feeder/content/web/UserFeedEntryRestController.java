package dev.javarush.feeder.content.web;

import dev.javarush.feeder.content.UserFeedEntry;
import dev.javarush.feeder.content.use_case.FeedEntriesGetAction;
import java.util.Collection;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/{userId}/entries")
public class UserFeedEntryRestController {

    private final FeedEntriesGetAction feedEntriesGetActionUseCase;

    public UserFeedEntryRestController(FeedEntriesGetAction feedEntriesGetActionUseCase) {
      this.feedEntriesGetActionUseCase = feedEntriesGetActionUseCase;
    }

    @GetMapping
    public Collection<UserFeedEntry> getEntries(@PathVariable String userId) {
        return feedEntriesGetActionUseCase.execute(userId);
    }
}
