package com.vayl.identityAccess.core.domain.common.DomainErrors;

public class InvalidValueException extends RuntimeException implements DomainException {
  private String userId; // optional
  private String event;
  private String invalidValue;

  public InvalidValueException(String aUserId, String aEvent, String aInvalidValue) {
    this.userId = aUserId;
    this.event = aEvent;
    this.invalidValue = aInvalidValue;
  }

  public InvalidValueException(String aEvent, String aInvalidValue) {
    this.userId = null;
    this.event = aEvent;
    this.invalidValue = aInvalidValue;
  }

  @Override
  public String toString() {
    return "InvalidValueMsg{" +
            "userId='" + this.userId + '\'' +
            ", event='" + this.event + '\'' +
            ", invalidValue='" + this.invalidValue + '\'' +
            '}';
  }

  public String userId() {
    return this.userId;
  }

  public String invalidValue() {
    return this.invalidValue;
  }

  public String event() {
    return this.event;
  }

}
