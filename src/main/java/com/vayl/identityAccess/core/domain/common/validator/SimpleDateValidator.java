package com.vayl.identityAccess.core.domain.common.validator;

import org.jspecify.annotations.NonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class SimpleDateValidator implements Validator {
  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

  public boolean isValid(@NonNull String dateStr) {
    if (dateStr == null) return false;
    try {
      LocalDate.parse(dateStr, FORMATTER);
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }
}
