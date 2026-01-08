package com.vayl.identityAccess.core.domain.subscription;

import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.permission.PermissionId;
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
    this.throwErrorOnEmptyAddAndRemovePermissions(addPermissionIds, removePermissionIds);

    for (PermissionId permissionId : addPermissionIds) {
      this.addPermissionIdToGrantedPermissions(permissionId);
    }

    for (PermissionId permissionId : removePermissionIds) {
      this.removePermissionIdFromGrantedPermissions(permissionId);
    }
  }

  private void throwErrorOnEmptyAddAndRemovePermissions(
      List<PermissionId> addPermissions, List<PermissionId> removePermissions) {
    if (addPermissions.isEmpty() && removePermissions.isEmpty()) {
      throw new InvalidValueException("SUBSCRIPTION_MODIFY_PERMISSIONS", "");
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
}
