package com.vayl.identityAccess.domainTest.roleTest;

import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
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

public class DefaultRoleTest {
  // TODO: TODO: Replace String with a more specific Contract
  @Test
  void constructor_withSubscriptionAssignmentHavingContract_throwsInvalidValueException() {
    RoleId roleId = new RoleId(UUID.randomUUID().toString());
    String roleName = "Admin Role";
    // Create SubscriptionAssignment with a contract
    SubscriptionId subscriptionId = new SubscriptionId(UUID.randomUUID().toString());
    SubscriptionAssignment subscriptionAssignmentWithContract =
        new SubscriptionAssignment(subscriptionId, "CONTRACT_123");

    List<PermissionId> grantedPermissions = this.initializePermissionidList(5);

    try {
      DefaultRole defaultRole =
          new DefaultRole(roleId, roleName, subscriptionAssignmentWithContract, grantedPermissions);
      assert false : "Expected exception was not thrown for SubscriptionAssignment with contract";
    } catch (InvalidValueException e) {
      assert e.event().equals(ExceptionEvent.DEFAULT_ROLE_CREATION)
          : "InvalidValueError event mismatch got: "
              + e.event()
              + " expected: "
              + ExceptionEvent.DEFAULT_ROLE_CREATION;

      assert e.reason().equals(ExceptionReason.SUBSCRIPTION_ASSIGNMENT_HAS_CONTRACT)
          : "InvalidValueError reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.SUBSCRIPTION_ASSIGNMENT_HAS_CONTRACT;

      assert e.level().equals(ExceptionLevel.ERROR)
          : "InvalidValueError level mismatch got: "
              + e.level()
              + " expected: "
              + ExceptionLevel.ERROR;
      assert e.invalidValue().equals(subscriptionAssignmentWithContract.toString())
          : "Exception message mismatch got "
              + e.invalidValue()
              + " expected"
              + subscriptionAssignmentWithContract.toString();
    }
  }

  @Test
  void constructor_withValidParameters_createDefaultRoleCorrectly() {
    RoleId roleId = new RoleId(UUID.randomUUID().toString());
    String roleName = "User Role";
    // Create SubscriptionAssignment without a contract
    SubscriptionId subscriptionId = new SubscriptionId(UUID.randomUUID().toString());
    SubscriptionAssignment subscriptionAssignment = new SubscriptionAssignment(subscriptionId);

    List<PermissionId> grantedPermissions = this.initializePermissionidList(3);

    DefaultRole defaultRole =
        new DefaultRole(roleId, roleName, subscriptionAssignment, grantedPermissions);

    assert defaultRole.id().equals(roleId)
        : "Role ID mismatch got " + defaultRole.id().toString() + " expected " + roleId.toString();
    assert defaultRole.name().equals(roleName)
        : "Role name mismatch got " + defaultRole.name() + " expected " + roleName;
    assert defaultRole.assignedSubscription().equals(subscriptionAssignment)
        : "SubscriptionAssignment mismatch got "
            + defaultRole.assignedSubscription().toString()
            + " expected "
            + subscriptionAssignment.toString();
    assert defaultRole.grantedPermissions().size() == grantedPermissions.size()
        : "Granted permissions size mismatch got "
            + defaultRole.grantedPermissions().size()
            + " expected "
            + grantedPermissions.size();
  }

  @Test
  void modifyGrantedPermissions_withValidAddAndRemovePermissions_modifiesPermissionsCorrectly() {
    RoleId roleId = new RoleId(UUID.randomUUID().toString());
    String roleName = "Editor Role";
    SubscriptionId subscriptionId = new SubscriptionId(UUID.randomUUID().toString());

    List<PermissionId> subscriptionPermission = this.initializePermissionidList(15);

    List<PermissionId> initialGrantedPermissions = subscriptionPermission.subList(0, 11);
    List<PermissionId> permissionsToAdd = subscriptionPermission.subList(11, 13);
    List<PermissionId> permissionsToRemove = subscriptionPermission.subList(0, 3);

    Subscription subscription = new Subscription(subscriptionId, "Standard Plan");
    subscription.modifyGrantedPermissions(subscriptionPermission, List.of());

    DefaultRole defaultRole =
        subscription.createDefaultRole(roleId, roleName, initialGrantedPermissions);

    defaultRole.modifyGrantedPermissions(subscription, permissionsToAdd, permissionsToRemove);

    int expectedSize =
        initialGrantedPermissions.size() + permissionsToAdd.size() - permissionsToRemove.size();
    assert defaultRole.grantedPermissions().size() == expectedSize
        : "Granted permissions size mismatch after modification got "
            + defaultRole.grantedPermissions().size()
            + " expected "
            + expectedSize;
  }

