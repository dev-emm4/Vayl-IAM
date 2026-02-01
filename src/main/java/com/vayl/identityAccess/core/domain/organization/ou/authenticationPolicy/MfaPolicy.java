package com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy;

import com.vayl.identityAccess.core.domain.common.Date;
import com.vayl.identityAccess.core.domain.common.MfaType;

public class MfaPolicy {
  MfaType mfaType;
  Date enforcementDate;

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
}
