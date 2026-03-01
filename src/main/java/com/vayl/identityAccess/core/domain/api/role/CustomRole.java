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

public class CustomRole implements Role {
  private OrgId orgId;
  private RoleId id;
  private String name;
  private ApiId apiId;
  private final Set<PermissionId> assignedPermissionIds = new HashSet<PermissionId>();

  public CustomRole(
      @NonNull OrgId orgId,
      @NonNull RoleId id,
      @NonNull String name,
      @NonNull ApiId apiId,
      @NonNull List<PermissionId> assignedPermissionIds) {
    this.setOrgId(orgId);
    this.setId(id);
    this.setName(name);
    this.setApiId(apiId);
    this.assignPermissionIds(assignedPermissionIds);
  }

  private void setOrgId(OrgId orgId) {
    AssertionConcern.isNotNull(orgId, ExceptionReason.INVALID_ROLE_ARG);
    this.orgId = orgId;
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

  private void setApiId(ApiId apiId) {
    AssertionConcern.isNotNull(apiId, ExceptionReason.INVALID_ROLE_ARG);
    this.apiId = apiId;
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

  @Override
  public ApiId assignedApiId() {
    return this.apiId;
  }

  @Override
  public boolean accessibleBy(OrgId orgId) {
    return this.orgId.equals(orgId);
  }

  @Override
  public RoleId id() {
    return this.id;
  }

  public String name() {
    return this.name;
  }

  public Set<PermissionId> assignedPermissionIds() {
    return this.assignedPermissionIds;
  }
}
