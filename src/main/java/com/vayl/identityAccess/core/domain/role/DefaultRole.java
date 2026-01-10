package com.vayl.identityAccess.core.domain.role;

import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.permission.PermissionId;
import com.vayl.identityAccess.core.domain.subscription.Subscription;
import java.util.ArrayList;
import java.util.List;

public class DefaultRole implements Role {
  RoleId id;
  String name;
  SubscriptionAssignment assignedSubscription;
  List<PermissionId> grantedPermissions = new ArrayList<>();

  public DefaultRole(
      RoleId id,
      String name,
      SubscriptionAssignment subscriptionAssignment,
      List<PermissionId> grantedPermissions) {
    this.setId(id);
    this.setName(name);
    this.setAssignedSubscription(subscriptionAssignment);
    this.setGrantedPermissions(grantedPermissions);
  }

  private void setId(RoleId id) {
    this.id = id;
  }

  private void setName(String name) {
    this.name = name;
  }

  private void setAssignedSubscription(SubscriptionAssignment assignedSubscription) {
    this.throwErrorIfSubscriptionAssignmentHaveContract(assignedSubscription);
    this.assignedSubscription = assignedSubscription;
  }

  // Ensure that the SubscriptionAssignment does not have an associated contract
  private void throwErrorIfSubscriptionAssignmentHaveContract(
      SubscriptionAssignment subscriptionAssignment) {
    if (subscriptionAssignment.subscriptionContract() != null) {
      throw new InvalidValueException(
          ExceptionEvent.DEFAULT_ROLE_CREATION,
          ExceptionReason.SUBSCRIPTION_ASSIGNMENT_HAS_CONTRACT,
          subscriptionAssignment.toString(),
          ExceptionLevel.ERROR);
    }
  }

  public RoleId id() {
    return this.id;
  }

  public String name() {
    return this.name;
  }

  public SubscriptionAssignment assignedSubscription() {
    return this.assignedSubscription;
  }

  public List<PermissionId> grantedPermissions() {
    return this.grantedPermissions;
  }

  public void modifyGrantedPermissions(
      Subscription subscription,
      List<PermissionId> addPermissions,
      List<PermissionId> removePermissions) {
    this.throwErrorIfSubscriptionIsNotAssigned(subscription);
    this.throwErrorIfPermissionNotGrantedBySubscription(subscription, addPermissions);

    this.setGrantedPermissions(addPermissions);
    this.removeGrantedPermissions(removePermissions);
  }

  private void throwErrorIfPermissionNotGrantedBySubscription(
      Subscription subscription, List<PermissionId> selectedPermissions) {
    if (!subscription.containsPermission(selectedPermissions)) {
      throw new InvalidValueException(
          ExceptionEvent.DEFAULT_ROLE_PERMISSION_MODIFICATION,
          ExceptionReason.SELECTED_PERMISSION_NOT_GRANTED_BY_SUBSCRIPTION,
          selectedPermissions.toString(),
          ExceptionLevel.INFO);
    }
  }

  private void throwErrorIfSubscriptionIsNotAssigned(Subscription subscription) {
    if (!this.assignedSubscription.subscriptionId().equals(subscription.id())) {
      throw new InvalidValueException(
          ExceptionEvent.DEFAULT_ROLE_PERMISSION_MODIFICATION,
          ExceptionReason.SUBSCRIPTION_NOT_ASSIGNED,
          subscription.id().toString(),
          ExceptionLevel.ERROR);
    }
  }

  private void setGrantedPermissions(List<PermissionId> grantedPermissions) {
    for (PermissionId permissionId : grantedPermissions) {
      if (!this.grantedPermissions.contains(permissionId)) {
        this.grantedPermissions.add(permissionId);
      }
    }
  }

  private void removeGrantedPermissions(List<PermissionId> grantedPermissions) {
    for (PermissionId permissionId : grantedPermissions) {
      this.grantedPermissions.remove(permissionId);
    }
  }
}
