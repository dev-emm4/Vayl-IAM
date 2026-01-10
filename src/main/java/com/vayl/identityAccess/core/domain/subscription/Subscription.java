package com.vayl.identityAccess.core.domain.subscription;

import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.permission.PermissionId;
import com.vayl.identityAccess.core.domain.role.DefaultRole;
import com.vayl.identityAccess.core.domain.role.RoleId;
import com.vayl.identityAccess.core.domain.role.SubscriptionAssignment;
import java.util.List;

public class Subscription {
  private SubscriptionId id;
  private String name;
  private List<PermissionId> grantedPermissions;

  public Subscription(SubscriptionId id, String name) {
    this.setId(id);
    this.setName(name);
    // Initialize grantedPermissions as an empty list
    this.initializeGrantedPermissions();
  }

  private void setId(SubscriptionId id) {
    this.id = id;
  }

  private void setName(String name) {
    this.name = name;
  }

  private void initializeGrantedPermissions() {
    this.grantedPermissions = new java.util.ArrayList<>();
  }

  public SubscriptionId id() {
    return this.id;
  }

  public String name() {
    return this.name;
  }

  public List<PermissionId> grantedPermissions() {
    return this.grantedPermissions;
  }

  public void modifyGrantedPermissions(
      List<PermissionId> addPermissionIds, List<PermissionId> removePermissionIds) {

    for (PermissionId permissionId : addPermissionIds) {
      this.addPermissionIdToGrantedPermissions(permissionId);
    }

    for (PermissionId permissionId : removePermissionIds) {
      this.removePermissionIdFromGrantedPermissions(permissionId);
    }
  }

  private void addPermissionIdToGrantedPermissions(PermissionId permissionId) {
    if (!this.grantedPermissions.contains(permissionId)) {
      this.grantedPermissions.add(permissionId);
    }
  }

  private void removePermissionIdFromGrantedPermissions(PermissionId permissionId) {
    if (this.grantedPermissions.remove(permissionId)) {
      // TODO: Publish domain event removedSubscriptionPermission
    }
  }

  public DefaultRole createDefaultRole(
      RoleId roleId, String name, List<PermissionId> selectedPermissions) {

    this.throwErrorIfPermissionNotGranted(selectedPermissions);

    SubscriptionAssignment subscriptionAssignment = new SubscriptionAssignment(this.id());

    return new DefaultRole(roleId, name, subscriptionAssignment, selectedPermissions);
  }

  private void throwErrorIfPermissionNotGranted(List<PermissionId> selectedPermissions) {
    for (PermissionId permissionId : selectedPermissions) {
      if (!this.grantedPermissions.contains(permissionId)) {
        throw new InvalidValueException(
            ExceptionEvent.DEFAULT_ROLE_CREATION,
            ExceptionReason.SELECTED_PERMISSION_NOT_GRANTED_BY_SUBSCRIPTION,
            permissionId.toString(),
            ExceptionLevel.INFO);
      }
    }
  }

  public boolean containsPermission(List<PermissionId> permissionId) {
    for (PermissionId pid : permissionId) {
      if (!this.grantedPermissions.contains(pid)) {
        return false;
      }
    }
    return true;
  }
}
