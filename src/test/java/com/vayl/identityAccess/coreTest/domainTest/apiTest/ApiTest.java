package com.vayl.identityAccess.coreTest.domainTest.apiTest;

import com.vayl.identityAccess.core.domain.api.Api;
import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.api.permission.Permission;
import com.vayl.identityAccess.core.domain.api.permission.PermissionId;
import com.vayl.identityAccess.core.domain.api.role.CustomRole;
import com.vayl.identityAccess.core.domain.api.role.DefaultRole;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;

public class ApiTest {

  @Test
  void constructor_withValidParameters_createApiCorrectly() {
    ApiId apiId = new ApiId("example.com");
    String apiName = "Test API";
    Api api = new Api(apiId, apiName);

    assert api.id().equals(apiId) : "got: " + api.id() + " expected: " + apiId;
    assert api.name().equals(apiName) : "got: " + api.name() + " expected: " + apiName;
  }

  @Test
  void constructor_withBlankName_throwException() {
    ApiId apiId = new ApiId("example.com");
    String apiName = " ";
    try {
      new Api(apiId, apiName);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason() == ExceptionReason.INVALID_API_ARG
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_API_ARG;
    }
  }

  @Test
  void constructor_withNullParameters_throwException() {
    ApiId apiId = new ApiId("example.com");
    String apiName = "Test API";

    for (int i = 0; i < 2; i++) {
      try {
        if (i == 0) {
          new Api(null, apiName);
        } else {
          new Api(apiId, null);
        }

        assert false : "Exception expected";
      } catch (InvalidValueException e) {
        assert e.reason() == ExceptionReason.INVALID_API_ARG
            : " got: " + e.reason() + " expected: " + ExceptionReason.INVALID_API_ARG;
      }
    }
  }

