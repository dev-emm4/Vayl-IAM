package com.vayl.identityAccess.coreTest.domainTest.roleTest;

import com.vayl.identityAccess.core.domain.api.Api;
import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.api.permission.Permission;
import com.vayl.identityAccess.core.domain.api.permission.PermissionId;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.role.DefaultRole;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class DefaultRoleTest {
  // TODO: TODO: Replace String with a more specific Contract
  @Test
  void constructor_withInvalidPermissions_throwsInvalidValueException() {
    try {
      Api api1 = this.createApi("example.com");
      Api api2 = this.createApi("example2.com");
      // creating permissions located in api1
      List<PermissionId> permissionInApi1 = this.createPermissionsFor(api1, 5);
      // using api1 permissions to create role for api2
      DefaultRole defaultRole = api2.createDefaultRole("create-user", permissionInApi1);

      assert false
          : "Expected exception was not thrown when creating defaultRole with invalid Permission";
    } catch (InvalidValueException e) {
      assert e.event().equals(ExceptionEvent.DEFAULT_ROLE_CREATION)
          : "InvalidValueError event mismatch got: "
              + e.event()
              + " expected: "
              + ExceptionEvent.DEFAULT_ROLE_CREATION;

      assert e.reason().equals(ExceptionReason.GRANTED_PERMISSION_NOT_LOCATED_IN_API)
          : "InvalidValueError reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.GRANTED_PERMISSION_NOT_LOCATED_IN_API;

      assert e.level().equals(ExceptionLevel.INFO)
          : "InvalidValueError level mismatch got: "
              + e.level()
              + " expected: "
              + ExceptionLevel.ERROR;

      assert e.invalidValue() == null
          : "Exception message mismatch got " + e.invalidValue() + " expected" + null;
    }
  }

  @Test
  void constructor_withValidPermissions_createDefaultRoleCorrectly() {
    Api api = this.createApi("example.com");
    List<PermissionId> assignPermissions = this.createPermissionsFor(api, 5);
    DefaultRole defaultRole = api.createDefaultRole("create-user", assignPermissions);

    assert defaultRole.name().equals("create-user")
        : "Role name mismatch got " + defaultRole.name() + " expected " + "create-user";

    assert defaultRole.assignedApi().equals(api.id())
        : "assignedApi mismatch got "
            + defaultRole.assignedApi().toString()
            + " expected "
            + api.id();

    assert defaultRole.grantedPermissions().size() == assignPermissions.size()
        : "Granted permissions size mismatch got "
            + defaultRole.grantedPermissions().size()
            + " expected "
            + assignPermissions.size();
  }

  @Test
  void modifyGrantedPermissions_withValidPermissions_modifiesPermissionsCorrectly() {
    Api api = this.createApi("example.com");
    List<PermissionId> totalPermission = this.createPermissionsFor(api, 15);

    List<PermissionId> initialGrantedPermissions = totalPermission.subList(0, 11);
    List<PermissionId> permissionsToAdd = totalPermission.subList(11, 13);
    List<PermissionId> permissionsToRemove = totalPermission.subList(0, 3);

    DefaultRole defaultRole = api.createDefaultRole("create-user", initialGrantedPermissions);
    defaultRole.modifyGrantedPermissions(permissionsToAdd, permissionsToRemove);

    int expectedSize =
        initialGrantedPermissions.size() + permissionsToAdd.size() - permissionsToRemove.size();

    assert defaultRole.grantedPermissions().size() == expectedSize
        : "Granted permissions size mismatch after modification got "
            + defaultRole.grantedPermissions().size()
            + " expected "
            + expectedSize;
  }

  @Test
  void modifyGrantedPermissions_withInvalidPermissions_throwInvalidValueException() {
    try {
      Api api1 = this.createApi("example.com");
      Api api2 = this.createApi("example2.com");

      List<PermissionId> permissionsInApi1 = this.createPermissionsFor(api1, 5);
      List<PermissionId> permissionsInApi2 = this.createPermissionsFor(api2, 5);

      DefaultRole roleInApi1 = api1.createDefaultRole("admin", permissionsInApi1);
      roleInApi1.modifyGrantedPermissions(permissionsInApi2, List.of());

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
              + ExceptionLevel.ERROR;

      assert e.invalidValue() == null
          : "Exception message mismatch got " + e.invalidValue() + " expected" + null;
    }
  }

  private Api createApi(String audience) {
    ApiId id = new ApiId(audience);
    return new Api(id, "Example-API");
  }

  private List<PermissionId> createPermissionsFor(Api api, int amount) {
    List<PermissionId> permissionIds = new ArrayList<>();

    for (int i = 0; i < amount; i++) {
      String name = "perm" + i;
      Permission permission = api.createPermission(name, "");
      permissionIds.add(permission.id());
    }

    return permissionIds;
  }
}
