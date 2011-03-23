/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.schedule;

import static org.junit.Assert.assertArrayEquals;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.Test;
import javax.time.calendar.LocalDate;
import javax.time.calendar.ZonedDateTime;

import com.opengamma.financial.schedule.Schedule;
import com.opengamma.financial.schedule.SemiAnnualScheduleCalculator;
import com.opengamma.util.time.DateUtil;

/**
 * 
 */
public class SemiAnnualScheduleCalculatorTest extends ScheduleCalculatorTestCase {
  private static final SemiAnnualScheduleCalculator CALCULATOR = new SemiAnnualScheduleCalculator();

  @Override
  public Schedule getScheduleCalculator() {
    return CALCULATOR;
  }

  @Test
  public void testStartAndEndEqual1() {
    final LocalDate date = LocalDate.of(2010, 1, 1);
    final LocalDate[] result = CALCULATOR.getSchedule(date, date, true, true);
    assertEquals(result.length, 1);
    assertEquals(result[0], date);
  }

  @Test
  public void testStartAndEndEqual2() {
    final ZonedDateTime date = DateUtil.getUTCDate(2010, 1, 1);
    final ZonedDateTime[] result = CALCULATOR.getSchedule(date, date, true, true);
    assertEquals(result.length, 1);
    assertEquals(result[0], date);
  }

  @Test
  public void test1() {
    LocalDate startDate = LocalDate.of(2000, 1, 1);
    LocalDate endDate = LocalDate.of(2000, 3, 30);
    LocalDate[] forward = CALCULATOR.getSchedule(startDate, endDate, false, false);
    LocalDate[] backward = CALCULATOR.getSchedule(startDate, endDate, true, false);
    assertEquals(forward.length, 1);
    assertEquals(backward.length, 1);
    assertEquals(forward[0], startDate);
    assertEquals(backward[0], endDate);
    startDate = LocalDate.of(2000, 1, 1);
    endDate = LocalDate.of(2002, 4, 9);
    final int periods = 5;
    forward = CALCULATOR.getSchedule(startDate, endDate, false, false);
    backward = CALCULATOR.getSchedule(startDate, endDate, true, false);
    assertEquals(forward.length, periods);
    assertEquals(backward.length, periods);
    assertEquals(forward[0], startDate);
    assertEquals(backward[0], LocalDate.of(2000, 4, 9));
    assertEquals(forward[periods - 1], LocalDate.of(2002, 1, 1));
    assertEquals(backward[periods - 1], endDate);
    for (int i = 1; i < periods; i++) {
      if (forward[i].getYear() == forward[i - 1].getYear()) {
        assertEquals(forward[i].getMonthOfYear().getValue() - forward[i - 1].getMonthOfYear().getValue(), 6);
        assertEquals(backward[i].getMonthOfYear().getValue() - backward[i - 1].getMonthOfYear().getValue(), 6);
      } else {
        assertEquals(forward[i].getMonthOfYear().getValue() - forward[i - 1].getMonthOfYear().getValue(), -6);
        assertEquals(backward[i].getMonthOfYear().getValue() - backward[i - 1].getMonthOfYear().getValue(), -6);
      }
      assertEquals(forward[i].getDayOfMonth(), 1);
      assertEquals(backward[i].getDayOfMonth(), 9);
    }
  }

  @Test
  public void test2() {
    ZonedDateTime startDate = DateUtil.getUTCDate(2000, 1, 1);
    ZonedDateTime endDate = DateUtil.getUTCDate(2000, 3, 30);
    ZonedDateTime[] forward = CALCULATOR.getSchedule(startDate, endDate, false, false);
    ZonedDateTime[] backward = CALCULATOR.getSchedule(startDate, endDate, true, false);
    assertEquals(forward.length, 1);
    assertEquals(backward.length, 1);
    assertEquals(forward[0], startDate);
    assertEquals(backward[0], endDate);
    startDate = DateUtil.getUTCDate(2000, 1, 1);
    endDate = DateUtil.getUTCDate(2002, 4, 9);
    final int quarters = 5;
    forward = CALCULATOR.getSchedule(startDate, endDate, false, false);
    backward = CALCULATOR.getSchedule(startDate, endDate, true, false);
    assertEquals(forward.length, quarters);
    assertEquals(backward.length, quarters);
    assertEquals(forward[0], startDate);
    assertEquals(backward[0], DateUtil.getUTCDate(2000, 4, 9));
    assertEquals(forward[quarters - 1], DateUtil.getUTCDate(2002, 1, 1));
    assertEquals(backward[quarters - 1], endDate);
    for (int i = 1; i < quarters; i++) {
      if (forward[i].getYear() == forward[i - 1].getYear()) {
        assertEquals(forward[i].getMonthOfYear().getValue() - forward[i - 1].getMonthOfYear().getValue(), 6);
        assertEquals(backward[i].getMonthOfYear().getValue() - backward[i - 1].getMonthOfYear().getValue(), 6);
      } else {
        assertEquals(forward[i].getMonthOfYear().getValue() - forward[i - 1].getMonthOfYear().getValue(), -6);
        assertEquals(backward[i].getMonthOfYear().getValue() - backward[i - 1].getMonthOfYear().getValue(), -6);
      }
      assertEquals(forward[i].getDayOfMonth(), 1);
      assertEquals(backward[i].getDayOfMonth(), 9);
    }
  }

