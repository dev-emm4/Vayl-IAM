package com.vayl.identityAccess.core.domain.fieldConfiguration;

import com.vayl.identityAccess.core.domain.common.Schedule;

public interface FieldConfiguration {
  FieldConfigId id();

  String fieldName();

  boolean isVerifiable();

  Schedule enforcementDate();

  FieldType fieldType();
}
