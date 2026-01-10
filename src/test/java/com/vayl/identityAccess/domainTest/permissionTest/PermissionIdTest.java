package com.vayl.identityAccess.domainTest.permissionTest;

import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.permission.PermissionId;
import org.junit.jupiter.api.Test;

public class PermissionIdTest {

  @Test
  void constructor_givenInvalidPermissionName_shouldThrowInvalidValueException() {
    // Given
    String invalidPermissionName = "";
    try {
      ApiId apiId = new ApiId("exmple.com");
      new PermissionId(apiId, invalidPermissionName);
      assert false
          : "Expected InvalidValueError was not thrown for invalid permission name: "
              + invalidPermissionName;
    } catch (InvalidValueException e) {
      assert e.event().equals(ExceptionEvent.PERMISSION_ID_CREATION)
              : "InvalidValueError event mismatch got: "
              + e.event()
              + " expected: "
              + ExceptionEvent.PERMISSION_ID_CREATION;

      assert e.reason().equals(ExceptionReason.EMPTY_PERMISSION_NAME_PROVIDED)
              : "InvalidValueError reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.EMPTY_PERMISSION_NAME_PROVIDED;

      assert e.level().equals(ExceptionLevel.INFO)
              : "InvalidValueError level mismatch got: "
              + e.level()
              + " expected: "
              + ExceptionLevel.INFO;
      assert e.invalidValue().equals(invalidPermissionName)
          : "InvalidValueException invalidValue mismatch got: "
              + e.invalidValue()
              + " expected: "
              + invalidPermissionName;
    }
  }

  @Test
  void constructor_givenValidPermissionName_shouldCreatePermissionId() {
    String validPermissionName = "read_data";
    ApiId apiId = new ApiId("example.com");

    PermissionId permissionId = new PermissionId(apiId, validPermissionName);
    String expectedPermissionIdString = apiId.toString() + "-" + validPermissionName;

    assert permissionId.toString().equals(expectedPermissionIdString)
        : "Permission name mismatch after creation got: "
            + permissionId.toString()
            + " expected: "
            + validPermissionName;
  }

  @Test
  void equals_givenSamePermissionId_shouldReturnTrue() {
    ApiId apiId = new ApiId("example.com");
    PermissionId permissionId1 = new PermissionId(apiId, "read_data");
    PermissionId permissionId2 = new PermissionId(apiId, "read_data");

    assert permissionId1.equals(permissionId2) : "PermissionIds with same values should be equal.";
  }

  @Test
  void equals_givenDifferentPermissionId_shouldReturnFalse() {
    ApiId apiId = new ApiId("example.com");
    PermissionId permissionId1 = new PermissionId(apiId, "read_data");
    PermissionId permissionId2 = new PermissionId(apiId, "write_data");

    assert !permissionId1.equals(permissionId2)
        : "PermissionIds with different values should not be equal.";
  }

  @Test
  void toString_shouldReturnCorrectFormat() {
    ApiId apiId = new ApiId("example.com");
    PermissionId permissionId = new PermissionId(apiId, "read_data");
    String expectedPermissionIdString = "example.com-read_data";

    assert permissionId.toString().equals(expectedPermissionIdString)
        : "toString() output mismatch got: "
            + permissionId.toString()
            + " expected: "
            + expectedPermissionIdString;
  }

  @Test
  void hashCode_withSamePermissionId_returnsSameHashCode() {
    ApiId apiId = new ApiId("example.com");
    PermissionId permissionId1 = new PermissionId(apiId, "read_data");
    PermissionId permissionId2 = new PermissionId(apiId, "read_data");

    assert permissionId1.hashCode() == permissionId2.hashCode()
        : "Hash codes for identical PermissionIds should be the same.";
  }
}
