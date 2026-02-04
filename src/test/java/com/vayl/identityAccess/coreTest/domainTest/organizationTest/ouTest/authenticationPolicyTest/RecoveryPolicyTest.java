package com.vayl.identityAccess.coreTest.domainTest.organizationTest.ouTest.authenticationPolicyTest;

import com.vayl.identityAccess.core.domain.common.MfaType;
import com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy.RecoveryPolicy;
import org.junit.jupiter.api.Test;

public class RecoveryPolicyTest {
  @Test
  public void equals_sameAttributes_returnTrue() {
    RecoveryPolicy policy1 = new RecoveryPolicy(MfaType.EMAIL);
    RecoveryPolicy policy2 = new RecoveryPolicy(MfaType.EMAIL);

    assert policy1.equals(policy2);
  }

  @Test
  public void equals_differentAttributes_returnFalse() {
    RecoveryPolicy policy1 = new RecoveryPolicy(MfaType.EMAIL);
    RecoveryPolicy policy2 = new RecoveryPolicy(MfaType.SMS);

    assert !policy1.equals(policy2);
  }

  @Test
  public void toString_validAttributes_returnCorrectString() {
    RecoveryPolicy policy = new RecoveryPolicy(MfaType.EMAIL);
    String expectedString = "RecoveryPolicy{" + "mfaType=" + MfaType.EMAIL + '}';

    assert policy.toString().equals(expectedString);
  }

  @Test
  public void hashCode_sameAttributes_returnSameHashCode() {
    RecoveryPolicy policy1 = new RecoveryPolicy(MfaType.EMAIL);
    RecoveryPolicy policy2 = new RecoveryPolicy(MfaType.EMAIL);

    assert policy1.hashCode() == policy2.hashCode();
  }

  @Test
  public void hashCode_differentAttributes_returnDifferentHashCode() {
    RecoveryPolicy policy1 = new RecoveryPolicy(MfaType.EMAIL);
    RecoveryPolicy policy2 = new RecoveryPolicy(MfaType.SMS);

    assert policy1.hashCode() != policy2.hashCode();
  }
}
