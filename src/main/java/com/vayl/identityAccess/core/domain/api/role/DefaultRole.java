package com.vayl.identityAccess.core.domain.api.role;

import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.api.permission.PermissionId;
import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.NonNull;

public class DefaultRole implements Role {
  private RoleId id;
  private String name;
  private ApiId assignedApiId;
  private final Set<PermissionId> assignedPermissionIds = new HashSet<>();

  public DefaultRole(
      @NonNull RoleId id,
      @NonNull String name,
      @NonNull ApiId apiId,
      @NonNull List<PermissionId> assignedPermissionIds) {
    this.setId(id);
    this.setName(name);
    this.setApiId(apiId);
    this.assignPermissionIds(assignedPermissionIds);
  }

  private void setId(RoleId id) {
    AssertionConcern.isNotNull(id, ExceptionReason.INVALID_ROLE_ARG);
    this.id = id;
  }

  private void setName(String name) {
    AssertionConcern.isNotNull(name, ExceptionReason.INVALID_ROLE_ARG);
    AssertionConcern.isNotBlank(name, ExceptionReason.INVALID_ROLE_ARG);
    this.name = name;
  }

  private void setApiId(ApiId assignApi) {
    AssertionConcern.isNotNull(assignApi, ExceptionReason.INVALID_ROLE_ARG);
    this.assignedApiId = assignApi;
  }

  public void modifyGrantedPermissions(
      @NonNull List<PermissionId> addPermissionIds,
      @NonNull List<PermissionId> removePermissionIds) {

    this.assignPermissionIds(addPermissionIds);
    this.removeGrantedPermissions(removePermissionIds);
  }

  private void assignPermissionIds(@NonNull List<PermissionId> permissionIds) {
    AssertionConcern.isNotNull(permissionIds, ExceptionReason.INVALID_ROLE_ARG);

    for (PermissionId permissionId : permissionIds) {
      AssertionConcern.isEqual(
          permissionId.apiId(), this.assignedApiId(), ExceptionReason.INVALID_ROLE_ARG);
      this.assignedPermissionIds.add(permissionId);
    }
  }

  private void removeGrantedPermissions(@NonNull List<PermissionId> permissionIds) {
    AssertionConcern.isNotNull(permissionIds, ExceptionReason.INVALID_ROLE_ARG);

    for (PermissionId permissionId : permissionIds) {
      AssertionConcern.isTrue(
          this.isPermissionIdAssigned(permissionId), ExceptionReason.INVALID_ROLE_ARG);
      this.assignedPermissionIds.remove(permissionId);
    }
  }

  private boolean isPermissionIdAssigned(@NonNull PermissionId permissionId) {
    return this.assignedPermissionIds().contains(permissionId);
  }

  public @NonNull RoleId id() {
    return this.id;
  }

  public @NonNull String name() {
    return this.name;
  }

  public @NonNull ApiId assignedApiId() {
    return this.assignedApiId;
  }

  public boolean accessibleBy(OrgId orgId) {
    return true;
  }

  public @NonNull Set<PermissionId> assignedPermissionIds() {
    return this.assignedPermissionIds;
  }
}
