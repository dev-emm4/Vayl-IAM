package com.vayl.identityAccess.core.domain.api.permission;

import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;

public class PermissionId {
  private ApiId permissionLocation;
  private String name;

  public PermissionId(ApiId id, String name) {
    this.setPermissionLocation(id);
    this.setName(name);
  }

  private void setPermissionLocation(ApiId permissionLocation) {
    this.permissionLocation = permissionLocation;
  }

  private void setName(String string) {
    this.throwErrorOnBlankString(string);
    this.name = string;
  }

  private void throwErrorOnBlankString(String string) {
    if (string.isBlank()) {
      throw new InvalidValueException(
          ExceptionEvent.PERMISSION_ID_CREATION,
          ExceptionReason.BLANK_PERMISSION_NAME_PROVIDED,
          string,
          ExceptionLevel.INFO);
    }
  }

  @Override
  public String toString() {
    return permissionLocation.toString() + "-" + this.name;
  }

  @Override
  public boolean equals(Object object) {

    boolean isEqual = false;
    if (object != null && this.getClass() == object.getClass()) {
      PermissionId typedObject = (PermissionId) object;
      isEqual = typedObject.toString().equals(this.toString());
    }

    return isEqual;
  }

  @Override
  public int hashCode() {
    return this.toString().hashCode();
  }

  public ApiId permissionLocation() {
    return this.permissionLocation;
  }

  public String name() {
    return this.name;
  }
}
