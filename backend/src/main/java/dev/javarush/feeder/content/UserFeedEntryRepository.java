package dev.javarush.feeder.content;

import java.util.Collection;
import java.util.Optional;

public interface UserFeedEntryRepository {
    void save(UserFeedEntry entry);
    void saveAll(Collection<UserFeedEntry> entries);
    Optional<UserFeedEntry> findById(String id);
    Collection<UserFeedEntry> findByUserId(String userId);
}
