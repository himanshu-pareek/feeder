package dev.javarush.feeder.feed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestRomeFeedFetcher {
  private FeedFetcher feedFetcher;

  @BeforeEach
  void beforeEach() {
    feedFetcher = new RomeFeedFetcher();
  }

  @Test
  public void testRSS2Feed() throws URISyntaxException {
    var fileLocation = "sample-rss-feeds/sample-rss-2.xml";
    var feed = feedFetcher.fetchByUri(getResourceUri(fileLocation));
    assertNotNull(feed);
    assertEquals("rss_2.0", feed.getFeedType());
    assertEquals("NASA Space Station News", feed.getTitle());
    assertEquals("http://www.nasa.gov/", feed.getLink());
    assertEquals("A RSS news feed containing the latest NASA press releases on the International Space Station.", feed.getDescription());
    assertEquals("en-us", feed.getLanguage());
    assertEquals("neil.armstrong@example.com (Neil Armstrong)", feed.getManagingEditor());
    assertEquals("sally.ride@example.com (Sally Ride)", feed.getWebMaster());
    assertEquals(LocalDateTime.of(2003, 6, 10, 4, 0, 0), feed.getPublishedDate());

    assertNotNull(feed.getEntries());
    assertEquals(5, feed.getEntries().size());

    FeedEntry item1 = feed.getEntries().getFirst();
    assertEquals("Louisiana Students to Hear from NASA Astronauts Aboard Space Station", item1.title());
    assertEquals("http://www.nasa.gov/press-release/louisiana-students-to-hear-from-nasa-astronauts-aboard-space-station", item1.link());
    assertEquals("As part of the state's first Earth-to-space call, students from Louisiana will have an opportunity soon to hear from NASA astronauts aboard the International Space Station.", item1.description());
    assertEquals(LocalDateTime.of(2023, 7, 21, 13, 4, 0), item1.publishedDate());

    FeedEntry item2 = feed.getEntries().get(1);
    assertNull(item2.title());
    assertEquals("http://www.nasa.gov/press-release/nasa-awards-integrated-mission-operations-contract-iii", item2.link());
    assertEquals("NASA has selected KBR Wyle Services, LLC, of Fulton, Maryland, to provide mission and flight crew operations support for the International Space Station and future human space exploration.", item2.description());
    assertEquals(LocalDateTime.of(2023, 7, 20, 19, 5, 0), item2.publishedDate());
  }

  @Test
  public void testRSS091Feed() throws URISyntaxException {
    var fileLocation = "sample-rss-feeds/sample-rss-091.xml";
    var feed = feedFetcher.fetchByUri(getResourceUri(fileLocation));
    assertNotNull(feed);
    assertEquals("rss_0.91U", feed.getFeedType());
    assertEquals("WriteTheWeb", feed.getTitle());
    assertEquals("http://writetheweb.com", feed.getLink());
    assertEquals("News for web users that write back", feed.getDescription());
    assertEquals("en-us", feed.getLanguage());
    assertEquals("Copyright 2000, WriteTheWeb team.", feed.getCopyright());
    assertEquals("editor@writetheweb.com", feed.getManagingEditor());
    assertEquals("webmaster@writetheweb.com", feed.getWebMaster());
    assertNull(feed.getPublishedDate());

    assertNotNull(feed.getEntries());
    assertEquals(6, feed.getEntries().size());

    FeedEntry item1 = feed.getEntries().getFirst();
    assertEquals("Giving the world a pluggable Gnutella", item1.title());
    assertEquals("http://writetheweb.com/read.php?item=24", item1.link());
    assertEquals("WorldOS is a framework on which to build programs that work like Freenet or Gnutella -allowing distributed applications using peer-to-peer routing.", item1.description());
    assertNull(item1.publishedDate());
  }

  @Test
  public void testRSSThisPastWeekendFeed() throws URISyntaxException {
    var fileLocation = "sample-rss-feeds/thispastweekend-rss.xml";
    var feed = feedFetcher.fetchByUri(getResourceUri(fileLocation));
    assertNotNull(feed);
    assertEquals("rss_2.0", feed.getFeedType());
    assertEquals("This Past Weekend w/ Theo Von", feed.getTitle());
    assertEquals("https://art19.com/shows/this-past-weekend", feed.getLink());
    assertEquals("What happened this past weekend. And sometimes what happened on other days.", feed.getDescription());
    assertEquals("en", feed.getLanguage());
    assertEquals("2023 Theo Von", feed.getCopyright());
    assertNull(feed.getManagingEditor());
    assertNull(feed.getWebMaster());
    assertNull(feed.getPublishedDate());

    assertNotNull(feed.getEntries());
    assertFalse(feed.getEntries().isEmpty());

    FeedEntry item1 = feed.getEntries().getFirst();
    assertEquals("#644 - Bryan Johnson", item1.title());
    assertNull(item1.link());
    assertTrue(item1.description().startsWith("Bryan Johnson is an entrepreneur and longevity expert known for extensively"));
    assertEquals(LocalDateTime.of(2026, 3, 5, 17, 16, 0), item1.publishedDate());
    assertEquals(List.of(), item1.authors().stream().map(Person::name).collect(Collectors.toList()));
    assertEquals(List.of(), item1.categories());
    assertEquals(1, item1.enclosures().size());
    Enclosure enclosure = item1.enclosures().getFirst();
    assertEquals("https://www.podtrac.com/pts/redirect.mp3/pdst.fm/e/mgln.ai/e/94/claritaspod.com/measure/verifi.podscribe.com/rss/p/pfx.vpixl.com/j0JIg/traffic.megaphone.fm/ROOSTER1430439505.mp3?updated=1772731159", enclosure.url());
    assertEquals("audio/mpeg", enclosure.type());
    assertEquals(0, enclosure.length());
  }

  @Test
  public void testYoutubeRSSFeed() throws URISyntaxException {
    var fileLocation = "sample-rss-feeds/java-rush-youtube-rss.xml";
    var feed = feedFetcher.fetchByUri(getResourceUri(fileLocation));
    assertNotNull(feed);
    assertEquals("atom_1.0", feed.getFeedType());
    assertEquals("Java Rush", feed.getTitle());
    assertEquals("http://www.youtube.com/feeds/videos.xml?channel_id=UCNhH5zc8-_Hc7aE-wH7lM-g", feed.getLink());
    assertNull(feed.getPublishedDate());

    assertNotNull(feed.getEntries());
    assertFalse(feed.getEntries().isEmpty());

    FeedEntry item1 = feed.getEntries().getFirst();
    assertEquals("Serve Single Page Applications using Spring Boot - React, Vue, anything #react #vuejs #spring #java", item1.title());
    assertEquals("https://www.youtube.com/watch?v=1suDMh2sy_M", item1.link());
    assertEquals(LocalDateTime.of(2026, 1, 27, 4, 30, 3), item1.publishedDate());
    assertEquals(LocalDateTime.of(2026, 2, 24, 15, 39, 6), item1.updatedDate());
    assertEquals(List.of("Java Rush"), item1.authors().stream().map(Person::name).collect(Collectors.toList()));
  }

  private URI getResourceUri(String fileLocation) throws URISyntaxException {
    return Objects.requireNonNull(TestRomeFeedFetcher.class.getClassLoader()
        .getResource(fileLocation)).toURI();
  }
}
