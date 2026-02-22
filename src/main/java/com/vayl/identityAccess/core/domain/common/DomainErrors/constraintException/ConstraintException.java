package com.vayl.identityAccess.core.domain.common.DomainErrors.constraintException;

import com.vayl.identityAccess.core.domain.common.DomainErrors.DomainException;
import java.util.ArrayList;
import java.util.List;

import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

public class ConstraintException extends RuntimeException implements DomainException {
  ExceptionReason exceptionReason;
  private List<Participant> participants = new ArrayList<>();

  public ConstraintException(
          ExceptionReason exceptionReason, List<Participant> participants) {
    this.exceptionReason = exceptionReason;
    this.participants = participants;
  }

  public void addParticipants(List<Participant> participants) {
    this.participants = participants;
  }

  @Override
  public String toString() {
    return "ConstraintViolation{"
        + "constraintType= "
        + this.exceptionReason.toString()
        + "participants= "
        + this.participants.toString()
        + "}";
  }

  public ObjectNode toMap() {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode node = mapper.createObjectNode();

    node.put("participants", this.participants.toString());

    return node;
  }

  public ExceptionReason constraintViolation() {
    return exceptionReason;
  }

  public List<Participant> participants() {
    return this.participants;
  }
}
