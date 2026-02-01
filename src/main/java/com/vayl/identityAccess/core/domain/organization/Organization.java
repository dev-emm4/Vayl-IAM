package com.vayl.identityAccess.core.domain.organization;

public class Organization {
  OrgId orgId;
  String name;

  public Organization(OrgId orgId, String name) {
    this.setOrgId(orgId);
    this.setName(name);
    // TODO: Publish organization_created event
  }

  private void setOrgId(OrgId orgId) {
    this.orgId = orgId;
  }

  private void setName(String name) {
    this.name = name;
  }

  public OrgId id() {
    return this.orgId;
  }

  public String name() {
    return this.name;
  }
}
