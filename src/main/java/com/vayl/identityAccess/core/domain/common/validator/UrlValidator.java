package com.vayl.identityAccess.core.domain.common.validator;

import org.apache.commons.validator.routines.DomainValidator;
import org.jspecify.annotations.NonNull;

public class UrlValidator implements Validator {
  private final DomainValidator validator = DomainValidator.getInstance();

  public boolean isValid(@NonNull String aDomain) {
    return this.validator.isValid(aDomain);
  }
}
