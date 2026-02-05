package com.vayl.identityAccess.core.domain.role;

import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.api.permission.PermissionId;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultRole implements Role {
  RoleId id;
  String name;
  ApiId assignedApi;
  List<PermissionId> grantedPermissions = new ArrayList<>();

  public DefaultRole(
      RoleId id, String name, ApiId assignApi, List<PermissionId> grantedPermissions) {
    if (!this.checkIfPermissionIsLocatedInApi(grantedPermissions, assignApi)) {
      throw new InvalidValueException(
          ExceptionEvent.DEFAULT_ROLE_CREATION,
          ExceptionReason.GRANTED_PERMISSION_NOT_LOCATED_IN_API,
          null,
          ExceptionLevel.INFO);
    }
    this.setId(id);
    this.setName(name);
    this.assignApi(assignApi);
    this.assignPermission(grantedPermissions);
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
      List<PermissionId> addPermissions, List<PermissionId> removePermissions) {
    if (!this.checkIfPermissionIsLocatedInApi(addPermissions, this.assignedApi())) {
      throw new InvalidValueException(
          ExceptionEvent.DEFAULT_ROLE_PERMISSION_MODIFICATION,
          ExceptionReason.GRANTED_PERMISSION_NOT_LOCATED_IN_API,
          null,
          ExceptionLevel.INFO);
    }

    this.assignPermission(addPermissions);
    this.removeGrantedPermissions(removePermissions);
  }

  private boolean checkIfPermissionIsLocatedInApi(
      List<PermissionId> permissionsToCheck, ApiId assignedApi) {
    for (PermissionId permissionId : permissionsToCheck) {
      if (permissionId.permissionLocation() != assignedApi) {
        return false;
      }
    }
    return true;
  }

  private void assignPermission(List<PermissionId> addPermission) {
    Set<PermissionId> grantedPermissionMap = new HashSet<>(this.grantedPermissions());

    for (PermissionId permissionId : addPermission) {
      if (!grantedPermissionMap.contains(permissionId)) {
        this.grantedPermissions.add(permissionId);
        grantedPermissionMap.add(permissionId);
      }
    }
  }

  private void removeGrantedPermissions(List<PermissionId> removePermissions) {
    Set<PermissionId> removePermissionMap = new HashSet<>(removePermissions);

    this.grantedPermissions.removeIf(removePermissionMap::contains);
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

  public List<PermissionId> grantedPermissions() {
    return this.grantedPermissions;
  }
}
