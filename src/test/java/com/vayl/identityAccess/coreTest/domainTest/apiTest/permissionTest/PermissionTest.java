package com.vayl.identityAccess.coreTest.domainTest.apiTest.permissionTest;

import com.vayl.identityAccess.core.domain.api.Api;
import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.api.permission.Permission;
import com.vayl.identityAccess.core.domain.api.permission.PermissionId;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.inputViolation.InvalidValue;
import com.vayl.identityAccess.core.domain.common.DomainErrors.inputViolation.InvalidValueException;
import org.junit.jupiter.api.Test;

public class PermissionTest {
  @Test
  void createPermission_withInvalidName_throwInvalidValueException() {
    Api api = createApi("example.com");
    String invalidPermissionName = ""; // Invalid invalidPermissionName
    String description = "Permission to create a user";
    try {
      api.createPermission(invalidPermissionName, description);

      assert false
          : "Expected exception was not thrown when creating permission with invalidPermissionName";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_PERMISSION_ID)
          : "InvalidValueException reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.INVALID_PERMISSION_ID;

      assert e.invalidValue().equals(invalidPermissionName)
          : "Exception invalidValue mismatch got "
              + e.invalidValue()
              + " expected "
              + invalidPermissionName;
    }
  }

  @Test
  void createPermission_withValidName_createsPermissionWithCorrectly() {
    Api api = createApi("example.com");
    java.lang.String name = "CREATE_USER";
    java.lang.String description = "Permission to create a user";

    Permission permission = api.createPermission(name, description);
    PermissionId expectedPermissionId = new PermissionId(api.id(), name);

    assert permission.id().toString().equals(expectedPermissionId.toString())
        : "Permission ID mismatch got "
            + permission.id().toString()
            + " expected "
            + api.id().toString()
            + name;

    assert permission.name().equals(name)
        : "name mismatch got " + permission.name() + " expected " + name;

    assert permission.description().equals(description)
        : "Description mismatch got " + permission.description() + " expected " + description;

    assert permission.location().equals(api.id())
        : "Location mismatch got " + permission.location() + " expected " + api.id();
  }

  private Api createApi(String audience) {
    ApiId id = new ApiId(audience);
    return new Api(id, "Example-API");
  }
}
