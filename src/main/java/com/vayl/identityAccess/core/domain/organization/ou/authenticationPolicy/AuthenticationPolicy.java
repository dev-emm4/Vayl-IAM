package com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public record AuthenticationPolicy(
    RecoveryPolicy recoveryPolicy, MfaPolicy mfaPolicy, boolean isInherited) {
  public AuthenticationPolicy(
      @NonNull RecoveryPolicy recoveryPolicy, @NonNull MfaPolicy mfaPolicy, boolean isInherited) {
    AssertionConcern.isNotNull(mfaPolicy, ExceptionReason.INVALID_OU_ARG);
    AssertionConcern.isNotNull(recoveryPolicy, ExceptionReason.INVALID_OU_ARG);

    this.recoveryPolicy = recoveryPolicy;
    this.mfaPolicy = mfaPolicy;
    this.isInherited = isInherited;
  }

  @Contract("_, _, _ -> new")
  public @NonNull AuthenticationPolicy copyWith(
      MfaPolicy mfaPolicy, RecoveryPolicy recoveryPolicy, boolean isInherited) {
    MfaPolicy newMfaPolicy = mfaPolicy != null ? mfaPolicy : this.mfaPolicy();
    RecoveryPolicy newRecoveryPolicy =
        recoveryPolicy != null ? recoveryPolicy : this.recoveryPolicy();
    return new AuthenticationPolicy(newRecoveryPolicy, newMfaPolicy, isInherited);
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
