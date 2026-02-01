package com.vayl.identityAccess.core.domain.fieldConfiguration;

import com.vayl.identityAccess.core.domain.common.Date;

public interface FieldConfiguration {
  FieldConfigId id();

  String fieldName();

  boolean isVerifiable();

  Date enforcementDate();

  FieldType fieldType();
}
