package com.vayl.identityAccess.core.domain.fieldConfiguration;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.inputtableValue.DateInput;
import com.vayl.identityAccess.core.domain.common.validator.VerifiableFieldTypeValidator;

public class VerifiableFieldConfig implements FieldConfiguration {
  private FieldConfigId id;
  private FieldType fieldType;
  private boolean verificationRequirement;
  private DateInput enforcementDateInput;

  public VerifiableFieldConfig(
      FieldConfigId id,
      FieldType fieldType,
      boolean verificationRequirement,
      DateInput enforcementDateInput) {
    this.setId(id);
    this.setFieldType(fieldType);
    this.setVerificationRequirement(verificationRequirement);
    this.setEnforcementDate(enforcementDateInput);
  }

  private void setId(FieldConfigId id) {
    AssertionConcern.isNotNull(id, ExceptionReason.INVALID_FIELD_CONFIG_ARG);
    this.id = id;
  }

  private void setFieldType(FieldType fieldType) {
    AssertionConcern.isNotNull(fieldType, ExceptionReason.INVALID_FIELD_CONFIG_ARG);
    AssertionConcern.isValid(
        new VerifiableFieldTypeValidator(),
        fieldType.toString(),
        ExceptionReason.INVALID_FIELD_CONFIG_ARG);

    this.fieldType = fieldType;
  }

  public void modify(DateInput enforcementDateInput, boolean verificationRequirement) {
    AssertionConcern.isFalse(
        this.isPrimaryEmailEnforcementDateBeingUpdated(enforcementDateInput),
        ExceptionReason.INVALID_FIELD_CONFIG_ARG);

    setEnforcementDate(enforcementDateInput);
    setVerificationRequirement(verificationRequirement);
  }

  private boolean isPrimaryEmailEnforcementDateBeingUpdated(DateInput enforcementDateInput) {
    return this.fieldName().equalsIgnoreCase("PRIMARY_EMAIL")
        && !this.enforcementDateInput.equals(enforcementDateInput);
  }

  private void setEnforcementDate(DateInput enforcementDateInput) {
    AssertionConcern.isNotNull(enforcementDateInput, ExceptionReason.INVALID_FIELD_CONFIG_ARG);
    this.enforcementDateInput = enforcementDateInput;
  }

  private void setVerificationRequirement(boolean verificationRequirement) {
    this.verificationRequirement = verificationRequirement;
  }

  public FieldConfigId id() {
    return this.id;
  }

  public String fieldName() {
    return this.id.toString();
  }

  public DateInput enforcementDate() {
    return this.enforcementDateInput;
  }

  public boolean isVerifiable() {
    return this.verificationRequirement;
  }

  public FieldType fieldType() {
    return this.fieldType;
  }
}
