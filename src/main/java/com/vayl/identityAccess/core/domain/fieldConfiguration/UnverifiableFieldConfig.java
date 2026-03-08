package com.vayl.identityAccess.core.domain.fieldConfiguration;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.inputtableValue.DateInput;
import com.vayl.identityAccess.core.domain.common.validator.UnverifiableFieldTypeValidator;
import org.jspecify.annotations.NonNull;

public class UnverifiableFieldConfig implements FieldConfiguration {
  private FieldConfigId id;
  private FieldType fieldType;
  private DateInput enforcementDateInput;

  public UnverifiableFieldConfig(
      @NonNull FieldConfigId id,
      @NonNull FieldType fieldType,
      @NonNull DateInput enforcementDateInput) {
    this.setId(id);
    this.setFieldType(fieldType);
    this.setEnforcementDate(enforcementDateInput);
  }

  private void setId(FieldConfigId id) {
    AssertionConcern.isNotNull(id, ExceptionReason.INVALID_FIELD_CONFIG_ARG);
    this.id = id;
  }

  private void setFieldType(FieldType fieldType) {
    AssertionConcern.isNotNull(fieldType, ExceptionReason.INVALID_FIELD_CONFIG_ARG);
    AssertionConcern.isValid(
        new UnverifiableFieldTypeValidator(),
        fieldType.toString(),
        ExceptionReason.INVALID_FIELD_CONFIG_ARG);

    this.fieldType = fieldType;
  }

  public void modify(@NonNull DateInput enforcementDateInput) {
    this.setEnforcementDate(enforcementDateInput);
  }

  private void setEnforcementDate(DateInput enforcementDateInput) {
    AssertionConcern.isNotNull(enforcementDateInput, ExceptionReason.INVALID_FIELD_CONFIG_ARG);
    this.enforcementDateInput = enforcementDateInput;
  }

  public @NonNull DateInput enforcementDate() {
    return this.enforcementDateInput;
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
