package com.vayl.identityAccess.core.domain.role;

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
  ApiId assignedApi;
  Set<PermissionId> grantedPermissionIds = new HashSet<>();

  public DefaultRole(RoleId id, String name, ApiId apiId, List<PermissionId> grantedPermissionIds) {
    this.setId(id);
    this.setName(name);
    this.assignApi(apiId);
    this.assignPermission(grantedPermissionIds);
  }

  private void setId(RoleId id) {
    this.id = id;
  }

  private void setName(String name) {
    this.name = name;
  }

  private void assignApi(ApiId assignApi) {
    this.assignedApi = assignApi;
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
      this.grantedPermissionIds.add(permissionId);
    }
  }

  private void throwErrorIfPermissionNotLocatedInAssignedApi(@NonNull PermissionId permissionId) {
    if (permissionId.permissionLocation() != this.assignedApi()) {
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
      this.grantedPermissionIds.remove(permissionId);
    }
  }

  private void throwErrorIfRemovePermissionNotAssigned(@NonNull PermissionId permissionId) {
    if (!this.grantedPermissionIds().contains(permissionId)) {
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

  public ApiId assignedApi() {
    return this.assignedApi;
  }

  public boolean belongsTo(OrgId orgId) {
    return true;
  }

  public Set<PermissionId> grantedPermissionIds() {
    return this.grantedPermissionIds;
  }
}
