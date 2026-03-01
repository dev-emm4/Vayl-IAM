package com.vayl.identityAccess.core.domain.organization.registrationSession;

import com.vayl.identityAccess.core.domain.organization.Initiator;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import com.vayl.identityAccess.core.domain.organization.ou.OuId;
import com.vayl.identityAccess.core.domain.organization.registrationSession.regPhase.MfaRegPhase;
import com.vayl.identityAccess.core.domain.organization.registrationSession.regPhase.ProfileRegPhase;

public class RegistrationSession {
    OrgId orgId;
    OuId ouId;
    RegSessionId id;
    Initiator initiator;
    String endUserId;
    Mode mode;
    ProfileRegPhase profileRegPhase;
    MfaRegPhase mfaRegPhase;
    boolean isOpen;
}
