package com.vayl.identityAccess.core.domain.common.DomainException;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

public class InvalidValueException extends RuntimeException implements DomainException {
  private final ExceptionReason reason;

  public InvalidValueException(ExceptionReason reason) {
    this.reason = reason;
  }

  @Override
  public String toString() {
    return "InvalidValueException{" + "reason=" + this.reason + '}';
  }

  public ObjectNode toMap() {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode node = mapper.createObjectNode();

    node.put("reason", this.reason.toString());
    return node;
  }

  public ExceptionReason reason() {
    return this.reason;
  }
}
