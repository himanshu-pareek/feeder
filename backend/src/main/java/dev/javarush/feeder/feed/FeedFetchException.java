package dev.javarush.feeder.feed;

public class FeedFetchException extends RuntimeException {
  public FeedFetchException(String message, Throwable cause) {
    super(message, cause);
  }
}