  @Test
  void createPermission_withBlankName_throwException() {
    Api api = createApi("example.com");
    String invalidPermissionName = "";
    String permissionDescription = "Permission to create a user";
    try {
      api.createPermission(invalidPermissionName, permissionDescription);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_PERMISSION_ARG)
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_PERMISSION_ARG;
    }
  }

  @Test
  void createPermission_withNullName_throwException() {
    Api api = createApi("example.com");
    String permissionDescription = "Permission to create a user";
    try {
      api.createPermission(null, permissionDescription);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_PERMISSION_ARG)
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_PERMISSION_ARG;
    }
  }

  @Test
  void createPermission_withValidName_createsPermissionWithCorrectly() {
    Api api = createApi("example.com");
    String permissionName = "CREATE_USER";
    String permissionDescription = "Permission to create a user";

    Permission permission = api.createPermission(permissionName, permissionDescription);
    PermissionId expectedPermissionId = new PermissionId(api.id(), permissionName);

    assert permission.id().toString().equals(expectedPermissionId.toString())
        : "got: "
            + permission.id().toString()
            + " expected: "
            + api.id().toString()
            + permissionName;

    assert permission.name().equals(permissionName)
        : "got: " + permission.name() + " expected: " + permissionName;

    assert permission.description().equals(permissionDescription)
        : "got: " + permission.description() + " expected: " + permissionDescription;

    assert permission.location().equals(api.id())
        : "got: " + permission.location() + " expected: " + api.id();
  }

  @Test
  public void createDefaultRole_withPermissionInDifferentApi_throwException() {
    Api differenApi = this.createApi("different.com");
    List<PermissionId> differentApiPermissionIds =
        this.createPermissionIdsFor(differenApi, 9, "delete-users");

    try {
      this.createApi("example.com").createDefaultRole("dev-team", differentApiPermissionIds);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_ROLE_ARG)
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_ROLE_ARG;
    }
  }

  @Test
  public void createDefaultRole_withBlankName_throwException() {
    Api api = this.createApi("example.com");
    List<PermissionId> permissionIds = this.createPermissionIdsFor(api, 9, "delete-users");
    String invalidRoleName = " ";

    try {
      api.createDefaultRole(invalidRoleName, permissionIds);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_ROLE_ARG)
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_ROLE_ARG;
    }
  }

  @Test
  public void createDefaultRole_withNullParameters_throwException() {
    Api api = this.createApi("example.com");
    List<PermissionId> permissionIds = this.createPermissionIdsFor(api, 9, "delete-users");
    String roleName = "create-users";

    for (int i = 0; i < 2; i++) {
      try {
        if (i == 0) {
          api.createDefaultRole(null, permissionIds);
        } else {
          api.createDefaultRole(roleName, null);
        }

        assert false : "Exception expected";
      } catch (InvalidValueException e) {
        assert e.reason().equals(ExceptionReason.INVALID_ROLE_ARG)
            : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_ROLE_ARG;
      }
    }
  }

  @Test
  public void createDefaultRole_withValidParameters_createDefaultRole() {
    Api api = this.createApi("example2.com");
    List<PermissionId> permissionIds = this.createPermissionIdsFor(api, 9, "delete-users");
    String roleName = "accountant";
    DefaultRole role = api.createDefaultRole(roleName, permissionIds);

    assert role.assignedApiId().equals(api.id())
        : "got: " + role.assignedApiId().toString() + " expected: " + api.id().toString();
    assert role.name().equals("accountant") : "got: " + role.name() + " expected: " + roleName;
    assert new ArrayList<>(role.assignedPermissionIds()).equals(permissionIds)
        : " got: " + role.assignedPermissionIds() + " expected: " + permissionIds;
  }

  @Test
  public void createCustomRole_withValidParameters_createCustomRole() {
    OrgId orgId = new OrgId(UUID.randomUUID().toString());
    Api api = this.createApi("example2.com");
    List<PermissionId> permissionIds = this.createPermissionIdsFor(api, 8, "edit-users");
    String roleName = "accountant";
    CustomRole role = api.createCustomRole(roleName, orgId, permissionIds);

    assert role.accessibleBy(orgId) : "got: " + role.accessibleBy(orgId) + " expected: true";
    assert role.assignedApiId().equals(api.id())
        : "got " + role.assignedApiId().toString() + " expected " + api.id().toString();
    assert role.name().equals("accountant") : "got " + role.name() + " expected " + roleName;
    assert role.assignedPermissionIds().equals(Set.copyOf(permissionIds))
        : "got " + role.assignedPermissionIds() + " expected " + permissionIds;
  }

  @Test
  public void createCustomRole_withPermissionInDifferentApi_throwException() {
    OrgId orgId = new OrgId(UUID.randomUUID().toString());
    Api differenApi = new Api(new ApiId("different.com"), "admin");
    List<PermissionId> differentApiPermissions =
        this.createPermissionIdsFor(differenApi, 12, "create-users");

    try {
      this.createApi("example.com").createCustomRole("dev-team", orgId, differentApiPermissions);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_ROLE_ARG)
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_ROLE_ARG;
    }
  }

  @Test
  public void createCustomRole_withBlankName_throwException() {
    OrgId orgId = new OrgId(UUID.randomUUID().toString());
    Api api = this.createApi("example.com");
    List<PermissionId> permissionIds = this.createPermissionIdsFor(api, 9, "delete-users");
    String invalidRoleName = " ";

    try {
      api.createCustomRole(invalidRoleName, orgId, permissionIds);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_ROLE_ARG)
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_ROLE_ARG;
    }
  }

  @Test
  public void createCustomRole_withNullParameters_throwException() {
    OrgId orgId = new OrgId(UUID.randomUUID().toString());
    Api api = this.createApi("example.com");
    List<PermissionId> permissionIds = this.createPermissionIdsFor(api, 9, "delete-users");
    String roleName = "accountant";

    for (int i = 0; i < 3; i++) {
      try {
        if (i == 0) {
          api.createCustomRole(null, orgId, permissionIds);
        } else if (i == 1) {
          api.createCustomRole(roleName, null, permissionIds);
        } else {
          api.createCustomRole(roleName, orgId, null);
        }

        assert false : "Exception expected";
      } catch (InvalidValueException e) {
        assert e.reason().equals(ExceptionReason.INVALID_ROLE_ARG)
            : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_ROLE_ARG;
      }
    }
  }

  private Api createApi(String url) {
    ApiId id = new ApiId(url);
    return new Api(id, "Example-API");
  }

  private @NonNull List<PermissionId> createPermissionIdsFor(Api api, int amount, String name) {
    List<PermissionId> permissionIds = new ArrayList<>();

    for (int i = 0; i < amount; i++) {
      permissionIds.add(api.createPermission(name + i, "").id());
    }

    return permissionIds;
  }
}
