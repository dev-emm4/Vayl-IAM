package com.vayl.identityAccess.core.domain.api;

import com.vayl.identityAccess.core.domain.api.permission.Permission;
import com.vayl.identityAccess.core.domain.api.permission.PermissionId;
import com.vayl.identityAccess.core.domain.api.role.CustomRole;
import com.vayl.identityAccess.core.domain.api.role.DefaultRole;
import com.vayl.identityAccess.core.domain.api.role.RoleId;
import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.NonNull;

public class Api {
  private ApiId apiId;
  private String name;

  public Api(@NonNull ApiId apiId, @NonNull String name) {
    this.setId(apiId);
    this.setName(name);
  }

  private void setId(ApiId apiId) {
    AssertionConcern.isNotNull(apiId, ExceptionReason.INVALID_API_ID);
    this.apiId = apiId;
  }

  private void setName(String name) {
    AssertionConcern.isNotNull(name, ExceptionReason.INVALID_API_NAME);
    AssertionConcern.isNotBlank(name, ExceptionReason.INVALID_API_NAME);
    this.name = name;
  }

  public @NonNull Permission createPermission(@NonNull String name, String description) {
    PermissionId permissionId = new PermissionId(this.apiId, name);
    return new Permission(permissionId, description);
  }

  public @NonNull DefaultRole createDefaultRole(
      @NonNull String name, @NonNull List<PermissionId> permissionIds) {
    return new DefaultRole(
        new RoleId(UUID.randomUUID().toString()), name, this.id(), permissionIds);
  }

  public @NonNull CustomRole createCustomRole(
      @NonNull String name, @NonNull OrgId orgId, @NonNull List<PermissionId> permissionIds) {
    return new CustomRole(
        orgId, new RoleId(UUID.randomUUID().toString()), name, this.id(), permissionIds);
  }

  public @NonNull ApiId id() {
    return this.apiId;
  }

  public @NonNull String name() {
    return this.name;
  }
}
