package com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy;

import com.vayl.identityAccess.core.domain.common.Date;
import com.vayl.identityAccess.core.domain.common.MfaType;

public class MfaPolicy {
  private MfaType mfaType;
  private Date enforcementDate;

  public MfaPolicy(MfaType mfaType, Date enforcementDate) {
    this.setMfaType(mfaType);
    this.setEnforcementDate(enforcementDate);
  }

  private void setMfaType(MfaType mfaType) {
    this.mfaType = mfaType;
  }

  private void setEnforcementDate(Date enforcementDate) {
    this.enforcementDate = enforcementDate;
  }

  public MfaType mfaType() {
    return this.mfaType;
  }

  public Date enforcementDate() {
    return this.enforcementDate;
  }

  @Override
  public String toString() {
    return "MfaPolicy{"
        + "mfaType="
        + this.mfaType
        + ", enforcementDate="
        + this.enforcementDate
        + '}';
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || this.getClass() != obj.getClass()) return false;
    MfaPolicy that = (MfaPolicy) obj;
    return this.mfaType == that.mfaType && this.enforcementDate.equals(that.enforcementDate);
  }

  @Override
  public int hashCode() {
    int result = this.mfaType != null ? this.mfaType.hashCode() : 0;
    result = 31 * result + (this.enforcementDate != null ? this.enforcementDate.hashCode() : 0);
    return result;
  }
}
