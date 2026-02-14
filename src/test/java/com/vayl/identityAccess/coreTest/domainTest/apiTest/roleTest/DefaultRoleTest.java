package com.vayl.identityAccess.coreTest.domainTest.apiTest.roleTest;

import com.vayl.identityAccess.core.domain.api.Api;
import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.api.permission.Permission;
import com.vayl.identityAccess.core.domain.api.permission.PermissionId;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.api.role.DefaultRole;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;

public class DefaultRoleTest {
  @Test
  void constructor_withInvalidPermissions_throwsInvalidValueException() {
    Api api1 = this.createApi("example.com");
    Api api2 = this.createApi("example2.com");
    // creating permissions located in api1
    List<PermissionId> permissionInApi1 = this.createPermissionsFor(api1, 5);
    try {
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

      assert e.invalidValue().equals(permissionInApi1.getFirst().toString())
          : "Exception message mismatch got "
              + e.invalidValue()
              + " expected "
              + permissionInApi1.getFirst().toString();
    }
  }

  @Test
  void constructor_withValidPermissions_createDefaultRoleCorrectly() {
    Api api = this.createApi("example.com");
    List<PermissionId> permissionIds = this.createPermissionsFor(api, 5);
    DefaultRole defaultRole = api.createDefaultRole("create-user", permissionIds);

    assert defaultRole.name().equals("create-user")
        : "Role name mismatch got " + defaultRole.name() + " expected " + "create-user";

    assert defaultRole.assignedApiIds().equals(api.id())
        : "assignedApiIds mismatch got "
            + defaultRole.assignedApiIds().toString()
            + " expected "
            + api.id();

    assert defaultRole.assignedPermissionIds().size() == permissionIds.size()
        : "Granted permissions size mismatch got "
            + defaultRole.assignedPermissionIds().size()
            + " expected "
            + permissionIds.size();
  }

  @Test
  void modifyGrantedPermissions_withValidPermissions_modifiesPermissionIdsCorrectly() {
    Api api = this.createApi("example.com");
    List<PermissionId> totalPermissionIds = this.createPermissionsFor(api, 15);

    List<PermissionId> initialGrantedPermissionsIds = totalPermissionIds.subList(0, 11);
    List<PermissionId> permissionIdsToAdd = totalPermissionIds.subList(11, 13);
    List<PermissionId> permissionIdsToRemove = totalPermissionIds.subList(0, 3);

    DefaultRole defaultRole = api.createDefaultRole("create-user", initialGrantedPermissionsIds);
    defaultRole.modifyGrantedPermissions(permissionIdsToAdd, permissionIdsToRemove);

    int expectedSize =
        initialGrantedPermissionsIds.size()
            + permissionIdsToAdd.size()
            - permissionIdsToRemove.size();

    assert defaultRole.assignedPermissionIds().size() == expectedSize
        : "Granted permissions size mismatch after modification got "
            + defaultRole.assignedPermissionIds().size()
            + " expected "
            + expectedSize;
  }

  @Test
  void modifyGrantedPermissions_withInvalidPermissionIds_throwInvalidValueException() {
    Api api1 = this.createApi("example.com");
    Api api2 = this.createApi("example2.com");

    List<PermissionId> permissionIdsInApi1 = this.createPermissionsFor(api1, 5);
    List<PermissionId> permissionIdsInApi2 = this.createPermissionsFor(api2, 5);

    DefaultRole roleInApi1 = api1.createDefaultRole("admin", permissionIdsInApi1);
    try {
      roleInApi1.modifyGrantedPermissions(permissionIdsInApi2, List.of());

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

      assert e.invalidValue().equals(permissionIdsInApi2.getFirst().toString())
          : "Exception message mismatch got "
              + e.invalidValue()
              + " expected "
              + permissionIdsInApi2.getFirst().toString();
    }
  }

  @Test
  void modifyGrantedPermissions_withRemovingUnassignedPermission_throwInvalidValueException() {
    Api api = this.createApi("example.com");
    List<PermissionId> permissionIds = this.createPermissionsFor(api, 5);

    DefaultRole role = api.createDefaultRole("admin", permissionIds.subList(1, 3));
    PermissionId unassignedPermissionId = permissionIds.getFirst();

    try {
      role.modifyGrantedPermissions(List.of(), List.of(unassignedPermissionId));

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

  private @NonNull Api createApi(String audience) {
    ApiId id = new ApiId(audience);
    return new Api(id, "Example-API");
  }

  private @NonNull List<PermissionId> createPermissionsFor(Api api, int amount) {
    List<PermissionId> permissionIds = new ArrayList<>();

    for (int i = 0; i < amount; i++) {
      String name = "perm" + i;
      Permission permission = api.createPermission(name, "");
      permissionIds.add(permission.id());
    }

    return permissionIds;
  }
}
