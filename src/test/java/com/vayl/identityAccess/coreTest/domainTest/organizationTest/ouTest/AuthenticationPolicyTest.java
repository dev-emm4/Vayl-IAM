package com.vayl.identityAccess.coreTest.domainTest.organizationTest.ouTest;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.MfaType;
import com.vayl.identityAccess.core.domain.common.Schedule;
import com.vayl.identityAccess.core.domain.organization.ou.AuthenticationPolicy;
import com.vayl.identityAccess.core.domain.organization.ou.MfaPolicy;
import com.vayl.identityAccess.core.domain.organization.ou.RecoveryPolicy;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AuthenticationPolicyTest {
  @Test
  void constructor_withNullParameters_throwException() {
    RecoveryPolicy recoveryPolicy = new RecoveryPolicy(MfaType.EMAIL);
    MfaPolicy mfaPolicy =
        new MfaPolicy(MfaType.AUTHENTICATOR_APP, new Schedule("2023-12-01T00:00:00Z"));

    for (int i = 0; i < 3; i++) {
      try {
        if (i == 0) new AuthenticationPolicy(null, mfaPolicy, true);
        if (i == 1) new AuthenticationPolicy(recoveryPolicy, null, true);
        if (i == 2) new AuthenticationPolicy(recoveryPolicy, mfaPolicy, null);

        assert false : "Exception expected";
      } catch (InvalidValueException e) {
        assert List.of(ExceptionReason.INVALID_RECOVERY_POLICY, ExceptionReason.INVALID_MFA_POLICY, ExceptionReason.INVALID_AUTHENTICATION_POLICY_INHERITANCE).contains(e.reason());
      }
    }
  }

  @Test
  void constructor_withValidParameters_createAuthenticationPolicy() {
    RecoveryPolicy recoveryPolicy = new RecoveryPolicy(MfaType.EMAIL);
    MfaPolicy mfaPolicy =
        new MfaPolicy(MfaType.AUTHENTICATOR_APP, new Schedule("2023-12-01T00:00:00Z"));
    boolean isInherited = false;

    AuthenticationPolicy authenticationPolicy =
        new AuthenticationPolicy(recoveryPolicy, mfaPolicy, isInherited);

    assert authenticationPolicy.recoveryPolicy().equals(recoveryPolicy)
        : "got: " + authenticationPolicy.recoveryPolicy() + " expected: " + recoveryPolicy;
    assert authenticationPolicy.mfaPolicy().equals(mfaPolicy)
        : "got: " + authenticationPolicy.mfaPolicy() + " expected: " + mfaPolicy;
    assert authenticationPolicy.isInherited() == isInherited
        : "got: " + true + " expected: " + isInherited;
  }

  @Test
  public void toString_validAttributes_returnCorrectString() {
    RecoveryPolicy recoveryPolicy = new RecoveryPolicy(MfaType.EMAIL);
    MfaPolicy mfaPolicy =
        new MfaPolicy(MfaType.AUTHENTICATOR_APP, new Schedule("2023-12-01T00:00:00Z"));
    AuthenticationPolicy policy = new AuthenticationPolicy(recoveryPolicy, mfaPolicy, false);

    String expectedString =
        "AuthenticationPolicy{"
            + "recoveryPolicy="
            + recoveryPolicy
            + ", mfaPolicy="
            + mfaPolicy
            + ", isInherited="
            + false
            + '}';

    assert policy.toString().equals(expectedString)
        : "got: " + policy + " expected: " + expectedString;
  }

  @Test
  public void copyWith_withNullParameter_throwException() {
    try {
      RecoveryPolicy recoveryPolicy = new RecoveryPolicy(MfaType.EMAIL);
      MfaPolicy mfaPolicy =
          new MfaPolicy(MfaType.AUTHENTICATOR_APP, new Schedule("2023-12-01T00:00:00Z"));
      AuthenticationPolicy originalPolicy =
          new AuthenticationPolicy(recoveryPolicy, mfaPolicy, false);

      originalPolicy.copyWith(null);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason() == ExceptionReason.INVALID_AUTHENTICATION_POLICY_INHERITANCE
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_AUTHENTICATION_POLICY_INHERITANCE;
    }
  }

  @Test
  public void copyWith_withValidParameters_copiesPolicy() {
    RecoveryPolicy recoveryPolicy = new RecoveryPolicy(MfaType.EMAIL);
    MfaPolicy mfaPolicy =
        new MfaPolicy(MfaType.AUTHENTICATOR_APP, new Schedule("2023-12-01T00:00:00Z"));
    AuthenticationPolicy originalPolicy =
        new AuthenticationPolicy(recoveryPolicy, mfaPolicy, false);

    AuthenticationPolicy copiedPolicy = originalPolicy.copyWith(true);

    assert !originalPolicy.equals(copiedPolicy);
    assert copiedPolicy.recoveryPolicy().equals(originalPolicy.recoveryPolicy())
        : "got: " + copiedPolicy.recoveryPolicy() + " expected: " + originalPolicy.recoveryPolicy();
    assert copiedPolicy.mfaPolicy().equals(originalPolicy.mfaPolicy())
        : "got: " + copiedPolicy.mfaPolicy() + " expected: " + originalPolicy.mfaPolicy();
    assert copiedPolicy.isInherited() : "got: " + false + " expected: " + true;
  }
}
