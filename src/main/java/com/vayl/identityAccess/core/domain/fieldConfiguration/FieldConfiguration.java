package com.vayl.identityAccess.core.domain.fieldConfiguration;

import com.vayl.identityAccess.core.domain.common.Date;

public interface FieldConfiguration {
    boolean isVerifiable();
    Date enforcementDate();
    FieldType fieldType();
}
