package com.vayl.identityAccess.core.domain.organization.ou;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.MfaType;
import org.jspecify.annotations.NonNull;

public record RecoveryPolicy(MfaType mfaType) {
  public RecoveryPolicy(@NonNull MfaType mfaType) {
    AssertionConcern.isNotNull(mfaType, ExceptionReason.INVALID_MFA_TYPE);
    this.mfaType = mfaType;
  }

  @Override
  public @NonNull String toString() {
    return "RecoveryPolicy{" + "mfaType=" + this.mfaType + '}';
  }
}
