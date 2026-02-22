package com.vayl.identityAccess.core.domain.common.DomainErrors.inputViolation;

import com.vayl.identityAccess.core.domain.common.DomainErrors.DomainException;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

public class InvalidValueException extends RuntimeException implements DomainException {
  private final ExceptionReason reason;
  private final String invalidValue;

  public InvalidValueException(ExceptionReason reason, String invalidValue) {
    this.reason = reason;
    this.invalidValue = invalidValue;
  }

  @Override
  public String toString() {
    return "InvalidValueException{"
        + "reason="
        + this.reason
        + ", invalidValue='"
        + this.invalidValue
        + '}';
  }

  public ObjectNode toMap() {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode node = mapper.createObjectNode();

    node.put("fieldName", this.reason.toString());
    node.put("invalidValue", this.invalidValue);
    return node;
  }

  public String invalidValue() {
    return this.invalidValue;
  }

  public ExceptionReason reason() {
    return this.reason;
  }
}
