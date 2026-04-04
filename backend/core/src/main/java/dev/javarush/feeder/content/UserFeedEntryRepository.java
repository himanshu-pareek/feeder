package dev.javarush.feeder.content;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

public interface UserFeedEntryRepository {
  boolean containsEntry(String userId, URI feedUri, String entryUri);

  record EntryKey(String userId, URI feedUri, URI feedEntryUri) {}
    
    void save(UserFeedEntry entry);
    void saveAll(Collection<UserFeedEntry> entries);
    Optional<UserFeedEntry> findByKey(EntryKey key);
    Collection<UserFeedEntry> findByUserId(String userId);
}
