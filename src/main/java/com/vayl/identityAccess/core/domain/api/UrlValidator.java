package com.vayl.identityAccess.core.domain.api;

import org.apache.commons.validator.routines.DomainValidator;

public class UrlValidator {
  private final DomainValidator validator = DomainValidator.getInstance();

  public boolean isValid(String aDomain) {
    return this.validator.isValid(aDomain);
  }
}
