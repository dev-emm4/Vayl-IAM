package com.vayl.identityAccess.core.domain.role;

import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.permission.PermissionId;
import java.util.ArrayList;
import java.util.List;

public class DefaultRole implements Role {
  RoleId id;
  String name;
  SubscriptionAssignment assignedPackage;
  List<PermissionId> grantedPermissions;

  public DefaultRole(
      RoleId id,
      String name,
      SubscriptionAssignment subscriptionAssignment,
      List<PermissionId> grantedPermissions) {
    this.setId(id);
    this.setName(name);
    this.setAssignedPackage(subscriptionAssignment);
    this.setGrantedPermissions(grantedPermissions);
  }

  private void setId(RoleId id) {
    this.id = id;
  }

  private void setName(String name) {
    this.name = name;
  }

  private void setAssignedPackage(SubscriptionAssignment assignedPackage) {
    this.throwErrorIfSubscriptionAssignmentHaveContract(assignedPackage);
    this.assignedPackage = assignedPackage;
  }

  // Ensure that the SubscriptionAssignment does not have an associated contract
  private void throwErrorIfSubscriptionAssignmentHaveContract(
      SubscriptionAssignment subscriptionAssignment) {
    if (subscriptionAssignment.subscriptionContract() != null) {
      throw new InvalidValueException("DEFAULT_ROLE_CREATION", subscriptionAssignment.toString());
    }
  }

  private void setGrantedPermissions(List<PermissionId> grantedPermissions) {
    this.grantedPermissions = new ArrayList<>();
    for (PermissionId permissionId : grantedPermissions) {
      if (!this.grantedPermissions.contains(permissionId)) {
        this.grantedPermissions.add(permissionId);
      }
    }
  }

  public RoleId id() {
    return this.id;
  }

  public String name() {
    return this.name;
  }

  public SubscriptionAssignment assignedSubscription() {
    return this.assignedPackage;
  }

  public List<PermissionId> grantedPermissions() {
    return this.grantedPermissions;
  }
}
