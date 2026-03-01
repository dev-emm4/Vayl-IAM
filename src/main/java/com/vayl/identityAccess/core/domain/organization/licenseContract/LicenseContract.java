package com.vayl.identityAccess.core.domain.organization.licenseContract;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.inputtableValue.DateInput;
import com.vayl.identityAccess.core.domain.license.LicenseId;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import org.jspecify.annotations.NonNull;

public class LicenseContract {
  private LicenseContractId id;
  private int amountAllocated;
  private int amountRemaining;
  private DateInput expireAt;

  public LicenseContract(
      @NonNull LicenseContractId id,
      int amountAllocated,
      int amountRemaining,
      @NonNull DateInput expireAt) {
    this.setId(id);
    this.setAmountAllocated(amountAllocated);
    this.setAmountRemaining(amountRemaining);
    this.setExpireAt(expireAt);
  }

  private void setId(LicenseContractId id) {
    AssertionConcern.isNotNull(id, ExceptionReason.INVALID_LICENSE_CONTRACT_ARG);
    this.id = id;
  }

  private void setAmountAllocated(int amountAllocated) {
    this.amountAllocated = amountAllocated;
  }

  private void setAmountRemaining(int amountRemaining) {
    this.amountRemaining = amountRemaining;
  }

  private void setExpireAt(DateInput expireAt) {
    AssertionConcern.isNotNull(expireAt, ExceptionReason.INVALID_LICENSE_CONTRACT_ARG);
    this.expireAt = expireAt;
  }

  public LicenseContractId id() {
    return this.id;
  }

  public OrgId orgId() {
    return this.id.orgId();
  }

  public LicenseId licenseId() {
    return this.id().licenseId();
  }

  public int amountAllocated() {
    return this.amountAllocated;
  }

  public int amountRemaining() {
    return this.amountRemaining;
  }

  public DateInput expireAt() {
    return this.expireAt;
  }

  public void decreasingAmountRemaining() {
    // TODO: implement decreasingAmountRemaining in licenseContract
  }

  public void increaseAmountRemaining() {
    this.amountRemaining += 1;

    // TODO: publish increased_license_contract event
  }

  public void increaseAllocatedAmount(int additionalAmount) {
    this.amountAllocated += additionalAmount;
    this.amountRemaining += additionalAmount;

    // TODO: publish increased_license_contract event
  }
}
