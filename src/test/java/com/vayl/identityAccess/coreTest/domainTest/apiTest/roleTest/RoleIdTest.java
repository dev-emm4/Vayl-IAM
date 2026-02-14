package com.vayl.identityAccess.coreTest.domainTest.apiTest.roleTest;

import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.api.role.RoleId;
import org.junit.jupiter.api.Test;

public class RoleIdTest {

  @Test
  void constructor_withInvalidUUIDv4Id_throwsInvalidValueException() {
    String invalidId = "invalid-id!";

    try {
      new RoleId(invalidId);
      assert false
          : "Expected InvalidValueException was not thrown for invalid UUIDv4 id" + invalidId;
    } catch (InvalidValueException e) {
      assert e.event().equals(ExceptionEvent.ROLE_ID_CREATION)
              : "InvalidValueError event mismatch got: "
              + e.event()
              + " expected: "
              + ExceptionEvent.ROLE_ID_CREATION;

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
    String validId = java.util.UUID.randomUUID().toString();
    RoleId roleId = new RoleId(validId);

    assert roleId.toString().equals(validId)
        : "DefaultRoleId mismatch after creation got: "
            + roleId.toString()
            + " expected: "
            + validId;
  }

  @Test
  void equals_withSameId_returnsTrue() {
    String validId = java.util.UUID.randomUUID().toString();
    RoleId id1 = new RoleId(validId);
    RoleId id2 = new RoleId(validId);
    assert id1.equals(id2) : "DefaultRoleIds with same ids should be equal";
  }

  @Test
  void equals_withDifferentId_returnsFalse() {
    RoleId id1 = new RoleId(java.util.UUID.randomUUID().toString());
    RoleId id2 = new RoleId(java.util.UUID.randomUUID().toString());

    assert !id1.equals(id2) : "DefaultRoleIds with different ids should not be equal";
  }

  @Test
  void toString_returnsIdString() {
    String validId = java.util.UUID.randomUUID().toString();
    RoleId roleId = new RoleId(validId);
    assert roleId.toString().equals(validId)
        : "DefaultRoleId toString mismatch got: "
            + roleId.toString()
            + " expected: "
            + validId;
  }

  @Test
  void hashCode_withSameId_returnsSameHashCode() {
    String validId = java.util.UUID.randomUUID().toString();
    RoleId id1 = new RoleId(validId);
    RoleId id2 = new RoleId(validId);
    assert id1.hashCode() == id2.hashCode()
        : "DefaultRoleIds with same ids should have same hashCode";
  }
}
