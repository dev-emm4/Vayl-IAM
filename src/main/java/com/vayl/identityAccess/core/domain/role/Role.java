package com.vayl.identityAccess.core.domain.role;

import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.organization.OrgId;

public interface Role {
    ApiId assignedApi();
    boolean belongsTo(OrgId orgId);
    RoleId id();
}
