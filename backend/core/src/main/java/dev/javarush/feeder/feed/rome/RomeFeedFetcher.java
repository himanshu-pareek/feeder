package dev.javarush.feeder.feed.rome;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import dev.javarush.feeder.feed.Feed;
import dev.javarush.feeder.feed.FeedFetcher;
import dev.javarush.feeder.feed.converters.SyndFeedToFeed;
import dev.javarush.feeder.feed.exception.FeedFetchException;
import dev.javarush.feeder.util.DateTimeUtil;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class RomeFeedFetcher implements FeedFetcher {
  @Override
  public Feed fetchByUri(URI uri) {
    try {
      var connection = uri.toURL().openConnection();

      connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
      connection.setConnectTimeout(5000);
      connection.setReadTimeout(5000);

      try (
          InputStream inputStream = connection.getInputStream();
          XmlReader reader = new XmlReader(inputStream)
      ) {
        SyndFeedInput syndFeedInput = new SyndFeedInput();
        SyndFeed syndFeed = syndFeedInput.build(reader);
        var feed = SyndFeedToFeed.convert(syndFeed);
        feed.setUri(uri);
        feed.setLastSyncedAt(DateTimeUtil.nowInUTC());
        return feed;
      }
    } catch (IOException e) {
      throw new FeedFetchException("Error while fetching feed " + e.getLocalizedMessage(), e);
    } catch (FeedException e) {
      throw new FeedFetchException("Error while parsing feed " + e.getLocalizedMessage(), e);
    }
  }
}
