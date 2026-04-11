package dev.javarush.feeder.api.feed;

import dev.javarush.feeder.feed.exception.FeedFetchException;
import dev.javarush.feeder.feed.exception.FeedNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FeedControllerAdvice {
  @ExceptionHandler({
      FeedNotFoundException.class
  })
  ResponseEntity<ProblemDetail> handleNotFound(RuntimeException exception) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.NOT_FOUND,
        exception.getLocalizedMessage()
    );
    return ResponseEntity.of(
        problemDetail
    ).build();
  }

  @ExceptionHandler({
      FeedFetchException.class
  })
  ResponseEntity<ProblemDetail> handleServerError(RuntimeException exception) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.INTERNAL_SERVER_ERROR,
        exception.getLocalizedMessage()
    );
    return ResponseEntity.of(
        problemDetail
    ).build();
  }
}
