package com.vayl.identityAccess.core.domain.api;

import com.vayl.identityAccess.core.domain.permission.Permission;
import com.vayl.identityAccess.core.domain.permission.PermissionId;

public class Api {
  private ApiId id;
  private String name;

  public Api(ApiId anId, String aName) {
    this.setId(anId);
    this.setName(aName);
  }

  private void setId(ApiId anId) {
    this.id = anId;
  }

  private void setName(String aName) {
    this.name = aName;
  }

  public Permission createPermission(String aName, String aDescription) {
    PermissionId permissionId = new PermissionId(this.id, aName);
    return new Permission(permissionId, aName, this.id, aDescription);
  }

  public ApiId id() {
    return this.id;
  }

  public String name() {
    return this.name;
  }
}
