package com.vayl.identityAccess.coreTest.domainTest.organizationTest.registrationSessionTest;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.organization.registrationSession.RegSessionId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class RegSessionIdTest {
  @Test
  void constructor_withInvalidUUIDv4Id_throwsInvalidValueException() {
    String invalidId = "invalid-id!";

    try {
      new RegSessionId(invalidId);
      assert false : "Exception expected" + invalidId;
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_REG_SESSION_ARG)
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_REG_SESSION_ARG;
    }
  }

  @Test
  void constructor_withNullId_throwsException() {
    try {
      new RegSessionId(null);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_REG_SESSION_ARG)
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_REG_SESSION_ARG;
    }
  }

  @Test
  void constructor_withUUIDv4Id_createsId() {
    String validId = UUID.randomUUID().toString();
    RegSessionId regSessionId = new RegSessionId(validId);

    assert regSessionId.id().equals(validId)
        : "got: " + regSessionId.id() + " expected: " + validId;
  }

  @Test
  void toString_returnsIdString() {
    String validId = UUID.randomUUID().toString();
    RegSessionId regSessionId = new RegSessionId(validId);

    assert regSessionId.toString().equals(validId)
        : "got: " + regSessionId + " expected: " + validId;
  }
}
