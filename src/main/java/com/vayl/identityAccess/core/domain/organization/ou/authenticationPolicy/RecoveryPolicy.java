package com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy;

import com.vayl.identityAccess.core.domain.common.MfaType;

public class RecoveryPolicy {
  private MfaType mfaType;

  public RecoveryPolicy(MfaType mfaType) {
    this.setMfaType(mfaType);
  }

  private void setMfaType(MfaType mfaType) {
    this.mfaType = mfaType;
  }

  public MfaType mfaType() {
    return this.mfaType;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    RecoveryPolicy that = (RecoveryPolicy) obj;
    return this.mfaType == that.mfaType;
  }

  @Override
  public int hashCode() {
    return this.mfaType != null ? this.mfaType.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "RecoveryPolicy{" + "mfaType=" + this.mfaType + '}';
  }
}
