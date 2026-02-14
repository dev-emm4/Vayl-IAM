package com.vayl.identityAccess.coreTest.domainTest.apiTest.roleTest;

import com.vayl.identityAccess.core.domain.api.Api;
import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.api.permission.PermissionId;
import com.vayl.identityAccess.core.domain.api.role.DefaultRole;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DefaultRoleTest {
  Api api;
  List<PermissionId> permissionIds;

  @BeforeAll
  public void InitAll() {
    this.api = new Api(new ApiId("example.com"), "admin");
    this.permissionIds = this.createPermissionIdsFor(this.api, 6, "create-user");
  }

  @Test
  public void createCustomRole_withRequiredParameter_createCustomRole() {
    String roleName = "accountant";
    DefaultRole role = this.api.createDefaultRole(roleName, this.permissionIds);

    assert role.assignedApiIds().equals(this.api.id())
        : "assigned API ID mismatch got "
            + role.assignedApiIds().toString()
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
  public void modifyGrantedPermission_withValidPermissions_modifyPermission() {
    String roleName = "accountant";
    DefaultRole role = this.api.createDefaultRole(roleName, this.permissionIds);

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
    DefaultRole role = this.api.createDefaultRole(roleName, this.permissionIds);

    List<PermissionId> removePermissionIds = this.createPermissionIdsFor(api, 6, "analytic-perm");

    try {

      role.modifyGrantedPermissions(this.permissionIds, removePermissionIds);

      assert false
          : "Expected exception was not thrown when modifying defaultRole with unassigned Permission";
    } catch (InvalidValueException e) {
      assert e.event().equals(ExceptionEvent.DEFAULT_ROLE_PERMISSION_MODIFICATION)
          : "InvalidValueError event mismatch got: "
              + e.event()
              + " expected: "
              + ExceptionEvent.DEFAULT_ROLE_PERMISSION_MODIFICATION;

      assert e.reason().equals(ExceptionReason.REMOVING_UNASSIGNED_PERMISSION)
          : "InvalidValueError reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.REMOVING_UNASSIGNED_PERMISSION;

      assert e.level().equals(ExceptionLevel.INFO)
          : "InvalidValueError level mismatch got: "
              + e.level()
              + " expected: "
              + ExceptionLevel.INFO;
    }
  }

  @Test
  public void modifyGrantedPermission_withAddPermissionIdsInDifferentApi_throwException() {
    Api differnetApi = new Api(new ApiId("example.com"), "billing-API");
    List<PermissionId> differentApiPermissionIds =
        this.createPermissionIdsFor(differnetApi, 8, "create-users");

    DefaultRole role = this.api.createDefaultRole("admin", this.permissionIds);

    try {
      role.modifyGrantedPermissions(differentApiPermissionIds, this.permissionIds);

      assert false
          : "Expected exception was not thrown when modifying defaultRole with invalid Permission";
    } catch (InvalidValueException e) {
      assert e.event().equals(ExceptionEvent.DEFAULT_ROLE_PERMISSION_MODIFICATION)
          : "InvalidValueError event mismatch got: "
              + e.event()
              + " expected: "
              + ExceptionEvent.DEFAULT_ROLE_PERMISSION_MODIFICATION;

      assert e.reason().equals(ExceptionReason.GRANTED_PERMISSION_NOT_LOCATED_IN_API)
          : "InvalidValueError reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.GRANTED_PERMISSION_NOT_LOCATED_IN_API;

      assert e.level().equals(ExceptionLevel.INFO)
          : "InvalidValueError level mismatch got: "
              + e.level()
              + " expected: "
              + ExceptionLevel.INFO;

      assert e.invalidValue().equals(differentApiPermissionIds.getFirst().toString())
          : "Exception message mismatch got "
              + e.invalidValue()
              + " expected "
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
