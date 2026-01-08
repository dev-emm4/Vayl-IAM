package com.vayl.identityAccess.domainTest.subscriptionTest;

import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.permission.PermissionId;
import com.vayl.identityAccess.core.domain.subscription.Subscription;
import com.vayl.identityAccess.core.domain.subscription.SubscriptionId;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class SubscriptionTest {

  @Test
  void constructor_withValidParameters_createSubscriptionCorrectly() {
    SubscriptionId subscriptionId = new SubscriptionId(UUID.randomUUID().toString());
    String subscriptionName = "Premium Plan";
    Subscription subscription = new Subscription(subscriptionId, subscriptionName);

    assert subscription.id().equals(subscriptionId)
        : "Subscription ID mismatch got "
            + subscription.id().toString()
            + " expected "
            + subscriptionId.toString();
    assert subscription.name().equals(subscriptionName)
        : "Subscription name mismatch got " + subscription.name() + " expected " + subscriptionName;
  }

  @Test
  void modifyGrantedPermissions_withEmptyAddAndRemoveLists_throwsInvalidValueException() {
    SubscriptionId subscriptionId = new SubscriptionId(UUID.randomUUID().toString());
    String subscriptionName = "Basic Plan";
    Subscription subscription = new Subscription(subscriptionId, subscriptionName);

    try {
      subscription.modifyGrantedPermissions(List.of(), List.of());
      assert false : "Expected exception was not thrown for empty add and remove lists";
    } catch (InvalidValueException e) {
      assert e.invalidValue().isEmpty()
          : "Exception message mismatch got " + e.invalidValue() + " expected empty string";
    }
  }

  @Test
  void modifyGrantedPermissions_withValidAddAndRemoveLists_modifiesPermissionsCorrectly() {
    SubscriptionId subscriptionId = new SubscriptionId(UUID.randomUUID().toString());
    String subscriptionName = "Standard Plan";
    Subscription subscription = new Subscription(subscriptionId, subscriptionName);

    ApiId apiId = new ApiId("example.com");
    var permissionToAdd = new PermissionId(apiId, "CREATE_USER");
    var permissionToRemove = new PermissionId(apiId, "DELETE_USER");

    // Initially add a permission to be removed later
    subscription.modifyGrantedPermissions(List.of(permissionToRemove), List.of());

    // Now modify permissions
    subscription.modifyGrantedPermissions(List.of(permissionToAdd), List.of(permissionToRemove));

    assert subscription.grantedPermissions().contains(permissionToAdd)
        : "Permission to add was not added correctly";
    assert !subscription.grantedPermissions().contains(permissionToRemove)
        : "Permission to remove was not removed correctly";
  }
}
