package com.vayl.identityAccess.core.domain.fieldConfiguration;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.Schedule;
import com.vayl.identityAccess.core.domain.common.validator.VerifiableFieldTypeValidator;
import org.jspecify.annotations.NonNull;

public class VerifiableFieldConfig implements FieldConfiguration {
  private FieldConfigId id;
  private FieldType fieldType;
  private Boolean verificationRequirement;
  private Schedule enforcementDate;

  public VerifiableFieldConfig(
      @NonNull FieldConfigId id,
      @NonNull FieldType fieldType,
      @NonNull Boolean verificationRequirement,
      @NonNull Schedule enforcementDate) {
    this.setId(id);
    this.setFieldType(fieldType);
    this.setVerificationRequirement(verificationRequirement);
    this.setEnforcementDate(enforcementDate);
  }

  private void setId(FieldConfigId id) {
    AssertionConcern.isNotNull(id, ExceptionReason.INVALID_FIELD_CONFIG_ID);
    this.id = id;
  }

  private void setFieldType(FieldType fieldType) {
    AssertionConcern.isNotNull(fieldType, ExceptionReason.INVALID_FIELD_CONFIG_TYPE);
    AssertionConcern.isValid(
        new VerifiableFieldTypeValidator(),
        fieldType.toString(),
        ExceptionReason.INVALID_FIELD_CONFIG_TYPE);

    this.fieldType = fieldType;
  }

  public void modify(@NonNull Schedule enforcementDate, @NonNull Boolean verificationRequirement) {
    AssertionConcern.isFalse(
        this.isPrimaryEmailEnforcementDateBeingUpdated(enforcementDate),
        ExceptionReason.INVALID_ENFORCEMENT_DATE);

    setEnforcementDate(enforcementDate);
    setVerificationRequirement(verificationRequirement);
  }

  private boolean isPrimaryEmailEnforcementDateBeingUpdated(Schedule enforcementDate) {
    return this.fieldName().equalsIgnoreCase("PRIMARY_EMAIL")
        && !this.enforcementDate.equals(enforcementDate);
  }

  private void setEnforcementDate(Schedule enforcementDate) {
    AssertionConcern.isNotNull(enforcementDate, ExceptionReason.INVALID_ENFORCEMENT_DATE);
    this.enforcementDate = enforcementDate;
  }

  private void setVerificationRequirement(Boolean verificationRequirement) {
    AssertionConcern.isNotNull(verificationRequirement, ExceptionReason.INVALID_VERIFICATION_REQUIREMENT);
    this.verificationRequirement = verificationRequirement;
  }

  public @NonNull FieldConfigId id() {
    return this.id;
  }

  public @NonNull String fieldName() {
    return this.id.toString();
  }

  public @NonNull Schedule enforcementDate() {
    return this.enforcementDate;
  }

  public boolean isVerifiable() {
    return this.verificationRequirement;
  }

  public @NonNull FieldType fieldType() {
    return this.fieldType;
  }
}
