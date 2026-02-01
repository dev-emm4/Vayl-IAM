package com.vayl.identityAccess.coreTest.domainTest.apiTest;

import com.vayl.identityAccess.core.domain.api.Api;
import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.api.permission.Permission;
import com.vayl.identityAccess.core.domain.api.permission.PermissionId;
import org.junit.jupiter.api.Test;

public class ApiTest {

  @Test
  void constructor_withValidParameters_createApiCorrectly() {
    ApiId apiId = new ApiId("example.com");
    String apiName = "Test API";
    Api api = new Api(apiId, apiName);

    assert api.id().equals(apiId)
        : "API ID mismatch got " + api.id().toString() + " expected " + apiId;
    assert api.name().equals(apiName)
        : "API name mismatch got " + api.name() + " expected " + apiName;
  }
}
