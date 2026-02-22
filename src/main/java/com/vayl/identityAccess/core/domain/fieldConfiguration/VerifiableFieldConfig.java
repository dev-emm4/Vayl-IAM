package com.vayl.identityAccess.core.domain.fieldConfiguration;

import com.vayl.identityAccess.core.domain.common.Date;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.inputViolation.InvalidValueException;

public class VerifiableFieldConfig implements FieldConfiguration {
  private FieldConfigId id;
  private FieldType fieldType;
  private boolean verificationRequirement;
  private Date enforcementDate;

  public VerifiableFieldConfig(
      FieldConfigId id,
      FieldType fieldType,
      boolean verificationRequirement,
      Date enforcementDate) {
    this.setId(id);
    this.setFieldType(fieldType);
    this.setVerificationRequirement(verificationRequirement);
    this.setEnforcementDate(enforcementDate);
  }

  private void setId(FieldConfigId id) {
    this.id = id;
  }

  private void setFieldType(FieldType fieldType) {
    if (!isFieldTypeAllowed(fieldType)) {
      throw new InvalidValueException(ExceptionReason.INVALID_FIELD_TYPE, fieldType.toString());
    }
    this.fieldType = fieldType;
  }

  private boolean isFieldTypeAllowed(FieldType fieldType) {
    return fieldType == FieldType.EMAIL
        || fieldType == FieldType.PHONE
        || fieldType == FieldType.PASSCODE;
  }

  public void modify(Date enforcementDate, boolean verificationRequirement) {
    this.throwErrorOnPrimaryEmailViolation(enforcementDate);
    setEnforcementDate(enforcementDate);
    setVerificationRequirement(verificationRequirement);
  }

  private void throwErrorOnPrimaryEmailViolation(Date enforcementDate) {
    if (this.fieldName().equalsIgnoreCase("PRIMARY_EMAIL")
        && !this.enforcementDate.equals(enforcementDate)) {
      throw new InvalidValueException(
          ExceptionReason.UPDATING_ENFORCEMENT_DATE_IN_PRIMARY_EMAIL_FIELD_CONFIG,
          enforcementDate.toString());
    }
  }

  private void setEnforcementDate(Date enforcementDate) {
    this.enforcementDate = enforcementDate;
  }

  private void setVerificationRequirement(boolean verificationRequirement) {
    this.verificationRequirement = verificationRequirement;
  }

  public FieldConfigId id() {
    return this.id;
  }

  public java.lang.String fieldName() {
    return this.id.toString();
  }

  public Date enforcementDate() {
    return this.enforcementDate;
  }

  public boolean isVerifiable() {
    return this.verificationRequirement;
  }

  public FieldType fieldType() {
    return this.fieldType;
  }
}
