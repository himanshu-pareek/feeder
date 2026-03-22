package dev.javarush.feeder.content.web;

import dev.javarush.feeder.content.UserFeedEntry;
import dev.javarush.feeder.content.UserFeedEntryService;
import java.util.Collection;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/{userId}/entries")
public class UserFeedEntryRestController {

    private final UserFeedEntryService userFeedEntryService;

    public UserFeedEntryRestController(UserFeedEntryService userFeedEntryService) {
        this.userFeedEntryService = userFeedEntryService;
    }

    @GetMapping
    public Collection<UserFeedEntry> getEntries(@PathVariable String userId) {
        return userFeedEntryService.getEntriesForUser(userId);
    }
}
