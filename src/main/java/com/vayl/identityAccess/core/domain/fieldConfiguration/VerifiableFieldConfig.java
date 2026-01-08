package com.vayl.identityAccess.core.domain.fieldConfiguration;

import com.vayl.identityAccess.core.domain.common.Date;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;

public class VerifiableFieldConfig implements FieldConfiguration {
  private FieldConfigId id;
  private FieldType fieldType;
  private boolean verificationRequirement;
  private Date enforcementDate;

  public VerifiableFieldConfig(
      FieldConfigId anId,
      FieldType aFieldType,
      boolean aVerificationRequirement,
      Date anEnforcementDate) {
    this.setId(anId);
    this.setFieldType(aFieldType);
    this.setVerificationRequirement(aVerificationRequirement);
    this.setEnforcementDate(anEnforcementDate);
  }

  private void setId(FieldConfigId anId) {
    this.id = anId;
  }

  private void setFieldType(FieldType aFieldType) {
    if (!isFieldTypeAllowed(aFieldType)) {
      throw new InvalidValueException("FIELDCONFIG_ID_CREATION", aFieldType.toString());
    }
    this.fieldType = aFieldType;
  }

  private boolean isFieldTypeAllowed(FieldType aFieldType) {
    return aFieldType == FieldType.EMAIL
        || aFieldType == FieldType.PHONE
        || aFieldType == FieldType.PASSCODE
        || aFieldType == FieldType.PRIMARY_EMAIL;
  }

  public void modify(Date anEnforcementDate, boolean aVerificationRequirement) {
    this.throwErrorOnPrimaryEmailViolation(anEnforcementDate);
    setEnforcementDate(anEnforcementDate);
    setVerificationRequirement(aVerificationRequirement);
  }

  private void throwErrorOnPrimaryEmailViolation(Date anEnforcementDate) {
    if (this.fieldType == FieldType.PRIMARY_EMAIL
        && !this.enforcementDate.equals(anEnforcementDate)) {
      throw new InvalidValueException(
          "FIELDCONFIG_OF_TYPE_PRIMARY_EMAIL_ENFORCEMENT_MODIFICATION",
          anEnforcementDate.toString());
    }
  }

  private void setEnforcementDate(Date anEnforcementDate) {
    this.enforcementDate = anEnforcementDate;
  }

  private void setVerificationRequirement(boolean aVerificationRequirement) {
    this.verificationRequirement = aVerificationRequirement;
  }

  public FieldConfigId id() {
    return this.id;
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
