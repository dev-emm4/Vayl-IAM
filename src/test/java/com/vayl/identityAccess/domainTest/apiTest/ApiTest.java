package com.vayl.identityAccess.domainTest.apiTest;

import com.vayl.identityAccess.core.domain.api.Api;
import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.permission.Permission;
import com.vayl.identityAccess.core.domain.permission.PermissionId;
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

  @Test
  void createPermission_createsPermissionWithCorrectly() {
    String name = "CREATE_USER";
    String description = "Permission to create a user";
    ApiId apiId = new ApiId("example.com");
    Api api = new Api(apiId, "Test API");

    Permission permission = api.createPermission(name, description);
    PermissionId expectedPermissionId = new PermissionId(apiId, name);

    assert permission.id().toString().equals(expectedPermissionId.toString())
        : "Permission ID mismatch got "
            + permission.id().toString()
            + " expected "
            + apiId.toString()
            + name;
    assert permission.name().equals(name)
        : "name mismatch got " + permission.name() + " expected " + name;
    assert permission.description().equals(description)
        : "Description mismatch got " + permission.description() + " expected " + description;
    assert permission.location().equals(apiId)
        : "Location mismatch got " + permission.location() + " expected " + apiId;
  }
}
