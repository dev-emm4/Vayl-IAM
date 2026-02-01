package com.vayl.identityAccess.coreTest.domainTest.apiTest.permissionTest;

import com.vayl.identityAccess.core.domain.api.Api;
import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.api.permission.Permission;
import com.vayl.identityAccess.core.domain.api.permission.PermissionId;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import org.junit.jupiter.api.Test;

public class PermissionTest {
  @Test
  void createPermission_withInvalidName_throwInvalidValueException(){
      Api api = createApi("example.com");
      String name = ""; // Invalid name
      String description = "Permission to create a user";
    try {
      api.createPermission(name, description);

      assert false : "Expected exception was not thrown when creating permission with invalid name";
    } catch (InvalidValueException e) {
      assert e.event().equals(ExceptionEvent.PERMISSION_ID_CREATION)
          : "InvalidValueError event mismatch got: "
              + e.event()
              + " expected: "
              + ExceptionEvent.PERMISSION_ID_CREATION;

      assert e.reason().equals(ExceptionReason.BLANK_PERMISSION_NAME_PROVIDED)
          : "InvalidValueError reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.BLANK_PERMISSION_NAME_PROVIDED;

      assert e.level().equals(ExceptionLevel.INFO)
          : "InvalidValueError level mismatch got: "
              + e.level()
              + " expected: "
              + ExceptionLevel.INFO;

      assert e.invalidValue().equals(name)
          : "Exception invalid value mismatch got " + e.invalidValue() + " expected " + name;
    }
  }

  @Test
  void createPermission_withValidName_createsPermissionWithCorrectly() {
    Api api = createApi("example.com");
    String name = "CREATE_USER";
    String description = "Permission to create a user";

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
