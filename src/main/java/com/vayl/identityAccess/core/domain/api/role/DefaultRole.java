package com.vayl.identityAccess.core.domain.api.role;

import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.api.permission.PermissionId;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.NonNull;

public class DefaultRole implements Role {
  RoleId id;
  String name;
  ApiId assignedApiIds;
  Set<PermissionId> assignedPermissionIds = new HashSet<>();

  public DefaultRole(RoleId id, String name, ApiId apiId, List<PermissionId> assignedPermissionIds) {
    this.setId(id);
    this.setName(name);
    this.assignApi(apiId);
    this.assignPermissionIds(assignedPermissionIds);
  }

  private void setId(RoleId id) {
    this.id = id;
  }

  private void setName(String name) {
    this.name = name;
  }

  private void assignApi(ApiId assignApi) {
    this.assignedApiIds = assignApi;
  }

  public void modifyGrantedPermissions(
      @NonNull List<PermissionId> addPermissionIds,
      @NonNull List<PermissionId> removePermissionIds) {

    this.assignPermissionIds(addPermissionIds);
    this.removeGrantedPermissions(removePermissionIds);
  }

  private void assignPermissionIds(@NonNull List<PermissionId> addPermission) {
    for (PermissionId permissionId : addPermission) {
      this.throwErrorIfPermissionNotLocatedInAssignedApi(permissionId);
      this.assignedPermissionIds.add(permissionId);
    }
  }

  private void throwErrorIfPermissionNotLocatedInAssignedApi(@NonNull PermissionId permissionId) {
    if (permissionId.permissionLocation() != this.assignedApiIds()) {
      throw new InvalidValueException(
          ExceptionEvent.DEFAULT_ROLE_PERMISSION_MODIFICATION,
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
          ExceptionEvent.DEFAULT_ROLE_PERMISSION_MODIFICATION,
          ExceptionReason.REMOVING_UNASSIGNED_PERMISSION,
          permissionId.toString(),
          ExceptionLevel.INFO);
    }
  }

  public RoleId id() {
    return this.id;
  }

  public String name() {
    return this.name;
  }

  public ApiId assignedApiIds() {
    return this.assignedApiIds;
  }

  public boolean belongsTo(OrgId orgId) {
    return true;
  }

  public Set<PermissionId> assignedPermissionIds() {
    return this.assignedPermissionIds;
  }
}
