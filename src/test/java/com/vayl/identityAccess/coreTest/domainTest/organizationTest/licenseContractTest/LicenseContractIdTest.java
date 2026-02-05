package com.vayl.identityAccess.coreTest.domainTest.organizationTest.licenseContractTest;

import com.vayl.identityAccess.core.domain.license.LicenseId;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import com.vayl.identityAccess.core.domain.organization.licenseContract.LicenseContractId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class LicenseContractIdTest {
    @Test
    public void equals_withSameId_returnsTrue(){
        String validOrgId = UUID.randomUUID().toString();
        String validLicenseId = UUID.randomUUID().toString();
        LicenseContractId id1 = new LicenseContractId(new OrgId(validOrgId), new LicenseId(validLicenseId));
        LicenseContractId id2 = new LicenseContractId(new OrgId(validOrgId), new LicenseId(validLicenseId));

        assert id1.equals(id2)
                : "LicenseContractId equality mismatch for same ids got: false expected: true";
    }

    @Test
    public void equals_withDifferentId_returnsFalse(){
        String validOrgId1 = UUID.randomUUID().toString();
        String validLicenseId1 = UUID.randomUUID().toString();
        String validOrgId2 = UUID.randomUUID().toString();
        String validLicenseId2 = UUID.randomUUID().toString();
        LicenseContractId id1 = new LicenseContractId(new OrgId(validOrgId1), new LicenseId(validLicenseId1));
        LicenseContractId id2 = new LicenseContractId(new OrgId(validOrgId2), new LicenseId(validLicenseId2));

        assert !id1.equals(id2)
                : "LicenseContractId equality mismatch for different ids got: true expected: false";
    }

    @Test
    public void toString_returnsCorrectFormat(){
        String validOrgId = UUID.randomUUID().toString();
        String validLicenseId = UUID.randomUUID().toString();
        LicenseContractId id = new LicenseContractId(new OrgId(validOrgId), new LicenseId(validLicenseId));
        String expectedString = "LicenseContractId{orgId=" + new OrgId(validOrgId) + ", licenseId=" + new LicenseId(validLicenseId) + "}";

        assert id.toString().equals(expectedString)
                : "LicenseContractId toString mismatch got: " + id.toString() + " expected: " + expectedString;
    }

    @Test
    public void hashCode_withSameId_returnsSameHashCode(){
        String validOrgId = UUID.randomUUID().toString();
        String validLicenseId = UUID.randomUUID().toString();
        LicenseContractId id1 = new LicenseContractId(new OrgId(validOrgId), new LicenseId(validLicenseId));
        LicenseContractId id2 = new LicenseContractId(new OrgId(validOrgId), new LicenseId(validLicenseId));

        assert id1.hashCode() == id2.hashCode()
                : "LicenseContractId hashCode mismatch for same ids got: "
                + id1.hashCode()
                + " expected: "
                + id2.hashCode();
    }

    @Test
    public void hashCode_withDifferentId_returnsDifferentHashCode(){
        String validOrgId1 = UUID.randomUUID().toString();
        String validLicenseId1 = UUID.randomUUID().toString();
        String validOrgId2 = UUID.randomUUID().toString();
        String validLicenseId2 = UUID.randomUUID().toString();
        LicenseContractId id1 = new LicenseContractId(new OrgId(validOrgId1), new LicenseId(validLicenseId1));
        LicenseContractId id2 = new LicenseContractId(new OrgId(validOrgId2), new LicenseId(validLicenseId2));

        assert id1.hashCode() != id2.hashCode()
                : "LicenseContractId hashCode mismatch for different ids got: "
                + id1.hashCode()
                + " expected: different from "
                + id2.hashCode();
    }
}
