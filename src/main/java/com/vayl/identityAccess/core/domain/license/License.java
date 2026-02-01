package com.vayl.identityAccess.core.domain.license;

public class License {
  private LicenseId id;
  private String name;

  public License(LicenseId id, String name) {
    this.setId(id);
    this.setName(name);
  }

  private void setId(LicenseId id) {
    this.id = id;
  }

  private void setName(String name) {
    this.name = name;
  }

  public LicenseId id() {
    return this.id;
  }

  public String name() {
    return this.name;
  }


}
