package com.vayl.identityAccess.core.domain.organization.ou.authorizationPolicy;

import java.util.ArrayList;
import java.util.List;

public class AuthorizationPolicy {
  List<SubscriptionAssignment> subscriptionAssignments = new ArrayList<>();
  boolean isInherited;

  public AuthorizationPolicy(
      List<SubscriptionAssignment> subscriptionAssignments, boolean isInherited) {
    this.setSubscriptionAssignments(subscriptionAssignments);
    this.setInherited(isInherited);
  }

  private void setSubscriptionAssignments(List<SubscriptionAssignment> subscriptionAssignments) {
    this.subscriptionAssignments = subscriptionAssignments;
  }

  private void setInherited(boolean inherited) {
    isInherited = inherited;
  }

  public AuthorizationPolicy() {}
}
