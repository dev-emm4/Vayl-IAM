package com.vayl.identityAccess.core.domain.permission;

import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;

public class PermissionId {
  private ApiId permissionLocation;
  private String name;

  public PermissionId(ApiId anApiId, String aName) {
    this.setPermissionLocation(anApiId);
    this.setName(aName);
  }

  private void setPermissionLocation(ApiId permissionLocation) {
    this.permissionLocation = permissionLocation;
  }

  private void setName(String aName) {
    this.throwErrorOnEmptyString(aName);
    this.name = aName;
  }

  private void throwErrorOnEmptyString(String aString) {
    if (aString.isEmpty()) {
      throw new InvalidValueException("PERMISSION_ID_CREATION", aString);
    }
  }

  @Override
  public String toString() {
    return permissionLocation.toString() + "-" + this.name;
  }

  @Override
  public boolean equals(Object anObject) {

    boolean isEqual = false;
    if (anObject != null && this.getClass() == anObject.getClass()) {
      PermissionId typedObject = (PermissionId) anObject;
      isEqual = typedObject.toString().equals(this.toString());
    }

    return isEqual;
  }

  @Override
  public int hashCode() {
    return this.toString().hashCode();
  }
}
