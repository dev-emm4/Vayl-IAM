//package com.vayl.identityAccess.coreTest.domainTest.commonTest.inputtableValueTest;
//
//import com.vayl.identityAccess.core.domain.common.inputtableValue.PasscodeInput;
//import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
//import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
//import org.junit.jupiter.api.Test;
//
//public class PasscodeInputTest {
//
//    @Test
//    void of_withShortPasscode_throwsInvalidValueException() {
//        try {
//            new PasscodeInput("abc");
//            assert false : "Expected InvalidValueException was not thrown for short passcode";
//        } catch (InvalidValueException e) {
//            assert e.reason() == ExceptionReason.INVALID_PASSCODE;
//        }
//    }
//
//    @Test
//    void of_withValidPasscode_createsPasscode() {
//        PasscodeInput p = new PasscodeInput("secret123");
//        assert p != null;
//        assert p.value().equals("secret123");
//    }
//}
//
