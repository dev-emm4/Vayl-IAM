package com.vayl.identityAccess.coreTest.domainTest.organizationTest.ouTest.authenticationPolicyTest;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.MfaType;
import com.vayl.identityAccess.core.domain.common.inputtableValue.DateInput;
import com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy.MfaPolicy;
import org.junit.jupiter.api.Test;

public class MfaPolicyTest {
  @Test
  public void constructor_withNullParameters_throwException() {
    MfaType mfaType = MfaType.EMAIL;
    DateInput enforcementDate = new DateInput("2023-12-01T00:00:00Z");

    for (int i = 0; i < 2; i++) {
      try {
        if (i == 0) new MfaPolicy(null, enforcementDate);
        if (i == 1) new MfaPolicy(mfaType, null);

        assert false : "Exception expected when passing null parameter";
      } catch (InvalidValueException e) {
        assert e.reason().equals(ExceptionReason.INVALID_OU_ARG)
            : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_OU_ARG;
      }
    }
  }

  @Test
  void constructor_withValidParameters_throwException() {
    MfaType mfaType = MfaType.EMAIL;
    DateInput enforcementDate = new DateInput("2023-12-01T00:00:00Z");
    MfaPolicy mfaPolicy = new MfaPolicy(mfaType, enforcementDate);

    assert mfaPolicy.mfaType().equals(mfaType)
        : "got: " + mfaPolicy.mfaType() + " expected: " + mfaType;
    assert mfaPolicy.enforcementDate().equals(enforcementDate)
        : "got: " + mfaPolicy.enforcementDate() + "expected: " + enforcementDate;
  }

  @Test
  public void toString_validAttributes_returnCorrectString() {
    MfaPolicy mfaPolicy =
        new MfaPolicy(MfaType.AUTHENTICATOR_APP, new DateInput("2023-12-01T00:00:00Z"));

    String expectedString =
        "MfaPolicy{"
            + "mfaType="
            + MfaType.AUTHENTICATOR_APP
            + ", enforcementDate="
            + new DateInput("2023-12-01T00:00:00Z")
            + '}';

    assert mfaPolicy.toString().equals(expectedString);
  }
}
