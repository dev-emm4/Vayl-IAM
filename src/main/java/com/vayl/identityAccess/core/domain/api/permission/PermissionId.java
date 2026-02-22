package com.vayl.identityAccess.core.domain.api.permission;

import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.api.LicenseRestrictable;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.inputViolation.InvalidValueException;
import org.jspecify.annotations.NonNull;

public class PermissionId implements LicenseRestrictable {
  private ApiId permissionLocation;
  private String name;

  public PermissionId(ApiId id, @NonNull String name) {
    this.setPermissionLocation(id);
    this.setName(name);
  }

  private void setPermissionLocation(ApiId permissionLocation) {
    this.permissionLocation = permissionLocation;
  }

  private void setName(@NonNull String name) {
    this.throwErrorOnBlankName(name);
    this.name = name;
  }

  private void throwErrorOnBlankName(@NonNull String name) {
    if (name.isBlank()) {
      throw new InvalidValueException(ExceptionReason.INVALID_PERMISSION_ID, name);
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
