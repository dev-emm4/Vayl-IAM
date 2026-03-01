package com.vayl.identityAccess.core.domain.api.permission;

import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import org.jspecify.annotations.NonNull;

public class Permission {
  private PermissionId id;
  private String description;

  public Permission(@NonNull PermissionId id, String description) {
    this.setId(id);
    this.setDescription(description);
  }

  private void setId(PermissionId id) {
    AssertionConcern.isNotNull(id, ExceptionReason.INVALID_PERMISSION_ARG);
    this.id = id;
  }

  private void setDescription(String description) {
    this.description = description;
  }

  public PermissionId id() {
    return this.id;
  }

  public ApiId location() {
    return this.id().apiId();
  }

  public String name() {
    return this.id().name();
  }

  public String description() {
    return this.description;
  }
}
