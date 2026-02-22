package com.vayl.identityAccess.core.domain.api.role;

import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.organization.OrgId;

public interface Role {
    ApiId assignedApiId();
    boolean accessibleBy(OrgId orgId);
    RoleId id();
}
