package com.vayl.identityAccess.core.domain.api.role;

import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.api.permission.PermissionId;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import java.util.List;
import org.jspecify.annotations.NonNull;

public class CustomRole implements Role {
  OrgId orgId;
  RoleId id;
  String name;
  ApiId apiId;
  List<PermissionId> assignedPermissionIds;

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
    this.setAssignedPermissionIds(assignedPermissionIds);
  }

  private void setOrgId(OrgId orgId) {
    this.orgId = orgId;
  }

  private void setId(RoleId id) {
    this.id = id;
  }

  private void setName(String name) {
    this.name = name;
  }

  private void setApiId(ApiId apiId) {
    this.apiId = apiId;
  }

  private void setAssignedPermissionIds(List<PermissionId> assignedPermissionIds) {
    this.assignedPermissionIds = assignedPermissionIds;
  }

  public void modifyGrantedPermissions(
      @NonNull List<PermissionId> addPermissionIds,
      @NonNull List<PermissionId> removePermissionIds) {

    this.assignPermission(addPermissionIds);
    this.removeGrantedPermissions(removePermissionIds);
  }

  private void assignPermission(@NonNull List<PermissionId> addPermission) {
    for (PermissionId permissionId : addPermission) {
      this.throwErrorIfPermissionNotLocatedInAssignedApi(permissionId);
      this.assignedPermissionIds().add(permissionId);
    }
  }

  private void throwErrorIfPermissionNotLocatedInAssignedApi(@NonNull PermissionId permissionId) {
    if (permissionId.permissionLocation() != this.assignedApiIds()) {
      throw new InvalidValueException(
          ExceptionEvent.CUSTOM_ROLE_PERMISSION_MODIFICATION,
          ExceptionReason.GRANTED_PERMISSION_NOT_LOCATED_IN_API,
          permissionId.toString(),
          ExceptionLevel.INFO);
    }
  }

  private void removeGrantedPermissions(@NonNull List<PermissionId> removePermissions) {
    for (PermissionId permissionId : removePermissions) {
      this.throwErrorIfRemovePermissionNotAssigned(permissionId);
      this.assignedPermissionIds.remove(permissionId);
    }
  }

  private void throwErrorIfRemovePermissionNotAssigned(@NonNull PermissionId permissionId) {
    if (!this.assignedPermissionIds().contains(permissionId)) {
      throw new InvalidValueException(
          ExceptionEvent.CUSTOM_ROLE_PERMISSION_MODIFICATION,
          ExceptionReason.REMOVING_UNASSIGNED_PERMISSION,
          permissionId.toString(),
          ExceptionLevel.INFO);
    }
  }

  @Override
  public ApiId assignedApiIds() {
    return this.apiId;
  }

  @Override
  public boolean belongsTo(OrgId orgId) {
    return this.orgId.equals(orgId);
  }

  @Override
  public RoleId id() {
    return this.id;
  }

  public String name() {
    return this.name;
  }

  public List<PermissionId> assignedPermissionIds() {
    return this.assignedPermissionIds;
  }
}
