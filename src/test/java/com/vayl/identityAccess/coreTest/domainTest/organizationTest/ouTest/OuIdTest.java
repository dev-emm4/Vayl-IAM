package com.vayl.identityAccess.coreTest.domainTest.organizationTest.ouTest;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.organization.ou.OuId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class OuIdTest {
  @Test
  void constructor_withInvalidUUIDv4Id_throwsException() {
    String invalidId = "invalid-id!";

    try {
      new OuId(invalidId);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_OU_ID);
    }
  }

  @Test
  void constructor_withNullId_throwException() {
    try {
      new OuId(null);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_OU_ID);
    }
  }

  @Test
  void constructor_withUUIDv4Id_createsId() {
    String validId = UUID.randomUUID().toString();
    OuId ouId = new OuId(validId);

    assert ouId.id().equals(validId) : "got: " + ouId + " expected: " + validId;
  }

  @Test
  void toString_returnsIdString() {
    String validId = UUID.randomUUID().toString();
    OuId ouId = new OuId(validId);

    assert ouId.toString().equals(validId) : "got: " + ouId + " expected: " + validId;
  }
}
