/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.convention.daycount;

import javax.time.calendar.ZonedDateTime;

import org.apache.commons.lang.Validate;

import com.opengamma.financial.analytics.securityconverters.StubCalculator;
import com.opengamma.financial.analytics.securityconverters.StubCalculator.StubType;

/**
 * 
 */
public class AccruedInterestCalculator {

  public double getAccruedInterest(final String daycountName, final ZonedDateTime date, final ZonedDateTime[] schedule, final double coupon, final int paymentsPerYear, final boolean isEOMConvention) {
    Validate.notNull(daycountName, "day-count name");
    Validate.notNull(date, "date");
    Validate.notNull(schedule, "schedule");
    Validate.noNullElements(schedule, "schedule");
    Validate.isTrue(paymentsPerYear > 0);
    final DayCount dayCount = DayCountFactory.INSTANCE.getDayCount(daycountName);
    boolean foundDates = false;
    int index = 0;
    final int length = schedule.length;
    for (int i = 0; i < length - 1; i++) {
      if (schedule[i].isBefore(date) && schedule[i + 1].isAfter(date)) {
        foundDates = true;
        index = i;
      }
    }
    if (!foundDates) {
      throw new IllegalArgumentException("Could not get previous and next coupon for date " + date);
    }
    return getAccruedInterest(dayCount, index, length, schedule[index], date, schedule[index + 1], coupon, paymentsPerYear, isEOMConvention);
  }

  private double getAccruedInterest(final DayCount dayCount, final int index, final int length, final ZonedDateTime previousCouponDate, final ZonedDateTime date, final ZonedDateTime nextCouponDate,
      final double coupon, final int paymentsPerYear, final boolean isEOMConvention) {
    if (dayCount instanceof ActualActualICMANormal) {
      final StubType stubType = getStubType(index, length, previousCouponDate, nextCouponDate, paymentsPerYear, isEOMConvention);
      return ((ActualActualICMANormal) dayCount).getAccruedInterest(previousCouponDate, date, nextCouponDate, coupon, paymentsPerYear, stubType);
    } else if (dayCount instanceof ActualActualICMA) {
      final StubType stubType = getStubType(index, length, previousCouponDate, nextCouponDate, paymentsPerYear, isEOMConvention);
      return ((ActualActualICMA) dayCount).getAccruedInterest(previousCouponDate, date, nextCouponDate, coupon, paymentsPerYear, stubType);
    } else if (dayCount instanceof ThirtyUThreeSixty) {
      return ((ThirtyUThreeSixty) dayCount).getAccruedInterest(previousCouponDate, date, nextCouponDate, coupon, paymentsPerYear, isEOMConvention);
    }
    return dayCount.getAccruedInterest(previousCouponDate, date, nextCouponDate, coupon, paymentsPerYear);

  }

  private StubType getStubType(final int index, final int length, final ZonedDateTime previousCouponDate, final ZonedDateTime nextCouponDate, final int paymentsPerYear,
      final boolean isEOMConvention) {
    StubType stubType;
    if (index == 0) {
      stubType = StubCalculator.getStartStubType(new ZonedDateTime[] {previousCouponDate, nextCouponDate}, paymentsPerYear, isEOMConvention);
    } else if (index == length - 2) {
      stubType = StubCalculator.getEndStubType(new ZonedDateTime[] {previousCouponDate, nextCouponDate}, paymentsPerYear, isEOMConvention);
    } else {
      stubType = StubType.NONE;
    }
    return stubType;
  }
}
