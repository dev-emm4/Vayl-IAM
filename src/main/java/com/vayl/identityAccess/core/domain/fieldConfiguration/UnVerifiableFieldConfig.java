package com.vayl.identityAccess.core.domain.fieldConfiguration;

import com.vayl.identityAccess.core.domain.common.Date;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;

public class UnVerifiableFieldConfig implements FieldConfiguration {
  private FieldConfigId id;
  private FieldType fieldType;
  private Date enforcementDate;

  public UnVerifiableFieldConfig(FieldConfigId anId, FieldType aFieldType, Date anEnforcementDate) {
    this.setId(anId);
    this.setFieldType(aFieldType);
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
    return aFieldType == FieldType.STRING
        || aFieldType == FieldType.ADDRESS
        || aFieldType == FieldType.DATE
        || aFieldType == FieldType.USERNAME;
  }

  public void modify(Date anEnforcementDate) {
    this.setEnforcementDate(anEnforcementDate);
  }

  private void setEnforcementDate(Date anEnforcementDate) {
    this.enforcementDate = anEnforcementDate;
  }

  public Date enforcementDate() {
    return this.enforcementDate;
  }

  public FieldType fieldType() {
    return this.fieldType;
  }

  public FieldConfigId id() {
    return this.id;
  }

  public boolean isVerifiable() {
    return false;
  }
}
