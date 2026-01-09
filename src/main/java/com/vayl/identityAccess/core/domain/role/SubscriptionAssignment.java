package com.vayl.identityAccess.core.domain.role;

import com.vayl.identityAccess.core.domain.subscription.SubscriptionId;

public class SubscriptionAssignment {
  private SubscriptionId subscriptionId;
  private String subscriptionContract;

  public SubscriptionAssignment(SubscriptionId subscriptionId) {
    this.setSubscriptionId(subscriptionId);
    this.subscriptionContract = null;
  }

  public SubscriptionAssignment(SubscriptionId subscriptionId, String subscriptionContract) {
    this.setSubscriptionId(subscriptionId);
    this.subscriptionContract = subscriptionContract;
  }

  private void setSubscriptionId(SubscriptionId subscriptionId) {
    this.subscriptionId = subscriptionId;
  }

  public SubscriptionId subscriptionId() {
    return this.subscriptionId;
  }

  public String subscriptionContract() {
    return this.subscriptionContract;
  }

  @Override
  public boolean equals(Object anObject) {
    boolean isEqual = false;
    if (anObject != null && this.getClass() == anObject.getClass()) {
      SubscriptionAssignment typedObject = (SubscriptionAssignment) anObject;
      isEqual = typedObject.subscriptionId().equals(this.subscriptionId());
    }
    return isEqual;
  }

  @Override
  public String toString() {
    return this.subscriptionId().toString();
  }

  @Override
  public int hashCode() {
    return this.subscriptionId().hashCode();
  }
}
