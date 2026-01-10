package com.vayl.identityAccess.core.domain.api;

import com.vayl.identityAccess.core.domain.permission.Permission;
import com.vayl.identityAccess.core.domain.permission.PermissionId;

public class Api {
  private ApiId id;
  private String name;

  public Api(ApiId id, String name) {
    this.setId(id);
    this.setName(name);
  }

  private void setId(ApiId id) {
    this.id = id;
  }

  private void setName(String name) {
    this.name = name;
  }

  public Permission createPermission(String name, String description) {
    PermissionId permissionId = new PermissionId(this.id, name);
    return new Permission(permissionId, name, this.id, description);
  }

  public ApiId id() {
    return this.id;
  }

  public String name() {
    return this.name;
  }
}
