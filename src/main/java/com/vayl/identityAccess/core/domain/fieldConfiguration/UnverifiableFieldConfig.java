package com.vayl.identityAccess.core.domain.fieldConfiguration;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.Schedule;
import com.vayl.identityAccess.core.domain.common.validator.UnverifiableFieldTypeValidator;
import org.jspecify.annotations.NonNull;

public class UnverifiableFieldConfig implements FieldConfiguration {
  private FieldConfigId id;
  private FieldType fieldType;
  private Schedule enforcementDate;

  public UnverifiableFieldConfig(
      @NonNull FieldConfigId id, @NonNull FieldType fieldType, @NonNull Schedule enforcementDate) {
    this.setId(id);
    this.setFieldType(fieldType);
    this.setEnforcementDate(enforcementDate);
  }

  private void setId(FieldConfigId id) {
    AssertionConcern.isNotNull(id, ExceptionReason.INVALID_FIELD_CONFIG_ID);
    this.id = id;
  }

  private void setFieldType(FieldType fieldType) {
    AssertionConcern.isNotNull(fieldType, ExceptionReason.INVALID_FIELD_CONFIG_TYPE);
    AssertionConcern.isValid(
        new UnverifiableFieldTypeValidator(),
        fieldType.toString(),
        ExceptionReason.INVALID_FIELD_CONFIG_TYPE);

    this.fieldType = fieldType;
  }

  public void modify(@NonNull Schedule enforcementDate) {
    this.setEnforcementDate(enforcementDate);
  }

  private void setEnforcementDate(Schedule enforcementDate) {
    AssertionConcern.isNotNull(enforcementDate, ExceptionReason.INVALID_ENFORCEMENT_DATE);
    this.enforcementDate = enforcementDate;
  }

  public @NonNull Schedule enforcementDate() {
    return this.enforcementDate;
  }

  public @NonNull FieldType fieldType() {
    return this.fieldType;
  }

  public @NonNull String fieldName() {
    return this.id().toString();
  }

  public @NonNull FieldConfigId id() {
    return this.id;
  }

  public boolean isVerifiable() {
    return false;
  }
}
