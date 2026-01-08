package com.vayl.identityAccess.core.domain.permission;

import com.vayl.identityAccess.core.domain.api.ApiId;

public class Permission {
  private PermissionId id;
  private String name;
  private ApiId permissionLocation;
  private String description;

  public Permission(PermissionId anId, String aName, ApiId aLocation, String aDescription) {
    this.setId(anId);
    this.setName(aName);
    this.setPermissionLocation(aLocation);
    this.setDescription(aDescription);
  }

  private void setId(PermissionId id) {
    this.id = id;
  }

  private void setName(String name) {
    this.name = name;
  }

  private void setPermissionLocation(ApiId permissionLocation) {
    this.permissionLocation = permissionLocation;
  }

  private void setDescription(String description) {
    this.description = description;
  }

  public PermissionId id() {
    return this.id;
  }

  public ApiId location() {
    return this.permissionLocation;
  }

  public String name() {
    return this.name;
  }

  public String description() {
    return this.description;
  }
}