  @Test
  public void testEndOfMonth1() {
    final LocalDate startDate = LocalDate.of(2008, 8, 31);
    final LocalDate endDate = LocalDate.of(2010, 8, 31);
    final LocalDate[] fromStart = new LocalDate[] {LocalDate.of(2008, 8, 31), LocalDate.of(2009, 2, 28), LocalDate.of(2009, 8, 31), LocalDate.of(2010, 2, 28), LocalDate.of(2010, 8, 31)};
    final LocalDate[] fromStartRecursive = new LocalDate[] {LocalDate.of(2008, 8, 31), LocalDate.of(2009, 2, 28), LocalDate.of(2009, 8, 28), LocalDate.of(2010, 2, 28), LocalDate.of(2010, 8, 28)};
    final LocalDate[] fromEnd = new LocalDate[] {LocalDate.of(2008, 8, 31), LocalDate.of(2009, 2, 28), LocalDate.of(2009, 8, 31), LocalDate.of(2010, 2, 28), LocalDate.of(2010, 8, 31)};
    final LocalDate[] fromEndRecursive = new LocalDate[] {LocalDate.of(2009, 2, 28), LocalDate.of(2009, 8, 28), LocalDate.of(2010, 2, 28), LocalDate.of(2010, 8, 31)};
    assertArrayEquals(fromStart, CALCULATOR.getSchedule(startDate, endDate, false, false));
    assertArrayEquals(fromStartRecursive, CALCULATOR.getSchedule(startDate, endDate, false, true));
    assertArrayEquals(fromEnd, CALCULATOR.getSchedule(startDate, endDate, true, false));
    assertArrayEquals(fromEndRecursive, CALCULATOR.getSchedule(startDate, endDate, true, true));
  }

  @Test
  public void testEndOfMonth2() {
    final ZonedDateTime startDate = DateUtil.getUTCDate(2008, 8, 31);
    final ZonedDateTime endDate = DateUtil.getUTCDate(2010, 8, 31);
    final ZonedDateTime[] fromStart = new ZonedDateTime[] {DateUtil.getUTCDate(2008, 8, 31), DateUtil.getUTCDate(2009, 2, 28), DateUtil.getUTCDate(2009, 8, 31), DateUtil.getUTCDate(2010, 2, 28),
        DateUtil.getUTCDate(2010, 8, 31)};
    final ZonedDateTime[] fromStartRecursive = new ZonedDateTime[] {DateUtil.getUTCDate(2008, 8, 31), DateUtil.getUTCDate(2009, 2, 28), DateUtil.getUTCDate(2009, 8, 28),
        DateUtil.getUTCDate(2010, 2, 28), DateUtil.getUTCDate(2010, 8, 28)};
    final ZonedDateTime[] fromEnd = new ZonedDateTime[] {DateUtil.getUTCDate(2008, 8, 31), DateUtil.getUTCDate(2009, 2, 28), DateUtil.getUTCDate(2009, 8, 31), DateUtil.getUTCDate(2010, 2, 28),
        DateUtil.getUTCDate(2010, 8, 31)};
    final ZonedDateTime[] fromEndRecursive = new ZonedDateTime[] {DateUtil.getUTCDate(2009, 2, 28), DateUtil.getUTCDate(2009, 8, 28), DateUtil.getUTCDate(2010, 2, 28),
        DateUtil.getUTCDate(2010, 8, 31)};
    assertArrayEquals(fromStart, CALCULATOR.getSchedule(startDate, endDate, false, false));
    assertArrayEquals(fromStartRecursive, CALCULATOR.getSchedule(startDate, endDate, false, true));
    assertArrayEquals(fromEnd, CALCULATOR.getSchedule(startDate, endDate, true, false));
    assertArrayEquals(fromEndRecursive, CALCULATOR.getSchedule(startDate, endDate, true, true));
  }

}
