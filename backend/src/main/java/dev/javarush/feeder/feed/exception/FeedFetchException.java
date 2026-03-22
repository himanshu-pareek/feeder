package dev.javarush.feeder.feed.exception;

public class FeedFetchException extends RuntimeException {
  public FeedFetchException(String message, Throwable cause) {
    super(message, cause);
  }
}
