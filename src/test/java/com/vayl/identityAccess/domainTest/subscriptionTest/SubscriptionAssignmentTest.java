package com.vayl.identityAccess.domainTest.subscriptionTest;

import com.vayl.identityAccess.core.domain.role.SubscriptionAssignment;
import com.vayl.identityAccess.core.domain.subscription.SubscriptionId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class SubscriptionAssignmentTest {
  @Test
  void constructor_withValidSubscriptionId_createsAssignment() {
    String validId = UUID.randomUUID().toString();
    SubscriptionId subscriptionId = new SubscriptionId(validId);
    SubscriptionAssignment assignment = new SubscriptionAssignment(subscriptionId);

    assert assignment.subscriptionId().equals(subscriptionId)
        : "SubscriptionAssignment subscriptionId mismatch got: "
            + assignment.subscriptionId()
            + " expected: "
            + subscriptionId;
  }

  @Test
  void equals_withSameSubscriptionId_returnsTrue() {
    String validId = UUID.randomUUID().toString();
    SubscriptionId subscriptionId = new SubscriptionId(validId);
    SubscriptionAssignment assignment1 = new SubscriptionAssignment(subscriptionId);
    SubscriptionAssignment assignment2 = new SubscriptionAssignment(subscriptionId);

    assert assignment1.equals(assignment2)
        : "SubscriptionAssignments with same subscriptionIds should be equal";
  }

  @Test
  void equals_withDifferentSubscriptionId_returnsFalse() {
    SubscriptionAssignment assignment1 =
        new SubscriptionAssignment(new SubscriptionId(java.util.UUID.randomUUID().toString()));
    SubscriptionAssignment assignment2 =
        new SubscriptionAssignment(new SubscriptionId(java.util.UUID.randomUUID().toString()));
    assert !assignment1.equals(assignment2)
        : "SubscriptionAssignments with different subscriptionIds should not be equal";
  }

  @Test
  void toString_returnsSubscriptionIdString() {
    String validId = java.util.UUID.randomUUID().toString();
    SubscriptionId subscriptionId = new SubscriptionId(validId);
    SubscriptionAssignment assignment = new SubscriptionAssignment(subscriptionId);
    assert assignment.toString().equals(validId)
        : "SubscriptionAssignment toString mismatch got: "
            + assignment.toString()
            + " expected: "
            + validId;
  }

  @Test
  void hashCode_withSameSubscriptionId_returnsSameHashCode() {
    String validId = java.util.UUID.randomUUID().toString();
    SubscriptionId subscriptionId = new SubscriptionId(validId);
    SubscriptionAssignment assignment1 = new SubscriptionAssignment(subscriptionId);
    SubscriptionAssignment assignment2 = new SubscriptionAssignment(subscriptionId);
    assert assignment1.hashCode() == assignment2.hashCode()
        : "SubscriptionAssignments with same subscriptionIds should have same hashCode";
  }
}
