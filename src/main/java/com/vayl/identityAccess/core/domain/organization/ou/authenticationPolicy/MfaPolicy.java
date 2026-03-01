package com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.MfaType;
import com.vayl.identityAccess.core.domain.common.inputtableValue.DateInput;
import org.jspecify.annotations.NonNull;

public record MfaPolicy(MfaType mfaType, DateInput enforcementDate) {
  public MfaPolicy(@NonNull MfaType mfaType, @NonNull DateInput enforcementDate) {
    AssertionConcern.isNotNull(mfaType, ExceptionReason.INVALID_OU_ARG);
    AssertionConcern.isNotNull(enforcementDate, ExceptionReason.INVALID_OU_ARG);
    this.mfaType = mfaType;
    this.enforcementDate = enforcementDate;
  }

  @Override
  public @NonNull String toString() {
    return "MfaPolicy{"
        + "mfaType="
        + this.mfaType
        + ", enforcementDate="
        + this.enforcementDate
        + '}';
  }
}