  @Test
  void modifyGrantedPermissions_withPermissionsNotInSubscription_throwsInvalidValueException() {
    RoleId roleId = new RoleId(UUID.randomUUID().toString());
    String roleName = "Viewer Role";
    SubscriptionId subscriptionId = new SubscriptionId(UUID.randomUUID().toString());

    List<PermissionId> subscriptionPermission = this.initializePermissionidList(5);
    List<PermissionId> invalidPermissions = this.initializePermissionidList(9).subList(6, 8);

    Subscription subscription = new Subscription(subscriptionId, "Basic Plan");
    subscription.modifyGrantedPermissions(subscriptionPermission, List.of());

    DefaultRole defaultRole =
        subscription.createDefaultRole(roleId, roleName, subscriptionPermission);

    try {
      defaultRole.modifyGrantedPermissions(subscription, invalidPermissions, List.of());
      assert false
          : "Expected InvalidValueException was not thrown for permissions not in subscription";
    } catch (InvalidValueException e) {
      assert e.event().equals(ExceptionEvent.DEFAULT_ROLE_PERMISSION_MODIFICATION)
          : "InvalidValueError event mismatch got: "
              + e.event()
              + " expected: "
              + ExceptionEvent.DEFAULT_ROLE_PERMISSION_MODIFICATION;

      assert e.reason().equals(ExceptionReason.SELECTED_PERMISSION_NOT_GRANTED_BY_SUBSCRIPTION)
          : "InvalidValueError reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.SELECTED_PERMISSION_NOT_GRANTED_BY_SUBSCRIPTION;

      assert e.level().equals(ExceptionLevel.INFO)
          : "InvalidValueError level mismatch got: "
              + e.level()
              + " expected: "
              + ExceptionLevel.INFO;
      assert e.invalidValue().equals(invalidPermissions.toString())
          : "Exception message mismatch got "
              + e.invalidValue()
              + " expected "
              + invalidPermissions.getFirst().toString();
    }
  }

  @Test
  void modifyGrantedPermissions_withDifferentSubscription_throwsInvalidValueException() {
    RoleId roleId = new RoleId(UUID.randomUUID().toString());
    String roleName = "Contributor Role";
    SubscriptionId assignedSubscriptionId = new SubscriptionId(UUID.randomUUID().toString());
    SubscriptionId differentSubscriptionId = new SubscriptionId(UUID.randomUUID().toString());

    List<PermissionId> subscriptionPermission = this.initializePermissionidList(7);

    Subscription assignedSubscription = new Subscription(assignedSubscriptionId, "Pro Plan");
    assignedSubscription.modifyGrantedPermissions(subscriptionPermission, List.of());

    Subscription differentSubscription = new Subscription(differentSubscriptionId, "Free Plan");
    differentSubscription.modifyGrantedPermissions(subscriptionPermission, List.of());

    DefaultRole defaultRole =
        assignedSubscription.createDefaultRole(roleId, roleName, subscriptionPermission);

    try {
      defaultRole.modifyGrantedPermissions(differentSubscription, List.of(), List.of());
      assert false
          : "Expected InvalidValueException was not thrown for different assigned subscription";
    } catch (InvalidValueException e) {
      assert e.event().equals(ExceptionEvent.DEFAULT_ROLE_PERMISSION_MODIFICATION)
          : "InvalidValueError event mismatch got: "
              + e.event()
              + " expected: "
              + ExceptionEvent.DEFAULT_ROLE_PERMISSION_MODIFICATION;

      assert e.reason().equals(ExceptionReason.SUBSCRIPTION_NOT_ASSIGNED)
          : "InvalidValueError reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.SUBSCRIPTION_NOT_ASSIGNED;

      assert e.level().equals(ExceptionLevel.ERROR)
          : "InvalidValueError level mismatch got: "
              + e.level()
              + " expected: "
              + ExceptionLevel.ERROR;

      assert e.invalidValue().equals(differentSubscription.id().toString())
          : "Exception message mismatch got "
              + e.invalidValue()
              + " expected "
              + differentSubscription.id().toString();
    }
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
