package com.vayl.identityAccess.core.domain.api.permission;

import com.vayl.identityAccess.core.domain.api.ApiId;

public class Permission {
  private PermissionId id;
  private String description;

  public Permission(PermissionId anId, String aDescription) {
    this.setId(anId);
    this.setDescription(aDescription);
  }

  private void setId(PermissionId id) {
    this.id = id;
  }

  private void setDescription(String description) {
    this.description = description;
  }

  public PermissionId id() {
    return this.id;
  }

  public ApiId location() {
    return this.id().permissionLocation();
  }

  public String name() {
    return this.id().name();
  }

  public String description() {
    return this.description;
  }
}
