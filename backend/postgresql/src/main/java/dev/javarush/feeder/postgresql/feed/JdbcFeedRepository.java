package dev.javarush.feeder.postgresql.feed;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.javarush.feeder.feed.Feed;
import dev.javarush.feeder.feed.FeedEntry;
import dev.javarush.feeder.feed.FeedRepository;
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

@Repository
public class JdbcFeedRepository implements FeedRepository {

    private static final String INSERT_FEED_SQL = """
        INSERT INTO feeds (uri, title, link, description, published_date, authors, copyright, language, feed_type, managing_editor, web_master, last_synced_at, entries)
        VALUES (?, ?, ?, ?, ?, ?::jsonb, ?, ?, ?, ?, ?, ?, ?::jsonb)
        ON CONFLICT (uri) DO UPDATE SET
        title = EXCLUDED.title,
        link = EXCLUDED.link,
        description = EXCLUDED.description,
        published_date = EXCLUDED.published_date,
        authors = EXCLUDED.authors,
        copyright = EXCLUDED.copyright,
        language = EXCLUDED.language,
        feed_type = EXCLUDED.feed_type,
        managing_editor = EXCLUDED.managing_editor,
        web_master = EXCLUDED.web_master,
        last_synced_at = EXCLUDED.last_synced_at,
        entries = EXCLUDED.entries
        """;

    private static final String SELECT_BY_URI_SQL = "SELECT * FROM feeds WHERE uri = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM feeds";

    private final JdbcClient jdbcClient;

    public JdbcFeedRepository(DataSource dataSource) {
        this.jdbcClient = JdbcClient.create(dataSource);
    }

    @Override
    public void save(Feed feed) {
        jdbcClient.sql(INSERT_FEED_SQL)
            .param(feed.getUri().toString())
            .param(feed.getTitle())
            .param(feed.getLink())
            .param(feed.getDescription())
            .param(feed.getPublishedDate() != null ? Timestamp.valueOf(feed.getPublishedDate()) : null)
            .param(JsonUtil.toJson(feed.getAuthors()))
            .param(feed.getCopyright())
            .param(feed.getLanguage())
            .param(feed.getFeedType())
            .param(feed.getManagingEditor())
            .param(feed.getWebMaster())
            .param(feed.getLastSyncedAt() != null ? Timestamp.valueOf(feed.getLastSyncedAt()) : null)
            .param(JsonUtil.toJson(feed.getEntries()))
            .update();
    }

    @Override
    public Optional<Feed> findByUri(URI uri) {
        return jdbcClient.sql(SELECT_BY_URI_SQL)
            .param(uri.toString())
            .query(this::mapRow)
            .optional();
    }

    @Override
    public Collection<Feed> findAll() {
        return jdbcClient.sql(SELECT_ALL_SQL)
            .query(this::mapRow)
            .list();
    }

    private Feed mapRow(ResultSet rs, int rowNum) throws SQLException {
        Feed feed = new Feed(
            URI.create(rs.getString("uri")),
            rs.getString("title"),
            rs.getString("link"),
            rs.getString("description")
        );
        Timestamp pubDate = rs.getTimestamp("published_date");
        if (pubDate != null) feed.setPublishedDate(pubDate.toLocalDateTime());

        String authorsJson = rs.getString("authors");
        if (authorsJson != null) {
            feed.setAuthors(JsonUtil.fromJson(authorsJson, new TypeReference<List<String>>() {}));
        }

        feed.setCopyright(rs.getString("copyright"));
        feed.setLanguage(rs.getString("language"));
        feed.setFeedType(rs.getString("feed_type"));
        feed.setManagingEditor(rs.getString("managing_editor"));
        feed.setWebMaster(rs.getString("web_master"));

        Timestamp lastSyncedAt = rs.getTimestamp("last_synced_at");
        if (lastSyncedAt != null) feed.setLastSyncedAt(lastSyncedAt.toLocalDateTime());

        String entriesJson = rs.getString("entries");
        if (entriesJson != null) {
            feed.setEntries(JsonUtil.fromJson(entriesJson, new TypeReference<List<FeedEntry>>() {}));
        }

        return feed;
    }
}