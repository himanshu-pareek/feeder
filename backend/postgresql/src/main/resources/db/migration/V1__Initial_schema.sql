CREATE TABLE IF NOT EXISTS public.users (
    id VARCHAR(255) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS public.feeds (
    uri VARCHAR(255) PRIMARY KEY,
    title VARCHAR(255),
    link VARCHAR(255),
    description TEXT,
    published_date TIMESTAMP,
    authors JSONB,
    copyright VARCHAR(255),
    language VARCHAR(50),
    feed_type VARCHAR(50),
    managing_editor VARCHAR(255),
    web_master VARCHAR(255),
    last_synced_at TIMESTAMP,
    entries JSONB
);

CREATE TABLE IF NOT EXISTS public.subscriptions (
    user_id VARCHAR(255) REFERENCES users(id),
    feed_uri VARCHAR(255) REFERENCES feeds(uri),
    feed_name VARCHAR(255),
    subscribed_at TIMESTAMP,
    PRIMARY KEY (user_id, feed_uri)
);

CREATE TABLE IF NOT EXISTS public.user_feed_entries (
    user_id VARCHAR(255) REFERENCES users(id),
    feed_uri VARCHAR(255) REFERENCES feeds(uri),
    feed_entry_uri VARCHAR(255),
    title VARCHAR(255),
    link VARCHAR(255),
    description TEXT,
    published_date TIMESTAMP,
    updated_date TIMESTAMP,
    authors JSONB,
    categories JSONB,
    comments TEXT,
    contents JSONB,
    contributors JSONB,
    enclosures JSONB,
    is_read BOOLEAN,
    PRIMARY KEY (user_id, feed_uri, feed_entry_uri)
);
