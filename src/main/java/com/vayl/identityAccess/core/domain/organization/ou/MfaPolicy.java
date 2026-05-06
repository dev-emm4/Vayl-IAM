package com.vayl.identityAccess.core.domain.organization.ou;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.MfaType;
import com.vayl.identityAccess.core.domain.common.Schedule;
import org.jspecify.annotations.NonNull;

public record MfaPolicy(MfaType mfaType, Schedule enforcementDate) {
  public MfaPolicy(@NonNull MfaType mfaType, @NonNull Schedule enforcementDate) {
    AssertionConcern.isNotNull(mfaType, ExceptionReason.INVALID_MFA_TYPE);
    AssertionConcern.isNotNull(enforcementDate, ExceptionReason.INVALID_MFA_ENFORCEMENT_DATE);
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
