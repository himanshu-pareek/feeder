package dev.javarush.feeder.user.web;

import dev.javarush.feeder.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserRestControllerAdvice {
  @ExceptionHandler(UserNotFoundException.class)
  ResponseEntity<ProblemDetail> handleNotFound(
      UserNotFoundException exception
  ) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.NOT_FOUND,
        exception.getLocalizedMessage()
    );
    return ResponseEntity.of(
        problemDetail
    ).build();
  }
}
