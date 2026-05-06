package com.vayl.identityAccess.core.domain.common;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;

class ScheduleTest {
  @Test
  void constructor_withInvalidSchedule_throwException() {
    String invalidSchedule = "invalid-schedule";

    try {
      new Schedule(invalidSchedule);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_SCHEDULE)
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_SCHEDULE;
    }
  }

  @Test
  void constructor_withImpossibleSchedule_throwException() {
    String impossibleSchedule = "2023-02-30T00:00:00Z"; // February 30th does not exist

    try {
      new Schedule(impossibleSchedule);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_SCHEDULE)
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_SCHEDULE;
    }
  }

  @Test
  void constructor_withNullSchedule_throwException() {
    try {
      new Schedule(null);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_SCHEDULE)
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_SCHEDULE;
    }
  }

  @Test
  void constructor_withOffsetDateTime_createsEnforcementScheduleInUTC() {
    String validSchedule = "2026-03-08T15:30:00+01:00";
    Schedule schedule = new Schedule(validSchedule);

    assert schedule.schedule().equals("2026-03-08T14:30:00Z")
        : "got: " + schedule.schedule() + " expected: 2026-03-08T14:30:00Z";
  }

  @Test
  void constructor_withZeroSchedule_createsEnforcementSchedule() {
    String zeroSchedule = "0";
    Schedule schedule = new Schedule(zeroSchedule);

    assert schedule.schedule().equals(zeroSchedule)
        : "got: " + schedule.schedule() + " expected: " + zeroSchedule;
  }

  @Test
  void constructor_withUTCOffsetDateTime_createsEnforcementSchedule() {
    String validSchedule = "2026-03-08T14:30:00Z";
    Schedule schedule = new Schedule(validSchedule);

    assert schedule.schedule().equals(validSchedule)
        : "got: " + schedule.schedule() + " expected: " + validSchedule;
  }

  @Test
  void isDue_withFutureSchedule_returnsFalse() {
    String futureSchedule = OffsetDateTime.now().plusDays(1).toString();
    Schedule schedule = new Schedule(futureSchedule);

    assert !schedule.isDue() : "Expected schedule to be due";
  }

  @Test
  void isDue_withPastSchedule_returnsTrue() {
    String pastSchedule = OffsetDateTime.now().minusDays(1).toString();
    Schedule schedule = new Schedule(pastSchedule);

    assert schedule.isDue() : "Expected schedule to be due";
  }

  @Test
  void isDue_withZeroSchedule_returnsFalse() {
    Schedule schedule = new Schedule("0");

    assert !schedule.isDue() : "Expected schedule to not be due";
  }
}
