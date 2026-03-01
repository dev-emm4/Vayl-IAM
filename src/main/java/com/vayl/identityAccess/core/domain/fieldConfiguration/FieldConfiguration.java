package com.vayl.identityAccess.core.domain.fieldConfiguration;

import com.vayl.identityAccess.core.domain.common.inputtableValue.DateInput;

public interface FieldConfiguration {
  FieldConfigId id();

  String fieldName();

  boolean isVerifiable();

  DateInput enforcementDate();

  FieldType fieldType();
}
