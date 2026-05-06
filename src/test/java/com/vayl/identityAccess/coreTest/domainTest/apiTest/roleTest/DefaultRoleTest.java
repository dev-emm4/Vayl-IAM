package com.vayl.identityAccess.coreTest.domainTest.apiTest.roleTest;

import com.vayl.identityAccess.core.domain.api.Api;
import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.api.permission.PermissionId;
import com.vayl.identityAccess.core.domain.api.role.DefaultRole;
import com.vayl.identityAccess.core.domain.api.role.RoleId;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
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
  public void constructor_withNullParameters_throwException() {
    RoleId roleId = new RoleId(UUID.randomUUID().toString());
    String name = "admin-role";
    ApiId apiId = this.api.id();
    List<PermissionId> permissionIds = List.of();

    for (int i = 0; i < 4; i++) {
      try {
        if (i == 0) new DefaultRole(null, name, apiId, permissionIds);
        if (i == 1) new DefaultRole(roleId, null, apiId, permissionIds);
        if (i == 2) new DefaultRole(roleId, name, null, permissionIds);
        if (i == 3) new DefaultRole(roleId, name, apiId, null);

        assert false : "Exception expected";
      } catch (InvalidValueException e) {
        assert List.of(
                ExceptionReason.INVALID_ROLE_ID,
                ExceptionReason.INVALID_ROLE_NAME,
                ExceptionReason.INVALID_API_ID,
                ExceptionReason.INVALID_PERMISSION_ID)
            .contains(e.reason());
      }
    }
  }

  @Test
  public void constructor_withBlankName_throwException() {
    RoleId roleId = new RoleId(UUID.randomUUID().toString());
    String name = "   ";
    ApiId apiId = this.api.id();
    List<PermissionId> permissionIds = List.of();

    try {
      new DefaultRole(roleId, name, apiId, permissionIds);
      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_ROLE_NAME);
    }
  }

  @Test
  public void constructor_withValidParameters_createDefaultRole() {
    RoleId roleId = new RoleId(UUID.randomUUID().toString());
    String name = "admin-role";
    ApiId apiId = this.api.id();

    DefaultRole role = new DefaultRole(roleId, name, apiId, this.permissionIds);

    assert role.id().equals(roleId) : "got: " + role.id() + " expected: " + roleId;
    assert role.name().equals(name) : "got: " + role.name() + " expected: " + name;
    assert role.assignedApiId().equals(apiId)
        : "got: " + role.assignedApiId() + " expected: " + apiId;
    assert role.assignedPermissionIds().equals(new HashSet<>(this.permissionIds))
        : "got: " + role.assignedPermissionIds() + " expected: " + this.permissionIds;
  }

  @Test
  public void modifyGrantedPermission_withValidPermissions_modifyPermission() {
    String roleName = "accountant";
    DefaultRole role = this.api.createDefaultRole(roleName, this.permissionIds);

    List<PermissionId> addPermissionIds = this.createPermissionIdsFor(this.api, 6, "analytic-perm");

    role.modifyGrantedPermissions(addPermissionIds, this.permissionIds);

    assert role.assignedPermissionIds().equals(new HashSet<>(addPermissionIds))
        : "got " + role.assignedPermissionIds() + " expected " + addPermissionIds;
  }

  @Test
  public void modifyGrantedPermission_withNullParameters_throwException() {
    String roleName = "accountant";
    DefaultRole role = this.api.createDefaultRole(roleName, this.permissionIds);

    for (int i = 0; i < 2; i++) {
      try {
        if (i == 0) role.modifyGrantedPermissions(null, this.permissionIds);
        if (i == 1) role.modifyGrantedPermissions(List.of(), null);

        assert false : "Exception expected";
      } catch (InvalidValueException e) {
        assert e.reason().equals(ExceptionReason.INVALID_PERMISSION_ID);
      }
    }
  }

  @Test
  public void modifyGrantedPermission_withUnassignedRemovePermissionId_throwException() {
    String roleName = "accountant";
    DefaultRole role = this.api.createDefaultRole(roleName, this.permissionIds);

    List<PermissionId> removePermissionIds = this.createPermissionIdsFor(api, 6, "analytic-perm");

    try {
      role.modifyGrantedPermissions(this.permissionIds, removePermissionIds);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.UNPROCESSABLE_CANNOT_REMOVE_UNASSIGNED_PERMISSION);
    }
  }

  @Test
  public void modifyGrantedPermission_withAddPermissionIdsInDifferentApi_throwException() {
    Api differnetApi = new Api(new ApiId("different.com"), "billing-API");
    List<PermissionId> differentApiPermissionIds =
        this.createPermissionIdsFor(differnetApi, 8, "create-users");

    DefaultRole role = this.api.createDefaultRole("admin", this.permissionIds);

    try {
      role.modifyGrantedPermissions(differentApiPermissionIds, this.permissionIds);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {

      assert e.reason().equals(ExceptionReason.UNPROCESSABLE_PERMISSION_BELONG_TO_DIFFERENT_API);
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
