package com.vayl.identityAccess.core.domain.fieldConfiguration;

import com.vayl.identityAccess.core.domain.common.Date;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;

public class UnverifiableFieldConfig implements FieldConfiguration {
  private FieldConfigId id;
  private FieldType fieldType;
  private Date enforcementDate;

  public UnverifiableFieldConfig(FieldConfigId id, FieldType fieldType, Date enforcementDate) {
    this.setId(id);
    this.setFieldType(fieldType);
    this.setEnforcementDate(enforcementDate);
  }

  private void setId(FieldConfigId id) {
    this.id = id;
  }

  private void setFieldType(FieldType fieldType) {
    if (!isFieldTypeAllowed(fieldType)) {
      throw new InvalidValueException(
          ExceptionEvent.UNVERIFIABLEFIELDCONFIG_CREATION,
          ExceptionReason.INVALID_FIELD_TYPE,
          fieldType.toString(),
          ExceptionLevel.INFO);
    }

    this.fieldType = fieldType;
  }

  private boolean isFieldTypeAllowed(FieldType fieldType) {
    return fieldType == FieldType.STRING
        || fieldType == FieldType.ADDRESS
        || fieldType == FieldType.DATE
        || fieldType == FieldType.USERNAME;
  }

  public void modify(Date enforcementDate) {
    this.setEnforcementDate(enforcementDate);
  }

  private void setEnforcementDate(Date enforcementDate) {
    this.enforcementDate = enforcementDate;
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
