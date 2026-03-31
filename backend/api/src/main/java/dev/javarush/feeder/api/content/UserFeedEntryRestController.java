package dev.javarush.feeder.api.content;

import dev.javarush.feeder.content.UserFeedEntry;
import dev.javarush.feeder.content.use_case.FeedEntriesGetAction;
import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feedentries")
public class UserFeedEntryRestController {

    private final FeedEntriesGetAction feedEntriesGetActionUseCase;

    public UserFeedEntryRestController(FeedEntriesGetAction feedEntriesGetActionUseCase) {
      this.feedEntriesGetActionUseCase = feedEntriesGetActionUseCase;
    }

    @GetMapping
    public Collection<UserFeedEntry> getEntries(Authentication authentication) {
        return feedEntriesGetActionUseCase.execute(authentication.getName());
    }
}
