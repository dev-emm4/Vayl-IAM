package com.vayl.identityAccess.coreTest.domainTest.apiTest;

import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.inputViolation.InvalidValueException;
import org.junit.jupiter.api.Test;

public class ApiIdTest {
  @Test
  void constructor_withInvalidDomain_throwsInvalidErrorException() {
    String invalidDomain = "invalid_domain!";
    try {
      new ApiId(invalidDomain);

      assert false
          : "Expected InvalidValueException was not thrown for invalid domain: " + invalidDomain;
    } catch (InvalidValueException e) {
      assert e.reason() == ExceptionReason.INVALID_API_ID
          : "InvalidValueError reason mismatch got: " + e.reason() + " expected: " + null;

      assert e.invalidValue().equals(invalidDomain)
          : "InvalidValueError invalidValue mismatch got: "
              + e.invalidValue()
              + " expected: "
              + invalidDomain;
    }
  }

  @Test
  void constructor_withValidDomain_createsApiId() {
    String validDomain = "example.com";
    ApiId apiId = new ApiId(validDomain);
    assert apiId.toString().equals(validDomain)
        : "ApiId mismatch after creation got: " + apiId.toString() + " expected: " + validDomain;
  }

  @Test
  void equals_withSameId_returnsTrue() {
    String domain = "example.com";
    ApiId apiId1 = new ApiId(domain);
    ApiId apiId2 = new ApiId(domain);
    assert apiId1.equals(apiId2) : "ApiIds with the same id should be equal";
  }

  @Test
  void equals_withDifferentId_returnsFalse() {
    ApiId apiId1 = new ApiId("example.com");
    ApiId apiId2 = new ApiId("different.com");
    assert !apiId1.equals(apiId2) : "ApiIds with different ids should not be equal";
  }

  @Test
  void toString_returnsIdString() {
    String domain = "example.com";
    ApiId apiId = new ApiId(domain);
    assert apiId.toString().equals(domain) : "toString should return the id string";
  }

  @Test
  void toHashCode_withSameId_returnsSameHashCode() {
    String domain = "example.com";
    ApiId apiId1 = new ApiId(domain);
    ApiId apiId2 = new ApiId(domain);
    assert apiId1.hashCode() == apiId2.hashCode()
        : "ApiIds with the same id should have the same hash code";
  }
}
