package com.vayl.identityAccess.core.domain.common;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.validator.DateTimeValidator;
import java.time.Instant;
import java.time.OffsetDateTime;
import org.jspecify.annotations.NonNull;

public record Schedule(String schedule) {
  public Schedule(@NonNull String schedule) {
    AssertionConcern.isNotNull(schedule, ExceptionReason.INVALID_SCHEDULE);

    if (schedule.equals("0")) {
      this.schedule = schedule;
      return;
    }

    AssertionConcern.isValid(new DateTimeValidator(), schedule, ExceptionReason.INVALID_SCHEDULE);

    this.schedule = convertToUTC(schedule).toString();
  }

  private Instant convertToUTC(String schedule) {
    OffsetDateTime userTime = OffsetDateTime.parse(schedule);

    return userTime.toInstant();
  }

  public boolean isDue() {
    if (this.schedule().equals("0")) return false;

    Instant storedDate = Instant.parse(this.schedule);
    Instant now = Instant.now();

    return storedDate.isBefore(now);
  }
}

/*
{
  "scheduledTime": "2026-03-08T15:30:00+01:00"
}
* */
