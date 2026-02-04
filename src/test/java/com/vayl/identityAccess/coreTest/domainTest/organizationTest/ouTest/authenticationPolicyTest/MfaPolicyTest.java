package com.vayl.identityAccess.coreTest.domainTest.organizationTest.ouTest.authenticationPolicyTest;

import com.vayl.identityAccess.core.domain.common.Date;
import com.vayl.identityAccess.core.domain.common.MfaType;
import com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy.MfaPolicy;
import org.junit.jupiter.api.Test;

public class MfaPolicyTest {
  @Test
  public void equals_sameAttributes_returnTrue() {
    MfaPolicy mfaPolicy1 =
        new MfaPolicy(MfaType.AUTHENTICATOR_APP, new Date("2023-12-01T00:00:00Z"));
    MfaPolicy mfaPolicy2 =
        new MfaPolicy(MfaType.AUTHENTICATOR_APP, new Date("2023-12-01T00:00:00Z"));

    assert mfaPolicy1.equals(mfaPolicy2);
  }

  @Test
  public void equals_differentAttributes_returnFalse() {
    MfaPolicy mfaPolicy1 =
        new MfaPolicy(MfaType.AUTHENTICATOR_APP, new Date("2023-12-01T00:00:00Z"));
    MfaPolicy mfaPolicy2 = new MfaPolicy(MfaType.EMAIL, new Date("2023-12-15T00:00:00Z"));

    assert !mfaPolicy1.equals(mfaPolicy2);
  }

  @Test
  public void toString_validAttributes_returnCorrectString() {
    MfaPolicy mfaPolicy =
        new MfaPolicy(MfaType.AUTHENTICATOR_APP, new Date("2023-12-01T00:00:00Z"));

    String expectedString =
        "MfaPolicy{"
            + "mfaType="
            + MfaType.AUTHENTICATOR_APP
            + ", enforcementDate="
            + new Date("2023-12-01T00:00:00Z")
            + '}';

    assert mfaPolicy.toString().equals(expectedString);
  }

  @Test
  public void hashCode_sameAttributes_returnSameHashCode() {
    MfaPolicy mfaPolicy1 =
        new MfaPolicy(MfaType.AUTHENTICATOR_APP, new Date("2023-12-01T00:00:00Z"));
    MfaPolicy mfaPolicy2 =
        new MfaPolicy(MfaType.AUTHENTICATOR_APP, new Date("2023-12-01T00:00:00Z"));

    assert mfaPolicy1.hashCode() == mfaPolicy2.hashCode();
  }

  @Test
  public void hashCode_differentAttributes_returnDifferentHashCode() {
    MfaPolicy mfaPolicy1 =
        new MfaPolicy(MfaType.AUTHENTICATOR_APP, new Date("2023-12-01T00:00:00Z"));
    MfaPolicy mfaPolicy2 = new MfaPolicy(MfaType.EMAIL, new Date("2023-12-15T00:00:00Z"));

    assert mfaPolicy1.hashCode() != mfaPolicy2.hashCode();
  }
}
