package com.vayl.identityAccess.core.domain.common.validator;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.jspecify.annotations.NonNull;

public class PhoneValidator implements Validator {
  private final PhoneNumberUtil util = PhoneNumberUtil.getInstance();

  public boolean isValid(@NonNull String phoneNumber) {
    try {
      //      String normalized = countryCode.trim() + subscriberNumber.trim();
      Phonenumber.PhoneNumber pn = util.parse(phoneNumber, null);
      return util.isValidNumber(pn);
    } catch (NumberParseException e) {
      return false;
    }
  }
}
