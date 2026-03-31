package dev.javarush.feeder.feed.exception;

public class FeedNotFoundException extends RuntimeException {
  public FeedNotFoundException(String s) {
    super(s);
  }
}
