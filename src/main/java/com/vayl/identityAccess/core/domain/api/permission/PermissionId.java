package com.vayl.identityAccess.core.domain.api.permission;

import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.api.LicenseRestrictable;
import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import org.jspecify.annotations.NonNull;

public record PermissionId(ApiId apiId, String name) implements LicenseRestrictable {
  public PermissionId(@NonNull ApiId apiId, @NonNull String name) {
    throwErrorIfIdIsInvalid(apiId, name);
    this.apiId = apiId;
    this.name = name;
  }

  private void throwErrorIfIdIsInvalid(ApiId apiId, String name) {
    AssertionConcern.isNotNull(apiId, ExceptionReason.INVALID_API_ID);
    AssertionConcern.isNotNull(name, ExceptionReason.INVALID_PERMISSION_NAME);
    AssertionConcern.isNotBlank(name, ExceptionReason.INVALID_PERMISSION_NAME);
  }

  @Override
  public @NonNull String toString() {
    return apiId.toString() + "-" + this.name;
  }
}
