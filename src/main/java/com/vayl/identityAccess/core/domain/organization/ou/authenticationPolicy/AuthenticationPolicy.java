package com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy;


import org.jspecify.annotations.NonNull;

public class AuthenticationPolicy {
  private RecoveryPolicy recoveryPolicy;
  private MfaPolicy mfaPolicy;
  private boolean isInherited;

  public AuthenticationPolicy(
          @NonNull RecoveryPolicy recoveryPolicy, @NonNull MfaPolicy mfaPolicy, boolean isInherited) {
    this.setRecoveryPolicy(recoveryPolicy);
    this.setMfaPolicy(mfaPolicy);
    this.setIsInherited(isInherited);
  }

  private void setRecoveryPolicy(RecoveryPolicy recoveryPolicy) {
    this.recoveryPolicy = recoveryPolicy;
  }

  private void setMfaPolicy(MfaPolicy mfaPolicy) {
    this.mfaPolicy = mfaPolicy;
  }

  private void setIsInherited(boolean isInherited) {
    this.isInherited = isInherited;
  }

  public RecoveryPolicy recoveryPolicy() {
    return this.recoveryPolicy;
  }

  public MfaPolicy mfaPolicy() {
    return this.mfaPolicy;
  }

  public boolean isInherited() {
    return isInherited;
  }

  public AuthenticationPolicy copyWith(
      MfaPolicy mfaPolicy, RecoveryPolicy recoveryPolicy, boolean isInherited) {
    MfaPolicy newMfaPolicy = mfaPolicy != null ? mfaPolicy : this.mfaPolicy();
    RecoveryPolicy newRecoveryPolicy =
        recoveryPolicy != null ? recoveryPolicy : this.recoveryPolicy();
    return new AuthenticationPolicy(newRecoveryPolicy, newMfaPolicy, isInherited);
  }

  @Override
  public String toString() {
    return "AuthenticationPolicy{"
        + "recoveryPolicy="
        + this.recoveryPolicy.toString()
        + ", mfaPolicy="
        + this.mfaPolicy.toString()
        + ", isInherited="
        + this.isInherited
        + '}';
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || this.getClass() != obj.getClass()) return false;
    AuthenticationPolicy that = (AuthenticationPolicy) obj;
    return this.isInherited == that.isInherited
        && this.recoveryPolicy.equals(that.recoveryPolicy)
        && this.mfaPolicy.equals(that.mfaPolicy);
  }

  @Override
  public int hashCode() {
    int result = this.recoveryPolicy != null ? this.recoveryPolicy.hashCode() : 0;
    result = 31 * result + (this.mfaPolicy != null ? this.mfaPolicy.hashCode() : 0);
    result = 31 * result + (this.isInherited ? 1 : 0);
    return result;
  }
}
