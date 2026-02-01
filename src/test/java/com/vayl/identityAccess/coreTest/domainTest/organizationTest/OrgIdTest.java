package com.vayl.identityAccess.coreTest.domainTest.organizationTest;

import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class OrgIdTest {
  @Test
  void constructor_withInvalidUUIDv4Id_throwsInvalidValueException() {
    String invalidId = "invalid-id!";

    try {
      new OrgId(invalidId);
      assert false
          : "Expected InvalidValueException was not thrown for invalid UUIDv4 id" + invalidId;
    } catch (InvalidValueException e) {
      assert e.event().equals(ExceptionEvent.ORG_ID_CREATION)
          : "InvalidValueError event mismatch got: "
              + e.event()
              + " expected: "
              + ExceptionEvent.ORG_ID_CREATION;

      assert e.reason().equals(ExceptionReason.INVALID_ID)
          : "InvalidValueError reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.INVALID_ID;

      assert e.level().equals(ExceptionLevel.ERROR)
          : "InvalidValueError level mismatch got: "
              + e.level()
              + " expected: "
              + ExceptionLevel.ERROR;

      assert e.invalidValue().equals(invalidId)
          : "InvalidValueError invalidValue mismatch got: "
              + e.invalidValue()
              + " expected: "
              + invalidId;
    }
  }

  @Test
  void constructor_withUUIDv4Id_createsId() {
    String validId = UUID.randomUUID().toString();
    OrgId orgId = new OrgId(validId);

    assert true
        : "OrgId mismatch after creation got: " + orgId.toString() + " expected: " + validId;
  }

  @Test
  void equals_withSameId_returnsTrue() {
    String validId = UUID.randomUUID().toString();
    OrgId id1 = new OrgId(validId);
    OrgId id2 = new OrgId(validId);

    assert id1.equals(id2);
  }

  @Test
  void equals_withDifferentId_returnsFalse() {
    OrgId id1 = new OrgId(UUID.randomUUID().toString());
    OrgId id2 = new OrgId(UUID.randomUUID().toString());

    assert !id1.equals(id2) : "OrgId with different ids should not be equal";
  }

  @Test
  void toString_returnsIdString() {
    String validId = UUID.randomUUID().toString();
    OrgId orgId = new OrgId(validId);

    assert orgId.toString().equals(validId) : "toString should return the id string";
  }

  @Test
  void hashCode_withSameId_returnsSameHashCode() {
    String validId = UUID.randomUUID().toString();
    OrgId id1 = new OrgId(validId);
    OrgId id2 = new OrgId(validId);

    assert id1.hashCode() == id2.hashCode() : "OrgId with same ids should have the same hash code";
  }
}
