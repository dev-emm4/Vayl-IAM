package com.vayl.identityAccess.domainTest.roleTest;

import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.permission.PermissionId;
import com.vayl.identityAccess.core.domain.role.DefaultRole;
import com.vayl.identityAccess.core.domain.role.RoleId;
import com.vayl.identityAccess.core.domain.role.SubscriptionAssignment;
import com.vayl.identityAccess.core.domain.subscription.SubscriptionId;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

public class DefaultRoleTest {

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

  private List<PermissionId> initializePermissionidList(int amount) {
    List<PermissionId> permissionIds = new java.util.ArrayList<>();
    ApiId apiId = new ApiId("example.com");

    for (int i = 0; i < amount; i++) {
      permissionIds.add(new PermissionId(apiId, "PERMISSION_" + i));
    }

    return permissionIds;
  }
}
