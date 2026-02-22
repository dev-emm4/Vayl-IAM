package com.vayl.identityAccess.coreTest.domainTest.apiTest.roleTest;

import com.vayl.identityAccess.core.domain.api.Api;
import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.api.permission.PermissionId;
import com.vayl.identityAccess.core.domain.api.role.CustomRole;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.inputViolation.InvalidValueException;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomRoleTest {
  OrgId orgId;
  Api api;
  List<PermissionId> permissionIds;

  @BeforeAll
  public void InitAll() {
    this.orgId = new OrgId(UUID.randomUUID().toString());
    this.api = new Api(new ApiId("example.com"), "admin");
    this.permissionIds = this.createPermissionIdsFor(this.api, 6, "create-user");
  }

  @Test
  public void createCustomRole_withRequiredParameter_createCustomRole() {
    String roleName = "accountant";
    CustomRole role = this.api.createCustomRole(roleName, this.orgId, this.permissionIds);

    assert role.accessibleBy(this.orgId) : "custom role should belong to org";
    assert role.assignedApiId().equals(this.api.id())
        : "assigned API ID mismatch got "
            + role.assignedApiId().toString()
            + " expected "
            + this.api.id().toString();
    assert role.name().equals("accountant")
        : "role name mismatch got " + role.name() + " expected " + roleName;
    assert new ArrayList<>(role.assignedPermissionIds()).equals(this.permissionIds)
        : "role assignedPermissionIds mismatch got "
            + role.assignedPermissionIds()
            + " expected "
            + this.permissionIds;
  }

  @Test
  public void createCustomRole_withPermissionInDifferentApi_throwException() {
    Api differenApi = new Api(new ApiId("different.com"), "admin");
    List<PermissionId> differentApiPermissions =
        this.createPermissionIdsFor(differenApi, 9, "delete-users");

    try {
      this.api.createCustomRole("dev-team", this.orgId, differentApiPermissions);

      assert false
          : "Expected exception was not thrown when creating role with unauthorize permission";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.ASSIGNING_UNAUTHORIZED_PERMISSION_TO_ROLE)
          : "reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.ASSIGNING_UNAUTHORIZED_PERMISSION_TO_ROLE;
      assert e.invalidValue().equals(differentApiPermissions.getFirst().toString())
          : "invalidValue mismatch got: "
              + e.invalidValue()
              + " expected: "
              + differentApiPermissions.getFirst().toString();
    }
  }

  @Test
  public void modifyGrantedPermission_withValidPermissions_modifyPermission() {
    String roleName = "accountant";
    CustomRole role = this.api.createCustomRole(roleName, this.orgId, this.permissionIds);

    List<PermissionId> addPermissionIds = this.createPermissionIdsFor(this.api, 6, "analytic-perm");

    role.modifyGrantedPermissions(addPermissionIds, this.permissionIds);

    assert new ArrayList<>(role.assignedPermissionIds()).equals(addPermissionIds)
        : "role assignedPermissionIds mismatch got "
            + role.assignedPermissionIds()
            + " expected "
            + addPermissionIds;
  }

  @Test
  public void modifyGrantedPermission_withUnassignedRemovePermissionId_throwException() {
    String roleName = "accountant";
    CustomRole role = this.api.createCustomRole(roleName, this.orgId, this.permissionIds);

    List<PermissionId> removePermissionIds = this.createPermissionIdsFor(api, 6, "analytic-perm");

    try {
      role.modifyGrantedPermissions(this.permissionIds, removePermissionIds);

      assert false
          : "Expected exception was not thrown when modifying defaultRole with unassigned Permission";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.REMOVING_UNASSIGNED_PERMISSION_FROM_ROLE)
          : "reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.ASSIGNING_UNAUTHORIZED_PERMISSION_TO_ROLE;
      assert e.invalidValue().equals(removePermissionIds.getFirst().toString())
          : "invalid value mismatch got: "
              + e.invalidValue()
              + " expected: "
              + removePermissionIds.getFirst().toString();
    }
  }

  @Test
  public void modifyGrantedPermission_withAddPermissionIdsInDifferentApi_throwException() {
    Api differnetApi = new Api(new ApiId("example.com"), "billing-API");
    List<PermissionId> differentApiPermissionIds =
        this.createPermissionIdsFor(differnetApi, 8, "create-users");

    CustomRole role = this.api.createCustomRole("admin", this.orgId, this.permissionIds);

    try {
      role.modifyGrantedPermissions(differentApiPermissionIds, this.permissionIds);

      assert false
          : "Expected exception was not thrown when modifying defaultRole with invalid Permission";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.ASSIGNING_UNAUTHORIZED_PERMISSION_TO_ROLE)
          : "reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.ASSIGNING_UNAUTHORIZED_PERMISSION_TO_ROLE;
      assert e.invalidValue().equals(differentApiPermissionIds.getFirst().toString())
          : "invalidValue mismatch got: "
              + e.invalidValue()
              + " expected: "
              + differentApiPermissionIds.getFirst().toString();
    }
  }

  private @NonNull List<PermissionId> createPermissionIdsFor(Api api, int amount, String name) {
    List<PermissionId> permissionIds = new ArrayList<>();

    for (int i = 0; i < amount; i++) {
      permissionIds.add(api.createPermission(name + i, "").id());
    }

    return permissionIds;
  }
}
