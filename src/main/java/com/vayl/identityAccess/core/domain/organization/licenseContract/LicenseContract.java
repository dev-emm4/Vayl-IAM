package com.vayl.identityAccess.core.domain.organization.licenseContract;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.inputtableValue.DateInput;
import com.vayl.identityAccess.core.domain.license.LicenseId;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import org.jspecify.annotations.NonNull;

public class LicenseContract {
  private LicenseContractId id;
  private Integer amountAllocated;
  private Integer amountRemaining;
  private DateInput expireAt;

  public LicenseContract(
      @NonNull LicenseContractId id,
      @NonNull Integer amountAllocated,
      @NonNull Integer amountRemaining,
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

  private void setAmountAllocated(Integer amountAllocated) {
    AssertionConcern.isNotNull(amountAllocated, ExceptionReason.INVALID_LICENSE_CONTRACT_ARG);
    this.amountAllocated = amountAllocated;
  }

  private void setAmountRemaining(Integer amountRemaining) {
    AssertionConcern.isNotNull(amountRemaining, ExceptionReason.INVALID_LICENSE_CONTRACT_ARG);
    this.amountRemaining = amountRemaining;
  }

  private void setExpireAt(DateInput expireAt) {
    AssertionConcern.isNotNull(expireAt, ExceptionReason.INVALID_LICENSE_CONTRACT_ARG);
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
    AssertionConcern.isNotNull(additionalAmount, ExceptionReason.INVALID_LICENSE_CONTRACT_ARG);

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

  public @NonNull DateInput expireAt() {
    return this.expireAt;
  }
}
