package dev.javarush.feeder.experiment;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import dev.javarush.feeder.feed.FeedFetcher;
import dev.javarush.feeder.feed.RomeFeedFetcher;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;

public class TestRomeLibrary {
  @Test
  public void testRSS2Feed() throws URISyntaxException {
    var fileLocation = "sample-rss-feeds/sample-rss-2.xml";
    FeedFetcher feedFetcher = new RomeFeedFetcher();
    var feed = feedFetcher
        .fetchByUri(
            TestRomeLibrary.class.getClassLoader()
                .getResource(fileLocation).toURI()
        );
    System.out.println(feed);
  }

  @Test
  public void testRSS091Feed() throws URISyntaxException {
    var fileLocation = "sample-rss-feeds/sample-rss-091.xml";
    FeedFetcher feedFetcher = new RomeFeedFetcher();
    var feed = feedFetcher
        .fetchByUri(
            TestRomeLibrary.class.getClassLoader()
                .getResource(fileLocation).toURI()
        );
    System.out.println(feed);
  }

  @Test
  public void testRSSThisPastWeekendFeed() throws URISyntaxException {
    var fileLocation = "sample-rss-feeds/thispastweekend-rss.xml";
    FeedFetcher feedFetcher = new RomeFeedFetcher();
    var feed = feedFetcher
        .fetchByUri(
            TestRomeLibrary.class.getClassLoader()
                .getResource(fileLocation).toURI()
        );
    System.out.println(feed);
  }

  @Test
  public void testYoutubeRSSFeed() throws URISyntaxException {
    var fileLocation = "sample-rss-feeds/java-rush-youtube-rss.xml";
    FeedFetcher feedFetcher = new RomeFeedFetcher();
    var feed = feedFetcher
        .fetchByUri(
            TestRomeLibrary.class.getClassLoader()
                .getResource(fileLocation).toURI()
        );
    System.out.println(feed);
  }
}
