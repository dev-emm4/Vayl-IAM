package com.vayl.identityAccess.coreTest.domainTest.apiTest.permissionTest;

import com.vayl.identityAccess.core.domain.api.permission.Permission;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import org.junit.jupiter.api.Test;

public class PermissionTest {
  @Test
  void constructor_withNullId_throwException() {
    try {
      new Permission(null, "this is a invalid permission");

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_PERMISSION_ARG)
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_PERMISSION_ARG;
    }
  }
}
