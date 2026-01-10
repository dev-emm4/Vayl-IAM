package com.vayl.identityAccess.core.domain.common.DomainErrors;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

public class InvalidValueException extends RuntimeException implements DomainException {
  private final ExceptionEvent event;
  private final ExceptionReason reason;
  private final String invalidValue;
  private final ExceptionLevel level;

  public InvalidValueException(
      ExceptionEvent event, ExceptionReason reason, String aInvalidValue, ExceptionLevel level) {
    this.reason = reason;
    this.event = event;
    this.invalidValue = aInvalidValue;
    this.level = level;
  }

  @Override
  public String toString() {
    return "InvalidValueException{"
        + "event="
        + event
        + ", reason="
        + reason
        + ", invalidValue='"
        + invalidValue
        + '\''
        + ", level="
        + level
        + '}';
  }

  public ObjectNode toMap() {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode node = mapper.createObjectNode();
    node.put("event", this.event.toString());
    node.put("reason", this.reason.toString());
    node.put("invalidValue", this.invalidValue);
    node.put("level", this.level.toString());
    return node;
  }

  public String invalidValue() {
    return this.invalidValue;
  }

  public ExceptionEvent event() {
    return this.event;
  }

  public ExceptionReason reason() {
    return this.reason;
  }

  public ExceptionLevel level() {
    return this.level;
  }
}
