package com.vayl.identityAccess.core.domain.organization.licenseContract;

import com.vayl.identityAccess.core.domain.common.Date;
import com.vayl.identityAccess.core.domain.organization.OrgId;

public class LicenseContract {
  private LicenseContractId id;
  private int amountAllocated;
  private int amountRemaining;
  private Date expireAt;

  public LicenseContract(
      LicenseContractId id, int amountAllocated, int amountRemaining, Date expireAt) {
    this.setId(id);
    this.setAmountAllocated(amountAllocated);
    this.setAmountRemaining(amountRemaining);
    this.setExpireAt(expireAt);
  }

  private void setId(LicenseContractId id) {
    this.id = id;
  }

  private void setAmountAllocated(int amountAllocated) {
    this.amountAllocated = amountAllocated;
  }

  private void setAmountRemaining(int amountRemaining) {
    this.amountRemaining = amountRemaining;
  }

  private void setExpireAt(Date expireAt) {
    this.expireAt = expireAt;
  }

  public LicenseContractId id() {
    return this.id;
  }

  public OrgId orgId() {
    return this.id.orgId();
  }

  public int amountAllocated() {
    return this.amountAllocated;
  }

  public int amountRemaining() {
    return this.amountRemaining;
  }

  public Date expireAt() {
    return this.expireAt;
  }

  public void decreasingAmountRemaining(){
    //TODO: implement decreasingAmountRemaining in licenseContract
  }

  public void increaseAmountRemaining(){
    this.amountRemaining += 1;

    //TODO: publish increased_license_contract event
  }

  public void increaseAllocatedAmount(int additionalAmount){
    this.amountAllocated += additionalAmount;
    this.amountRemaining += additionalAmount;

    //TODO: publish increased_license_contract event
  }
}
