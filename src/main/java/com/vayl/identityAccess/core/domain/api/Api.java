package com.vayl.identityAccess.core.domain.api;

import com.vayl.identityAccess.core.domain.api.permission.Permission;
import com.vayl.identityAccess.core.domain.api.permission.PermissionId;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.role.DefaultRole;
import com.vayl.identityAccess.core.domain.role.RoleId;
import java.util.List;
import java.util.UUID;

public class Api {
  private ApiId id;
  private String name;

  public Api(ApiId id, String name) {
    this.setId(id);
    this.setName(name);
  }

  private void setId(ApiId id) {
    this.id = id;
  }

  private void setName(String name) {
    this.name = name;
  }

  public Permission createPermission(String name, String description) {
    PermissionId permissionId = new PermissionId(this.id, name);
    return new Permission(permissionId, description);
  }

  public DefaultRole createDefaultRole(String name, List<PermissionId> assignPermissions) {
    if (!this.checkIfPermissionIsLocatedInApi(assignPermissions)) {
      throw new InvalidValueException(
          ExceptionEvent.DEFAULT_ROLE_CREATION,
          ExceptionReason.GRANTED_PERMISSION_NOT_LOCATED_IN_API,
          null,
          ExceptionLevel.INFO);
    }

    RoleId roleId = new RoleId(UUID.randomUUID().toString());
    return new DefaultRole(roleId, name, this.id(), assignPermissions);
  }

  private boolean checkIfPermissionIsLocatedInApi(List<PermissionId> permissionsToCheck) {
    for (PermissionId permissionId : permissionsToCheck) {
      if (permissionId.permissionLocation() != this.id()) {
        return false;
      }
    }
    return true;
  }

  public ApiId id() {
    return this.id;
  }

  public String name() {
    return this.name;
  }
}
