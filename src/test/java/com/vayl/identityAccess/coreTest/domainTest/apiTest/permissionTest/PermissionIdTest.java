package com.vayl.identityAccess.coreTest.domainTest.apiTest.permissionTest;

import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.api.permission.PermissionId;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PermissionIdTest {

  @Test
  void constructor_withInvalidBlankPermissionName_shouldThrowException() {
    String invalidPermissionName = " ";
    ApiId apiId = new ApiId("example.com");
    try {
      new PermissionId(apiId, invalidPermissionName);
      assert false : "Exception expected " + invalidPermissionName;
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_PERMISSION_NAME);
    }
  }

  @Test
  void constructor_withNullParameters_shouldThrowException() {
    String permissionName = "read_data";
    ApiId apiId = new ApiId("example.com");

    for (int i = 0; i < 2; i++) {
      try {
        new PermissionId(i == 0 ? null : apiId, i == 1 ? null : permissionName);

        assert false : "Exception expected";
      } catch (InvalidValueException e) {
        assert List.of(ExceptionReason.INVALID_PERMISSION_NAME, ExceptionReason.INVALID_API_ID)
            .contains(e.reason());
      }
    }
  }

  @Test
  void constructor_withValidApiIdAndPermissionName_shouldCreatePermissionId() {
    String validPermissionName = "read_data";
    ApiId apiId = new ApiId("example.com");

    PermissionId permissionId = new PermissionId(apiId, validPermissionName);
    String expectedPermissionIdString = apiId + "-" + validPermissionName;

    assert permissionId.toString().equals(expectedPermissionIdString)
        : "got: " + permissionId + " expected: " + validPermissionName;
  }

  @Test
  void toString_shouldReturnCorrectStringFormat() {
    ApiId apiId = new ApiId("example.com");
    PermissionId permissionId = new PermissionId(apiId, "read_data");
    String expectedPermissionIdString = "example.com-read_data";

    assert permissionId.toString().equals(expectedPermissionIdString)
        : "got: " + permissionId + " expected: " + expectedPermissionIdString;
  }
}
