package com.vayl.identityAccess.coreTest.domainTest.apiTest.roleTest;

import com.vayl.identityAccess.core.domain.api.Api;
import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.api.permission.PermissionId;
import com.vayl.identityAccess.core.domain.api.role.CustomRole;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomRoleTest {
  OrgId orgId;
  Api api;
  List<PermissionId> permissionIds;

  @BeforeAll
  public void InitAll() {
    this.orgId = new OrgId(UUID.randomUUID().toString());
    this.api = new Api(new ApiId("example.com"), "admin");
    this.permissionIds = this.createPermissionIds(this.api, 6, "create-user");
  }

  @Test
  public void createCustomRole_withRequiredParameter_createCustomRole() {
    String roleName = "accountant";
    CustomRole role = this.api.createCustomRole(roleName, this.orgId, this.permissionIds);

    assert role.belongsTo(this.orgId) : "custom role should belong to org";
    assert role.assignedApiIds().equals(this.api.id())
        : "assigned API ID mismatch got "
            + role.assignedApiIds().toString()
            + " expected "
            + this.api.id().toString();
    assert role.name().equals("accountant")
        : "role name mismatch got " + role.name() + " expected " + roleName;
    assert role.assignedPermissionIds().equals(this.permissionIds)
        : "role assignedPermissionIds mismatch got "
            + role.assignedPermissionIds()
            + " expected "
            + this.permissionIds;
  }

  private @NonNull List<PermissionId> createPermissionIds(Api api, int amount, String name) {
    List<PermissionId> permissionIds = new ArrayList<>();

    for (int i = 0; i < amount; i++) {
      permissionIds.add(api.createPermission(name + 1, "").id());
    }

    return permissionIds;
  }
}
