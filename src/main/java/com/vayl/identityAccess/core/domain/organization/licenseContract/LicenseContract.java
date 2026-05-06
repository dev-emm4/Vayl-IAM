package com.vayl.identityAccess.core.domain.organization.licenseContract;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.Schedule;
import com.vayl.identityAccess.core.domain.license.LicenseId;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import org.jspecify.annotations.NonNull;

public class LicenseContract {
  private LicenseContractId id;
  private Integer amountAllocated;
  private Integer amountRemaining;
  private Schedule expireAt;

  public LicenseContract(
      @NonNull LicenseContractId id,
      @NonNull Integer amountAllocated,
      @NonNull Integer amountRemaining,
      @NonNull Schedule expireAt) {
    this.setId(id);
    this.setAmountAllocated(amountAllocated);
    this.setAmountRemaining(amountRemaining);
    this.setExpireAt(expireAt);
  }

  private void setId(LicenseContractId id) {
    AssertionConcern.isNotNull(id, ExceptionReason.INVALID_LICENSE_CONTRACT_ID);
    this.id = id;
  }

  private void setAmountAllocated(Integer amountAllocated) {
    AssertionConcern.isNotNull(amountAllocated, ExceptionReason.INVALID_AMOUNT_ALLOCATED);
    this.amountAllocated = amountAllocated;
  }

  private void setAmountRemaining(Integer amountRemaining) {
    AssertionConcern.isNotNull(amountRemaining, ExceptionReason.INVALID_AMOUNT_REMAINING);
    this.amountRemaining = amountRemaining;
  }

  private void setExpireAt(Schedule expireAt) {
    AssertionConcern.isNotNull(expireAt, ExceptionReason.INVALID_EXPIRY_DATE);
    this.expireAt = expireAt;
  }

  public void decreasingAmountRemaining() {
    // TODO: implement decreasingAmountRemaining in licenseContract
  }

  public void increaseAmountRemaining() {
    this.amountRemaining += 1;

    // TODO: publish increased_license_contract event
  }

  public void increaseAllocatedAmount(@NonNull Integer additionalAmount) {
    AssertionConcern.isNotNull(additionalAmount, ExceptionReason.INVALID_ADDITIONAL_AMOUNT);

    this.amountAllocated += additionalAmount;
    this.amountRemaining += additionalAmount;

    // TODO: publish increased_license_contract event
  }

  public @NonNull LicenseContractId id() {
    return this.id;
  }

  public @NonNull OrgId orgId() {
    return this.id.orgId();
  }

  public @NonNull LicenseId licenseId() {
    return this.id().licenseId();
  }

  public int amountAllocated() {
    return this.amountAllocated;
  }

  public int amountRemaining() {
    return this.amountRemaining;
  }

  public @NonNull Schedule expireAt() {
    return this.expireAt;
  }
}
