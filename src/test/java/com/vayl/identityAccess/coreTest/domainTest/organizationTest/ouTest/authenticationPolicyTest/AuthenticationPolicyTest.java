package com.vayl.identityAccess.coreTest.domainTest.organizationTest.ouTest.authenticationPolicyTest;

import com.vayl.identityAccess.core.domain.common.Date;
import com.vayl.identityAccess.core.domain.common.MfaType;
import com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy.AuthenticationPolicy;
import com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy.MfaPolicy;
import com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy.RecoveryPolicy;
import org.junit.jupiter.api.Test;

public class AuthenticationPolicyTest {
  @Test
  public void equals_sameAttributes_returnTrue() {
    RecoveryPolicy recoveryPolicy = new RecoveryPolicy(MfaType.EMAIL);
    MfaPolicy mfaPolicy =
        new MfaPolicy(MfaType.AUTHENTICATOR_APP, new Date("2023-12-01T00:00:00Z"));
    AuthenticationPolicy policy1 = new AuthenticationPolicy(recoveryPolicy, mfaPolicy, false);
    AuthenticationPolicy policy2 = new AuthenticationPolicy(recoveryPolicy, mfaPolicy, false);

    assert policy1.equals(policy2);
  }

  @Test
  public void equals_differentAttributes_returnFalse() {
    RecoveryPolicy recoveryPolicy1 = new RecoveryPolicy(MfaType.EMAIL);
    MfaPolicy mfaPolicy1 =
        new MfaPolicy(MfaType.AUTHENTICATOR_APP, new Date("2023-12-01T00:00:00Z"));
    AuthenticationPolicy policy1 = new AuthenticationPolicy(recoveryPolicy1, mfaPolicy1, false);

    RecoveryPolicy recoveryPolicy2 = new RecoveryPolicy(MfaType.SMS);
    MfaPolicy mfaPolicy2 = new MfaPolicy(MfaType.EMAIL, new Date("2023-12-15T00:00:00Z"));
    AuthenticationPolicy policy2 = new AuthenticationPolicy(recoveryPolicy2, mfaPolicy2, true);

    assert !policy1.equals(policy2);
  }

  @Test
  public void toString_validAttributes_returnCorrectString() {
    RecoveryPolicy recoveryPolicy = new RecoveryPolicy(MfaType.EMAIL);
    MfaPolicy mfaPolicy =
        new MfaPolicy(MfaType.AUTHENTICATOR_APP, new Date("2023-12-01T00:00:00Z"));
    AuthenticationPolicy policy = new AuthenticationPolicy(recoveryPolicy, mfaPolicy, false);

    String expectedString =
        "AuthenticationPolicy{"
            + "recoveryPolicy="
            + recoveryPolicy.toString()
            + ", mfaPolicy="
            + mfaPolicy.toString()
            + ", isInherited="
            + false
            + '}';

    assert policy.toString().equals(expectedString);
  }

  @Test
  public void hashCode_sameAttributes_returnSameHashCode() {
    RecoveryPolicy recoveryPolicy = new RecoveryPolicy(MfaType.EMAIL);
    MfaPolicy mfaPolicy =
        new MfaPolicy(MfaType.AUTHENTICATOR_APP, new Date("2023-12-01T00:00:00Z"));
    AuthenticationPolicy policy1 = new AuthenticationPolicy(recoveryPolicy, mfaPolicy, false);
    AuthenticationPolicy policy2 = new AuthenticationPolicy(recoveryPolicy, mfaPolicy, false);

    assert policy1.hashCode() == policy2.hashCode();
  }

  @Test
  public void hashCode_differentAttributes_returnDifferentHashCode() {
    RecoveryPolicy recoveryPolicy1 = new RecoveryPolicy(MfaType.EMAIL);
    MfaPolicy mfaPolicy1 =
        new MfaPolicy(MfaType.AUTHENTICATOR_APP, new Date("2023-12-01T00:00:00Z"));
    AuthenticationPolicy policy1 = new AuthenticationPolicy(recoveryPolicy1, mfaPolicy1, false);

    RecoveryPolicy recoveryPolicy2 = new RecoveryPolicy(MfaType.SMS);
    MfaPolicy mfaPolicy2 = new MfaPolicy(MfaType.EMAIL, new Date("2023-12-15T00:00:00Z"));
    AuthenticationPolicy policy2 = new AuthenticationPolicy(recoveryPolicy2, mfaPolicy2, true);

    assert policy1.hashCode() != policy2.hashCode();
  }

  @Test
  public void copyWith_passingNull_doesNotChangeField() {
    RecoveryPolicy recoveryPolicy = new RecoveryPolicy(MfaType.EMAIL);
    MfaPolicy mfaPolicy =
        new MfaPolicy(MfaType.AUTHENTICATOR_APP, new Date("2023-12-01T00:00:00Z"));
    AuthenticationPolicy originalPolicy =
        new AuthenticationPolicy(recoveryPolicy, mfaPolicy, false);

    AuthenticationPolicy copiedPolicy = originalPolicy.copyWith(null, null, true);

    assert !originalPolicy.equals(copiedPolicy);
    assert copiedPolicy.recoveryPolicy().equals(originalPolicy.recoveryPolicy());
    assert copiedPolicy.mfaPolicy().equals(originalPolicy.mfaPolicy());
    assert copiedPolicy.isInherited() != originalPolicy.isInherited();
  }

  @Test
    public void copyWith_passingNewValues_changesFields() {
        RecoveryPolicy recoveryPolicy = new RecoveryPolicy(MfaType.EMAIL);
        MfaPolicy mfaPolicy =
            new MfaPolicy(MfaType.AUTHENTICATOR_APP, new Date("2023-12-01T00:00:00Z"));
        AuthenticationPolicy originalPolicy =
            new AuthenticationPolicy(recoveryPolicy, mfaPolicy, false);

        RecoveryPolicy newRecoveryPolicy = new RecoveryPolicy(MfaType.SMS);
        MfaPolicy newMfaPolicy = new MfaPolicy(MfaType.EMAIL, new Date("2023-12-15T00:00:00Z"));
        AuthenticationPolicy copiedPolicy =
            originalPolicy.copyWith(newMfaPolicy, newRecoveryPolicy, true);

        assert !originalPolicy.equals(copiedPolicy);
        assert !copiedPolicy.recoveryPolicy().equals(originalPolicy.recoveryPolicy());
        assert !copiedPolicy.mfaPolicy().equals(originalPolicy.mfaPolicy());
        assert copiedPolicy.isInherited() != originalPolicy.isInherited();
    }
}
