package com.vayl.identityAccess.domainTest.subscriptionTest;

import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.permission.PermissionId;
import com.vayl.identityAccess.core.domain.role.DefaultRole;
import com.vayl.identityAccess.core.domain.role.RoleId;
import com.vayl.identityAccess.core.domain.role.SubscriptionAssignment;
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

  @Test
  void createDefaultRole_withUnGrantedPermissions_throwsInvalidValueException() {
    RoleId roleId = new RoleId(UUID.randomUUID().toString());
    String roleName = "User Role";
    // Create Subscription with no granted permissions
    SubscriptionId subscriptionId = new SubscriptionId(UUID.randomUUID().toString());
    Subscription subscription = new Subscription(subscriptionId, "Gold Plan");
    // Permissions that are not granted by the subscription
    List<PermissionId> selectedPermissions = this.initializePermissionidList(6);

    try {
      subscription.createDefaultRole(roleId, roleName, selectedPermissions);
      assert false : "Expected InvalidValueException was not thrown for ungranted permissions";
    } catch (InvalidValueException e) {
      assert e.invalidValue().equals(selectedPermissions.getFirst().toString())
          : "Exception message mismatch got "
              + e.invalidValue()
              + selectedPermissions.getFirst().toString();
    }
  }

  @Test
  void createDefaultRole_withValidParameters_createDefaultRoleCorrectly() {
    RoleId roleId = new RoleId(UUID.randomUUID().toString());
    String roleName = "User Role";
    // Create Subscription with granted permissions
    SubscriptionId subscriptionId = new SubscriptionId(UUID.randomUUID().toString());
    Subscription subscription = new Subscription(subscriptionId, "Platinum Plan");
    List<PermissionId> grantedPermissions = this.initializePermissionidList(5);
    // Add granted permissions to the subscription
    subscription.modifyGrantedPermissions(grantedPermissions, List.of());

    // Select a subset of granted permissions for the default role
    List<PermissionId> selectedPermissions = grantedPermissions.subList(0, 3);

    DefaultRole defaultRole = subscription.createDefaultRole(roleId, roleName, selectedPermissions);
    SubscriptionAssignment expectedAssignment = new SubscriptionAssignment(subscription.id());

    assert defaultRole.id().equals(roleId)
        : "Role ID mismatch after creating DefaultRole got " + defaultRole.id().toString() + " expected " + roleId.toString();
    assert defaultRole.name().equals(roleName)
        : "Role name mismatch after creating DefaultRole got " + defaultRole.name() + " expected " + roleName;
    assert defaultRole.assignedSubscription().equals(expectedAssignment)
        : "SubscriptionAssignment mismatch after creating DefaultRole got "
            + defaultRole.assignedSubscription().toString()
            + " expected "
            + expectedAssignment.toString();
    assert defaultRole.grantedPermissions().size() == selectedPermissions.size()
        : "Granted permissions size mismatch after creating DefaultRole got "
            + defaultRole.grantedPermissions().size()
            + " expected "
            + selectedPermissions.size();
  }

  @Test
  void containsPermission_withMixedPermissions_returnsCorrectFalse() {
    SubscriptionId subscriptionId = new SubscriptionId(UUID.randomUUID().toString());
    Subscription subscription = new Subscription(subscriptionId, "Enterprise Plan");
    List<PermissionId> grantedPermissions = this.initializePermissionidList(4);
    subscription.modifyGrantedPermissions(grantedPermissions, List.of());

    // Create a list with some granted and some ungranted permissions
    List<PermissionId> testPermissions = List.of(
        grantedPermissions.getFirst(),
        new PermissionId(new ApiId("example.com"), "UNGRANTED_PERMISSION")
    );

    boolean contains = subscription.containsPermission(testPermissions);

    assert !contains : "containsPermission should return false for mixed permissions";
  }

  @Test
  void containsPermission_withAllGrantedPermissions_returnsTrue() {
    SubscriptionId subscriptionId = new SubscriptionId(UUID.randomUUID().toString());
    Subscription subscription = new Subscription(subscriptionId, "Startup Plan");
    List<PermissionId> grantedPermissions = this.initializePermissionidList(4);
    subscription.modifyGrantedPermissions(grantedPermissions, List.of());

    boolean contains = subscription.containsPermission(grantedPermissions);

    assert contains : "containsPermission should return true for all granted permissions";
  }

  private List<PermissionId> initializePermissionidList(int amount) {
    List<PermissionId> permissionIds = new java.util.ArrayList<>();
    ApiId apiId = new ApiId("example.com");

    for (int i = 0; i < amount; i++) {
      permissionIds.add(new PermissionId(apiId, "PERMISSION_" + i));
    }

    return permissionIds;
  }

}
