package dev.javarush.feeder.postgresql.user;

import dev.javarush.feeder.subscription.Subscription;
import dev.javarush.feeder.user.User;
import dev.javarush.feeder.user.UserRepository;
import java.net.URI;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JdbcUserRepository implements UserRepository {

    private static final String INSERT_USER_SQL = "INSERT INTO users (id) VALUES (?) ON CONFLICT (id) DO NOTHING";
    private static final String DELETE_SUBSCRIPTIONS_SQL = "DELETE FROM subscriptions WHERE user_id = ?";
    private static final String INSERT_SUBSCRIPTION_SQL = "INSERT INTO subscriptions (user_id, feed_uri, feed_name, subscribed_at) VALUES (?, ?, ?, ?)";
    private static final String SELECT_USER_ID_SQL = "SELECT id FROM users WHERE id = ?";
    private static final String SELECT_SUBSCRIPTIONS_SQL = "SELECT feed_uri, feed_name, subscribed_at FROM subscriptions WHERE user_id = ?";
    private static final String SELECT_SUBSCRIBED_USERS_SQL = "SELECT user_id FROM subscriptions WHERE feed_uri = ?";

    private final JdbcClient jdbcClient;

    public JdbcUserRepository(DataSource dataSource) {
        this.jdbcClient = JdbcClient.create(dataSource);
    }

    @Override
    @Transactional
    public void save(User user) {
        jdbcClient.sql(INSERT_USER_SQL)
            .param(user.getId())
            .update();

        // Remove old subscriptions
        jdbcClient.sql(DELETE_SUBSCRIPTIONS_SQL)
            .param(user.getId())
            .update();

        // Add current subscriptions
        for (Subscription sub : user.getSubscriptions()) {
            jdbcClient.sql(INSERT_SUBSCRIPTION_SQL)
                .param(user.getId())
                .param(sub.feedUri().toString())
                .param(sub.feedName())
                .param(Timestamp.valueOf(sub.subscribedAt()))
                .update();
        }
    }

    @Override
    public Optional<User> findById(String id) {
        Optional<String> userId = jdbcClient.sql(SELECT_USER_ID_SQL)
            .param(id)
            .query(String.class)
            .optional();

        if (userId.isEmpty()) {
            return Optional.empty();
        }

        Set<Subscription> subscriptions = jdbcClient.sql(SELECT_SUBSCRIPTIONS_SQL)
            .param(id)
            .query((rs, rowNum) -> new Subscription(
                URI.create(rs.getString("feed_uri")),
                rs.getString("feed_name"),
                rs.getTimestamp("subscribed_at").toLocalDateTime()
            ))
            .list()
            .stream()
            .collect(Collectors.toSet());

        return Optional.of(new User(userId.get(), subscriptions));
    }

    @Override
    public Collection<String> findAllSubscribedTo(URI feedUri) {
        return jdbcClient.sql(SELECT_SUBSCRIBED_USERS_SQL)
            .param(feedUri.toString())
            .query(String.class)
            .list();
    }
}