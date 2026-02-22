package com.vayl.identityAccess.core.domain.api;

import com.vayl.identityAccess.core.domain.api.permission.Permission;
import com.vayl.identityAccess.core.domain.api.permission.PermissionId;
import com.vayl.identityAccess.core.domain.api.role.CustomRole;
import com.vayl.identityAccess.core.domain.api.role.DefaultRole;
import com.vayl.identityAccess.core.domain.api.role.RoleId;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.inputViolation.InvalidValueException;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.NonNull;

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

  public Permission createPermission(@NonNull String name, String description) {
    PermissionId permissionId = new PermissionId(this.id, name);
    return new Permission(permissionId, description);
  }

  public DefaultRole createDefaultRole(String name, @NonNull List<PermissionId> permissionIds) {
    this.throwErrorIfPermissionNotLocatedInApi(permissionIds);

    RoleId roleId = new RoleId(UUID.randomUUID().toString());
    return new DefaultRole(roleId, name, this.id(), permissionIds);
  }

  public CustomRole createCustomRole(
      @NonNull String name, @NonNull OrgId orgId, @NonNull List<PermissionId> permissionIds) {
    this.throwErrorIfPermissionNotLocatedInApi(permissionIds);

    return new CustomRole(
        orgId, new RoleId(UUID.randomUUID().toString()), name, this.id(), permissionIds);
  }

  private void throwErrorIfPermissionNotLocatedInApi(@NonNull List<PermissionId> permissionIds) {
    for (PermissionId permissionId : permissionIds) {
      if (permissionId.permissionLocation() != this.id()) {
        throw new InvalidValueException(
            ExceptionReason.ASSIGNING_UNAUTHORIZED_PERMISSION_TO_ROLE, permissionId.toString());
      }
    }
  }

  public ApiId id() {
    return this.id;
  }

  public String name() {
    return this.name;
  }
}
