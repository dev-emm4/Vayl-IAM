package com.vayl.identityAccess.core.domain.organization.ou.authorizationPolicy;

import com.vayl.identityAccess.core.domain.role.RoleId;
import com.vayl.identityAccess.core.domain.license.LicenseId;
import java.util.List;

public class SubscriptionAssignment {
  LicenseId assignedSubscription;
  //TODO: SubscriptionContract implementation required
  String assignedSubscriptionContract;
  List<RoleId> assignedRoles;

  public SubscriptionAssignment(
      LicenseId assignedSubscription,
      String assignedSubscriptionContract,
      List<RoleId> assignedRoles) {
    this.setAssignedSubscription(assignedSubscription);
    this.setAssignedSubscriptionContract(assignedSubscriptionContract);
    this.setAssignedRoles(assignedRoles);
  }

  private void setAssignedRoles(List<RoleId> assignedRoles) {
    this.assignedRoles = assignedRoles;
  }

  private void setAssignedSubscription(LicenseId assignedSubscription) {
    this.assignedSubscription = assignedSubscription;
  }

  private void setAssignedSubscriptionContract(String assignedSubscriptionContract) {
    this.assignedSubscriptionContract = assignedSubscriptionContract;
  }
}
