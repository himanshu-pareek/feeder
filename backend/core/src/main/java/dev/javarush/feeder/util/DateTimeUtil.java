package dev.javarush.feeder.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class DateTimeUtil {
  public static LocalDateTime nowInUTC() {
    return LocalDateTime.now(ZoneOffset.UTC);
  }
}
