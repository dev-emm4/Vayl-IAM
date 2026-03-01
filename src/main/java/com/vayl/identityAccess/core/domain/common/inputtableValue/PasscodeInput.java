//package com.vayl.identityAccess.core.domain.common.inputtableValue;
//
//import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
//import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
//import java.util.Objects;
//
//public record PasscodeInput(String value) implements InputtableValue {
//  public PasscodeInput(String value) {
//    if (value == null || value.trim().isEmpty()) {
//      throw new InvalidValueException(ExceptionReason.INVALID_PASSCODE);
//    }
//    String trimmed = value.trim();
//    if (trimmed.length() < 4 || trimmed.length() > 32) {
//      throw new InvalidValueException(ExceptionReason.INVALID_PASSCODE);
//    }
//    this.value = trimmed;
//  }
//
//  @Override
//  public boolean equals(Object o) {
//    if (this == o) return true;
//    if (o == null || getClass() != o.getClass()) return false;
//    PasscodeInput that = (PasscodeInput) o;
//    return Objects.equals(value, that.value);
//  }
//
//  @Override
//  public String toString() {
//    return value;
//  }
//}
