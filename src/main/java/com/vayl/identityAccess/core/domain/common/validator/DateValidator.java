package com.vayl.identityAccess.core.domain.common.validator;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.jspecify.annotations.NonNull;

public class DateValidator implements Validator {
  DateTimeFormatter util = DateTimeFormatter.ISO_INSTANT;

  @Override
  public boolean isValid(@NonNull String date) {
    try {
      DateTimeFormatter.ISO_INSTANT.parse(date);
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }
}
