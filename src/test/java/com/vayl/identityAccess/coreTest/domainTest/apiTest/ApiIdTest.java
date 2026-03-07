package com.vayl.identityAccess.coreTest.domainTest.apiTest;

import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import org.junit.jupiter.api.Test;

public class ApiIdTest {
  @Test
  void constructor_withInvalidDomain_throwErrorException() {
    String invalidDomain = "invalid_domain!";
    try {
      new ApiId(invalidDomain);

      assert false : "Exception expected " + invalidDomain;
    } catch (InvalidValueException e) {
      assert e.reason() == ExceptionReason.INVALID_API_ARG
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_API_ARG;
    }
  }

  @Test
  void constructor_withNullDomain_throwErrorException() {
    try {
      new ApiId(null);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason() == ExceptionReason.INVALID_API_ARG
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_API_ARG;
    }
  }

  @Test
  void constructor_withValidDomain_createsApiId() {
    String validDomain = "example.com";
    ApiId apiId = new ApiId(validDomain);
    assert apiId.toString().equals(validDomain) : "got: " + apiId + " expected: " + validDomain;
  }

  @Test
  void toString_returnsIdString() {
    String domain = "example.com";
    ApiId apiId = new ApiId(domain);
    assert apiId.toString().equals(domain) : "got: " + apiId + " expected: " + domain;
  }
}
