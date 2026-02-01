package com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy;

import com.vayl.identityAccess.core.domain.common.MfaType;

public class RecoveryPolicy {
    MfaType mfaType;

    public RecoveryPolicy(MfaType mfaType){
        this.setMfaType(mfaType);
    }

    private void setMfaType(MfaType mfaType) {
        this.mfaType = mfaType;
    }
}
