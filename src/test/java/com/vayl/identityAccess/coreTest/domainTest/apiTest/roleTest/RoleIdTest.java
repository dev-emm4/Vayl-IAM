package com.vayl.identityAccess.coreTest.domainTest.apiTest.roleTest;

import com.vayl.identityAccess.core.domain.api.role.RoleId;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class RoleIdTest {

  @Test
  void constructor_withInvalidUUIDv4Id_throwException() {
    String invalidId = "invalid-id!";

    try {
      new RoleId(invalidId);
      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_ROLE_ID);
    }
  }

  @Test
  void constructor_nullId_throwException() {
    String invalidId = null;

    try {
      new RoleId(invalidId);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_ROLE_ID);
    }
  }

  @Test
  void constructor_withUUIDv4Id_createsId() {
    String validId = UUID.randomUUID().toString();
    RoleId roleId = new RoleId(validId);

    assert roleId.toString().equals(validId) : "got: " + roleId + " expected: " + validId;
  }
}
