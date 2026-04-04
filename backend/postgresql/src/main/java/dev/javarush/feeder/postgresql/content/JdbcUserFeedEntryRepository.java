package dev.javarush.feeder.postgresql.content;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.javarush.feeder.content.UserFeedEntry;
import dev.javarush.feeder.content.UserFeedEntryRepository;
import dev.javarush.feeder.feed.Enclosure;
import dev.javarush.feeder.feed.FeedEntry;
import dev.javarush.feeder.feed.FeedEntryContent;
import dev.javarush.feeder.feed.Person;
import dev.javarush.feeder.postgresql.util.JsonUtil;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JdbcUserFeedEntryRepository implements UserFeedEntryRepository {

    private static final String CONTAINS_ENTRY_SQL = "SELECT COUNT(*) FROM user_feed_entries WHERE user_id = ? AND feed_uri = ? AND feed_entry_uri = ?";
    
    private static final String INSERT_USER_FEED_ENTRY_SQL = """
        INSERT INTO user_feed_entries (user_id, feed_uri, feed_entry_uri, title, link, description, published_date, updated_date, authors, categories, comments, contents, contributors, enclosures, is_read)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?::jsonb, ?::jsonb, ?, ?::jsonb, ?::jsonb, ?::jsonb, ?)
        ON CONFLICT (user_id, feed_uri, feed_entry_uri) DO UPDATE SET
        title = EXCLUDED.title,
        link = EXCLUDED.link,
        description = EXCLUDED.description,
        published_date = EXCLUDED.published_date,
        updated_date = EXCLUDED.updated_date,
        authors = EXCLUDED.authors,
        categories = EXCLUDED.categories,
        comments = EXCLUDED.comments,
        contents = EXCLUDED.contents,
        contributors = EXCLUDED.contributors,
        enclosures = EXCLUDED.enclosures,
        is_read = EXCLUDED.is_read
        """;

    private static final String SELECT_BY_KEY_SQL = "SELECT * FROM user_feed_entries WHERE user_id = ? AND feed_uri = ? AND feed_entry_uri = ?";
    private static final String SELECT_BY_USER_ID_SQL = "SELECT * FROM user_feed_entries WHERE user_id = ?";

    private final JdbcClient jdbcClient;

    public JdbcUserFeedEntryRepository(DataSource dataSource) {
        this.jdbcClient = JdbcClient.create(dataSource);
    }

    @Override
    public boolean containsEntry(String userId, URI feedUri, String entryUri) {
        return jdbcClient.sql(CONTAINS_ENTRY_SQL)
            .param(userId)
            .param(feedUri.toString())
            .param(entryUri)
            .query(Long.class)
            .single() > 0;
    }

    @Override
    public void save(UserFeedEntry entry) {
        saveInternal(entry);
    }

    @Override
    @Transactional
    public void saveAll(Collection<UserFeedEntry> entries) {
        for (UserFeedEntry entry : entries) {
            saveInternal(entry);
        }
    }

    private void saveInternal(UserFeedEntry entry) {
        jdbcClient.sql(INSERT_USER_FEED_ENTRY_SQL)
            .param(entry.getUserId())
            .param(entry.getFeedUri().toString())
            .param(entry.getFeedEntryUri().toString())
            .param(entry.getTitle())
            .param(entry.getLink())
            .param(entry.getDescription())
            .param(entry.getPublishedDate() != null ? Timestamp.valueOf(entry.getPublishedDate()) : null)
            .param(entry.getUpdatedDate() != null ? Timestamp.valueOf(entry.getUpdatedDate()) : null)
            .param(JsonUtil.toJson(entry.getAuthors()))
            .param(JsonUtil.toJson(entry.getCategories()))
            .param(entry.getComments())
            .param(JsonUtil.toJson(entry.getContents()))
            .param(JsonUtil.toJson(entry.getContributors()))
            .param(JsonUtil.toJson(entry.getEnclosures()))
            .param(entry.isRead())
            .update();
    }

    @Override
    public Optional<UserFeedEntry> findByKey(EntryKey key) {
        return jdbcClient.sql(SELECT_BY_KEY_SQL)
            .param(key.userId())
            .param(key.feedUri().toString())
            .param(key.feedEntryUri().toString())
            .query(this::mapRow)
            .optional();
    }

    @Override
    public Collection<UserFeedEntry> findByUserId(String userId) {
        return jdbcClient.sql(SELECT_BY_USER_ID_SQL)
            .param(userId)
            .query(this::mapRow)
            .list();
    }

    private UserFeedEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
        String userId = rs.getString("user_id");
        URI feedUri = URI.create(rs.getString("feed_uri"));
        
        FeedEntry.Builder builder = FeedEntry.builder()
            .uri(rs.getString("feed_entry_uri"))
            .title(rs.getString("title"))
            .link(rs.getString("link"))
            .description(rs.getString("description"));
            
        Timestamp pubDate = rs.getTimestamp("published_date");
        if (pubDate != null) builder.publishedDate(pubDate.toLocalDateTime());
        
        Timestamp upDate = rs.getTimestamp("updated_date");
        if (upDate != null) builder.updatedDate(upDate.toLocalDateTime());
        
        String authorsJson = rs.getString("authors");
        if (authorsJson != null) {
            builder.authors(JsonUtil.fromJson(authorsJson, new TypeReference<List<Person>>() {}));
        }
        
        String categoriesJson = rs.getString("categories");
        if (categoriesJson != null) {
            builder.categories(JsonUtil.fromJson(categoriesJson, new TypeReference<List<String>>() {}));
        }
        
        builder.comments(rs.getString("comments"));
        
        String contentsJson = rs.getString("contents");
        if (contentsJson != null) {
            builder.contents(JsonUtil.fromJson(contentsJson, new TypeReference<List<FeedEntryContent>>() {}));
        }
        
        String contributorsJson = rs.getString("contributors");
        if (contributorsJson != null) {
            builder.contributors(JsonUtil.fromJson(contributorsJson, new TypeReference<List<Person>>() {}));
        }
        
        String enclosuresJson = rs.getString("enclosures");
        if (enclosuresJson != null) {
            builder.enclosures(JsonUtil.fromJson(enclosuresJson, new TypeReference<List<Enclosure>>() {}));
        }
        
        UserFeedEntry entry = new UserFeedEntry(userId, feedUri, builder.build());
        if (rs.getBoolean("is_read")) {
            entry.markAsRead();
        } else {
            entry.markAsUnread();
        }
        
        return entry;
    }
}