package dev.javarush.feeder.api.converter;

import java.net.URI;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

public class StringToUriConverter implements Converter<String, URI> {

  @Override
  public URI convert(@NonNull String source) {
    if (source.isBlank()) {
      return null;
    }
    String normalized = source.trim().toLowerCase();

    if (normalized.startsWith("http://")) {
      normalized = normalized.substring(7);
    } else if (normalized.startsWith("https://")) {
      normalized = normalized.substring(8);
    }

    if (normalized.startsWith("www.")) {
      normalized = normalized.substring(4);
    }

    if (normalized.endsWith("/")) {
      normalized = normalized.substring(0, normalized.length() - 1);
    }

    return URI.create("https://" + normalized);
  }
}
