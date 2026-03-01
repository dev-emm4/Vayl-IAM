package com.vayl.identityAccess.coreTest.domainTest.organizationTest.ouTest.authenticationPolicyTest;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.MfaType;
import com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy.RecoveryPolicy;
import org.junit.jupiter.api.Test;

public class RecoveryPolicyTest {
  @Test
  void constructor_withNullMfaType_throwException() {
    try {
      RecoveryPolicy policy = new RecoveryPolicy(null);

      assert false : "Exception expected when passing null parameter";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_OU_ARG)
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_OU_ARG;
    }
  }

  @Test
  void constructor_withValidMfaType_createRecoveryPolicy() {
    MfaType mfaType = MfaType.EMAIL;
    RecoveryPolicy policy = new RecoveryPolicy(mfaType);

    assert policy.mfaType().equals(mfaType) : "got: " + policy.mfaType() + " expected: " + mfaType;
  }

  @Test
  public void toString_validAttributes_returnCorrectString() {
    RecoveryPolicy policy = new RecoveryPolicy(MfaType.EMAIL);
    String expectedString = "RecoveryPolicy{" + "mfaType=" + MfaType.EMAIL + '}';

    assert policy.toString().equals(expectedString);
  }
}
