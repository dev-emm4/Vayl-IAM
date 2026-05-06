package com.vayl.identityAccess.core.domain.common.validator;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.jspecify.annotations.NonNull;

public class DateTimeValidator implements Validator {

  @Override
  public boolean isValid(@NonNull String dateStr) {
    if (dateStr == null || dateStr.isBlank()) {
      return false;
    }
    try {
      // ISO_OFFSET_DATE_TIME is the built-in standard for these formats
      OffsetDateTime.parse(dateStr, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }
}
