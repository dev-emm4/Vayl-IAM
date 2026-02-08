package com.vayl.identityAccess.coreTest.domainTest.licenseTest.licenseRestrictionRegistryTest;

import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.api.LicenseRestrictable;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.license.LicenseId;
import com.vayl.identityAccess.core.domain.license.licenseRestrictionRegistry.LicenseRestriction;
import com.vayl.identityAccess.core.domain.license.licenseRestrictionRegistry.LicenseRestrictionRegistry;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class LicenseRestrictionRegistryTest {
  @Test
  public void updateLicenseRestriction_withUpdatedRestrictions_updatesRegistryCorrectly() {
    LicenseRestriction licenseRestriction = this.createLicenseRestriction();
    LicenseRestrictionRegistry registry =
        createRegistryWithRestrictions(List.of(licenseRestriction));

    // update the blocking licenses for the existing license restriction
    LicenseRestriction updatedRestriction =
        new LicenseRestriction(
            licenseRestriction.licenseRestrictable(),
            new HashSet<>(List.of(new LicenseId(UUID.randomUUID().toString()))));

    List<LicenseRestriction> updatedRestrictions = List.of(updatedRestriction);

    registry.updateLicenseRestriction(updatedRestrictions);

    assert registry
            .getLicenseRestriction(licenseRestriction.licenseRestrictable())
            .equals(updatedRestriction)
        : "LicenseRestrictionRegistry did not update the license restriction correctly.";
  }

  @Test
  public void removeLicenseRestrictions_withExistingRestriction_removesRestrictionFromRegistry() {
    LicenseRestriction licenseRestriction = this.createLicenseRestriction();
    LicenseRestrictionRegistry registry =
        createRegistryWithRestrictions(List.of(licenseRestriction));

    registry.removeLicenseRestrictions(List.of(licenseRestriction.licenseRestrictable()));

    assert registry.getLicenseRestriction(licenseRestriction.licenseRestrictable()) == null
        : "LicenseRestrictionRegistry did not remove the license restriction correctly.";

  }

  @Test
    public void removeLicenseRestrictions_withNonExistingRestriction_throwsInvalidValueException() {
        LicenseRestrictionRegistry registry = createRegistryWithRestrictions(List.of());

        LicenseRestrictable nonExistingRestrictable = new ApiId("non-existing.com");

        try {
        registry.removeLicenseRestrictions(List.of(nonExistingRestrictable));
        assert false
            : "Expected an exception to be thrown when trying to remove a non-existing license restriction.";
        } catch (InvalidValueException e) {
        assert e.event() == ExceptionEvent.REMOVING_LICENSE_RESTRICTION
            : "InvalidValueException event mismatch got: "
                + e.event()
                + " expected: "
                + ExceptionEvent.REMOVING_LICENSE_RESTRICTION;

        assert e.reason() == ExceptionReason.INVALID_LICENSE_RESTRICTION_KEY
            : "InvalidValueException reason mismatch got: "
                + e.reason()
                + " expected: "
                + ExceptionReason.INVALID_LICENSE_RESTRICTION_KEY;

        assert e.level() == ExceptionLevel.INFO
            : "InvalidValueException level mismatch got: "
                + e.level()
                + " expected: "
                + ExceptionLevel.INFO;
        }
    }


  @Test
  public void updateLicenseRestriction_withNewRestriction_addsRestrictionToRegistry() {
    LicenseRestrictionRegistry registry = createRegistryWithRestrictions(List.of());

    LicenseRestriction newRestriction = this.createLicenseRestriction();

    List<LicenseRestriction> updatedRestrictions = List.of(newRestriction);

    registry.updateLicenseRestriction(updatedRestrictions);

    assert registry
            .getLicenseRestriction(newRestriction.licenseRestrictable())
            .equals(newRestriction)
        : "LicenseRestrictionRegistry did not add the new license restriction correctly.";
  }

  @Test
  public void canAccessLicenseRestrictable_WhenInitializedWithBlockingLicense_ReturnsFalse() {
    LicenseRestriction licenseRestriction = this.createLicenseRestriction();
    LicenseRestrictionRegistry registry =
        createRegistryWithRestrictions(List.of(licenseRestriction));

    // Initialize assigned licenses with one of the blocking licenses
    List<LicenseId> assignedLicenses =
        List.of(licenseRestriction.blockingLicenses().iterator().next());
    registry.initializeAssignedLicense(assignedLicenses);

    boolean canAccess =
        registry.canAccessLicenseRestrictable(licenseRestriction.licenseRestrictable());

    assert !canAccess : "Expected access to be denied, but it was granted.";
  }

  @Test
  public void canAccessLicenseRestrictable_WhenInitializedWithoutBlockingLicense_ReturnsTrue() {
    LicenseRestriction licenseRestriction = this.createLicenseRestriction();
    LicenseRestrictionRegistry registry =
        createRegistryWithRestrictions(List.of(licenseRestriction));

    // Initialize assigned licenses without any of the blocking licenses
    List<LicenseId> assignedLicenses = List.of(new LicenseId(UUID.randomUUID().toString()));
    registry.initializeAssignedLicense(assignedLicenses);

    boolean canAccess =
        registry.canAccessLicenseRestrictable(licenseRestriction.licenseRestrictable());

    assert canAccess : "Expected access to be granted, but it was denied.";
  }

  @Test
  public void canAccessLicenseRestrictable_WhenNoRestrictionExists_ReturnsTrue() {
    LicenseRestrictionRegistry registry = createRegistryWithRestrictions(List.of());

    LicenseRestrictable licenseRestrictable = new ApiId("non-restricted.com");

    // Initialize assigned licenses with any licenses
    List<LicenseId> assignedLicenses = List.of(new LicenseId(UUID.randomUUID().toString()));
    registry.initializeAssignedLicense(assignedLicenses);

    boolean canAccess = registry.canAccessLicenseRestrictable(licenseRestrictable);

    assert canAccess : "Expected access to be granted for non-restricted license restrictable.";
  }

  @Test
  public void canAccessLicenseRestrictable_WhenNotInitialized_ThrowsInvalidValueException() {
    LicenseRestrictionRegistry registry = createRegistryWithRestrictions(List.of());

    LicenseRestrictable licenseRestrictable = new ApiId("example.com");

    try {
      registry.canAccessLicenseRestrictable(licenseRestrictable);
      assert false
          : "Expected an exception to be thrown when checking access without initialization.";
    } catch (InvalidValueException e) {
      assert e.event() == ExceptionEvent.CHECKING_ACCESS_TO_LICENSE_RESTRICTABLE
          : "InvalidValueException event mismatch got: "
              + e.event()
              + " expected: "
              + ExceptionEvent.CHECKING_ACCESS_TO_LICENSE_RESTRICTABLE;

      assert e.reason() == ExceptionReason.LICENSE_RESTRICTION_REGISTRY_NOT_INITIALIZED
          : "InvalidValueException reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.LICENSE_RESTRICTION_REGISTRY_NOT_INITIALIZED;

      assert e.level() == ExceptionLevel.ERROR
          : "InvalidValueException level mismatch got: "
              + e.level()
              + " expected: "
              + ExceptionLevel.ERROR;
    }
  }

  private LicenseRestrictionRegistry createRegistryWithRestrictions(
      List<LicenseRestriction> licenseRestrictions) {
    return new LicenseRestrictionRegistry(licenseRestrictions);
  }

  private LicenseRestriction createLicenseRestriction() {
    LicenseRestrictable apiId = new ApiId("example.com");
    Set<LicenseId> blockingLicenses =
        new HashSet<>(
            List.of(
                new LicenseId(UUID.randomUUID().toString()),
                new LicenseId(UUID.randomUUID().toString())));
    return new LicenseRestriction(apiId, blockingLicenses);
  }
}
