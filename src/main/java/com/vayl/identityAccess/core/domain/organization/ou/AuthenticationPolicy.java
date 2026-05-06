package com.vayl.identityAccess.core.domain.organization.ou;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public record AuthenticationPolicy(
        RecoveryPolicy recoveryPolicy, MfaPolicy mfaPolicy, Boolean isInherited) {
  public AuthenticationPolicy(
      @NonNull RecoveryPolicy recoveryPolicy,
      @NonNull MfaPolicy mfaPolicy,
      @NonNull Boolean isInherited) {
    AssertionConcern.isNotNull(recoveryPolicy, ExceptionReason.INVALID_RECOVERY_POLICY);
    AssertionConcern.isNotNull(mfaPolicy, ExceptionReason.INVALID_MFA_POLICY);
    AssertionConcern.isNotNull(isInherited, ExceptionReason.INVALID_AUTHENTICATION_POLICY_INHERITANCE);

    this.recoveryPolicy = recoveryPolicy;
    this.mfaPolicy = mfaPolicy;
    this.isInherited = isInherited;
  }

  @Contract(" _ -> new")
  public @NonNull AuthenticationPolicy copyWith(@NonNull Boolean isInherited) {
    AssertionConcern.isNotNull(isInherited, ExceptionReason.INVALID_AUTHENTICATION_POLICY_INHERITANCE);

    return new AuthenticationPolicy(this.recoveryPolicy, this.mfaPolicy, isInherited);
  }

  @Override
  public @NonNull String toString() {
    return "AuthenticationPolicy{"
        + "recoveryPolicy="
        + this.recoveryPolicy.toString()
        + ", mfaPolicy="
        + this.mfaPolicy.toString()
        + ", isInherited="
        + this.isInherited
        + '}';
  }
}
