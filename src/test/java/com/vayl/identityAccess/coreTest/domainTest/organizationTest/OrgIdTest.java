package com.vayl.identityAccess.coreTest.domainTest.organizationTest;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class OrgIdTest {
  @Test
  void constructor_withInvalidUUIDv4Id_throwException() {
    String invalidId = "invalid-id!";

    try {
      new OrgId(invalidId);
      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_ORG_ID);
    }
  }

  @Test
  void constructor_withNullId_throwsException() {
    try {
      new OrgId(null);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_ORG_ID);
    }
  }

  @Test
  void constructor_withUUIDv4Id_createsId() {
    String validId = UUID.randomUUID().toString();
    OrgId orgId = new OrgId(validId);

    assert true : "got: " + orgId.id() + " expected: " + validId;
  }

  @Test
  void toString_returnsIdString() {
    String validId = UUID.randomUUID().toString();
    OrgId orgId = new OrgId(validId);

    assert orgId.toString().equals(validId) : "got: " + orgId + " expected: " + validId;
  }
}
