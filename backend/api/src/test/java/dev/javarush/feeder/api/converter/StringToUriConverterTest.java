package dev.javarush.feeder.api.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import org.junit.jupiter.api.Test;

class StringToUriConverterTest {

  private final StringToUriConverter converter = new StringToUriConverter();

  @Test
  void shouldNormalizeUris() {
    String expected = "https://example.com";

    assertEquals(URI.create(expected), converter.convert("https://example.com"));
    assertEquals(URI.create(expected), converter.convert("example.com"));
    assertEquals(URI.create(expected), converter.convert("https://www.example.com"));
    assertEquals(URI.create(expected), converter.convert("www.example.com"));
    assertEquals(URI.create(expected), converter.convert("http://example.com"));
    assertEquals(URI.create(expected), converter.convert("http://www.example.com"));
    assertEquals(URI.create(expected), converter.convert("example.com/"));
    assertEquals(URI.create(expected), converter.convert("https://example.com/"));
  }

  @Test
  void shouldHandleMixedCaseAndSpaces() {
    assertEquals(URI.create("https://example.com"), converter.convert("  HTTPS://EXAMPLE.COM/  "));
  }

  @Test
  void shouldKeepPathButNormalize() {
    assertEquals(URI.create("https://example.com/rss"), converter.convert("example.com/rss/"));
    assertEquals(URI.create("https://example.com/rss"), converter.convert("www.example.com/rss"));
  }
}
