package dev.javarush.feeder.content;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryUserFeedEntryRepository implements UserFeedEntryRepository {
    private final Map<EntryKey, UserFeedEntry> entries = new ConcurrentHashMap<>();

    @Override
    public void save(UserFeedEntry entry) {
        EntryKey key = new EntryKey(entry.getUserId(), entry.getFeedUri(), entry.getFeedEntryUri());
        entries.put(key, entry);
    }

    @Override
    public void saveAll(Collection<UserFeedEntry> entriesToSave) {
        entriesToSave.forEach(this::save);
    }

    @Override
    public Optional<UserFeedEntry> findByKey(EntryKey key) {
        return Optional.ofNullable(entries.get(key));
    }

    @Override
    public Collection<UserFeedEntry> findByUserId(String userId) {
        return entries.values().stream()
            .filter(e -> e.getUserId().equals(userId))
            .collect(Collectors.toList());
    }
}
