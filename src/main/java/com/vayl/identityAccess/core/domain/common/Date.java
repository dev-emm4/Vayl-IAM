package com.vayl.identityAccess.core.domain.common;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Date {
  private String date;

  public Date(String aDate) {
    this.setDate(aDate);
  }

  private void setDate(String aDate) {
    this.throwErrorOnInvalidDate(aDate);
    this.date = aDate;
  }

  private void throwErrorOnInvalidDate(String aDate) {
    try {
      // This specifically looks for the YYYY-MM-DDTHH:mm:ssZ format
      DateTimeFormatter.ISO_INSTANT.parse(aDate);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("invalid date");
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
}
