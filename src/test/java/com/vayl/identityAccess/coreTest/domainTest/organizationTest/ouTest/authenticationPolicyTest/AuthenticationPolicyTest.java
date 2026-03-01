package com.vayl.identityAccess.coreTest.domainTest.organizationTest.ouTest.authenticationPolicyTest;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.MfaType;
import com.vayl.identityAccess.core.domain.common.inputtableValue.DateInput;
import com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy.AuthenticationPolicy;
import com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy.MfaPolicy;
import com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy.RecoveryPolicy;
import org.junit.jupiter.api.Test;

public class AuthenticationPolicyTest {
  @Test
  void constructor_withNullParameters_throwException() {
    RecoveryPolicy recoveryPolicy = new RecoveryPolicy(MfaType.EMAIL);
    MfaPolicy mfaPolicy =
        new MfaPolicy(MfaType.AUTHENTICATOR_APP, new DateInput("2023-12-01T00:00:00Z"));

    for (int i = 0; i < 2; i++) {
      try {
        if (i == 0) new AuthenticationPolicy(null, mfaPolicy, true);
        if (i == 1) new AuthenticationPolicy(recoveryPolicy, null, true);

        assert false : "Exception expected when passing null parameters";
      } catch (InvalidValueException e) {
        assert e.reason() == ExceptionReason.INVALID_OU_ARG
            : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_OU_ARG;
      }
    }
  }

  @Test
  void constructor_withValidParameters_createAuthenticationPolicy() {
    RecoveryPolicy recoveryPolicy = new RecoveryPolicy(MfaType.EMAIL);
    MfaPolicy mfaPolicy =
        new MfaPolicy(MfaType.AUTHENTICATOR_APP, new DateInput("2023-12-01T00:00:00Z"));
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
        new MfaPolicy(MfaType.AUTHENTICATOR_APP, new DateInput("2023-12-01T00:00:00Z"));
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
  public void copyWith_passingNull_doesNotChangeField() {
    RecoveryPolicy recoveryPolicy = new RecoveryPolicy(MfaType.EMAIL);
    MfaPolicy mfaPolicy =
        new MfaPolicy(MfaType.AUTHENTICATOR_APP, new DateInput("2023-12-01T00:00:00Z"));
    AuthenticationPolicy originalPolicy =
        new AuthenticationPolicy(recoveryPolicy, mfaPolicy, false);

    AuthenticationPolicy copiedPolicy = originalPolicy.copyWith(null, null, true);

    assert !originalPolicy.equals(copiedPolicy);
    assert copiedPolicy.recoveryPolicy().equals(originalPolicy.recoveryPolicy())
        : "got: " + copiedPolicy.recoveryPolicy() + " expected: " + originalPolicy.recoveryPolicy();
    assert copiedPolicy.mfaPolicy().equals(originalPolicy.mfaPolicy())
        : "got: " + copiedPolicy.mfaPolicy() + " expected: " + originalPolicy.mfaPolicy();
    assert copiedPolicy.isInherited() != originalPolicy.isInherited()
        : "got: " + copiedPolicy.isInherited() + " expected: " + !originalPolicy.isInherited();
  }

  @Test
  public void copyWith_passingNewValues_changesFields() {
    RecoveryPolicy recoveryPolicy = new RecoveryPolicy(MfaType.EMAIL);
    MfaPolicy mfaPolicy =
        new MfaPolicy(MfaType.AUTHENTICATOR_APP, new DateInput("2023-12-01T00:00:00Z"));
    AuthenticationPolicy originalPolicy =
        new AuthenticationPolicy(recoveryPolicy, mfaPolicy, false);

    RecoveryPolicy newRecoveryPolicy = new RecoveryPolicy(MfaType.SMS);
    MfaPolicy newMfaPolicy = new MfaPolicy(MfaType.EMAIL, new DateInput("2023-12-15T00:00:00Z"));
    AuthenticationPolicy copiedPolicy =
        originalPolicy.copyWith(newMfaPolicy, newRecoveryPolicy, true);

    assert !originalPolicy.equals(copiedPolicy);
    assert !copiedPolicy.recoveryPolicy().equals(originalPolicy.recoveryPolicy())
        : "got: " + copiedPolicy.recoveryPolicy() + " expected: " + newRecoveryPolicy;
    assert !copiedPolicy.mfaPolicy().equals(originalPolicy.mfaPolicy())
        : "got: " + copiedPolicy.mfaPolicy() + " expected: " + newMfaPolicy;
    ;
    assert copiedPolicy.isInherited() != originalPolicy.isInherited()
        : "got: " + copiedPolicy.isInherited() + " expected: " + !originalPolicy.isInherited();
    ;
  }
}
