package com.vayl.identityAccess.core.domain.organization;

import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.inputViolation.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.IdValidator;

public class OrgId {
  private String id;

  public OrgId(String id) {
    this.setId(id);
  }

  private void setId(String id) {
    this.throwErrorOnInvalidId(id);
    this.id = id;
  }

  private void throwErrorOnInvalidId(String id) {
    if (!IdValidator.isValid(id)) {
      throw new InvalidValueException(ExceptionReason.INVALID_ORG_ID, id);
    }
  }

  @Override
  public String toString() {
    return this.id;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    OrgId orgId = (OrgId) obj;
    return id.equals(orgId.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
