package com.vayl.identityAccess.core.domain.organization.registrationSession.regPhase;

import org.jspecify.annotations.NonNull;

import java.util.List;

public class MfaRegPhase {
  List<FieldRegistration> fieldRegistrations;
  Status status;
  boolean isRequired;

  public MfaRegPhase(
      @NonNull List<FieldRegistration> fieldRegistrations,
      @NonNull Status status,
      boolean isRequired) {
    this.setFieldRegistrations(fieldRegistrations);
    this.setStatus(status);
    this.setRequired(isRequired);
  }

  private void setFieldRegistrations(List<FieldRegistration> fieldRegistrations) {
    this.fieldRegistrations = fieldRegistrations;
  }

  private void setStatus(Status status) {
    this.status = status;
  }

  private void setRequired(boolean isRequired) {
    this.isRequired = isRequired;
  }
}
