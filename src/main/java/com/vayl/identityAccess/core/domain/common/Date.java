package com.vayl.identityAccess.core.domain.common;

import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Date {
  private String date;

  public Date(String date) {
    this.setDate(date);
  }

  private void setDate(String date) {
    this.throwErrorOnInvalidDate(date);
    this.date = date;
  }

  private void throwErrorOnInvalidDate(String date) {
    try {
      // This specifically looks for the YYYY-MM-DDTHH:mm:ssZ format
      DateTimeFormatter.ISO_INSTANT.parse(date);
    } catch (DateTimeParseException e) {
      throw new InvalidValueException(
          ExceptionEvent.DATE_CREATION,
          ExceptionReason.INVALID_DATE_FORMAT,
          date,
          ExceptionLevel.INFO);
    }
  }

  @Override
  public String toString() {
    return this.date;
  }

  @Override
  public boolean equals(Object anObject) {

    boolean isEqual = false;
    if (anObject != null && this.getClass() == anObject.getClass()) {
      Date typedObject = (Date) anObject;
      isEqual = typedObject.toString().equals(this.toString());
    }

    return isEqual;
  }

  @Override
  public int hashCode() {
    return this.date.hashCode();
  }
}
