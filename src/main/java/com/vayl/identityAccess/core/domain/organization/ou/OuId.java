package com.vayl.identityAccess.core.domain.organization.ou;

import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.IdValidator;

public class OuId {
  private String id;

  public OuId(String id) {
    this.setId(id);
  }

  private void setId(String id) {
    this.throwErrorOnInvalidId(id);
    this.id = id;
  }

  private void throwErrorOnInvalidId(String anId) {
    if (!IdValidator.isValid(anId)) {
      throw new InvalidValueException(
          ExceptionEvent.OU_ID_CREATION, ExceptionReason.INVALID_ID, anId, ExceptionLevel.ERROR);
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
    OuId orgId = (OuId) obj;
    return id.equals(orgId.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
