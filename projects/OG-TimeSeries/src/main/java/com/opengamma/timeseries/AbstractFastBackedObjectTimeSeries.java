/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.timeseries;

import java.util.TimeZone;

import org.threeten.bp.ZoneId;

import com.opengamma.timeseries.ObjectTimeSeriesOperators.BinaryOperator;
import com.opengamma.timeseries.ObjectTimeSeriesOperators.UnaryOperator;
import com.opengamma.timeseries.fast.DateTimeNumericEncoding;
import com.opengamma.timeseries.fast.FastObjectTimeSeries;
import com.opengamma.timeseries.fast.integer.object.FastIntObjectTimeSeries;
import com.opengamma.timeseries.fast.integer.object.FastMutableIntObjectTimeSeries;
import com.opengamma.timeseries.fast.longint.object.FastLongObjectTimeSeries;
import com.opengamma.timeseries.fast.longint.object.FastMutableLongObjectTimeSeries;

/**
 * 
 * @param <DATE_TYPE> Type of the dates
 * @param <T> Type of the data
 */
public abstract class AbstractFastBackedObjectTimeSeries<DATE_TYPE, T>
    implements ObjectTimeSeries<DATE_TYPE, T>, FastBackedObjectTimeSeries<DATE_TYPE, T> {

  /** Serialization version. */
  private static final long serialVersionUID = -5914140323400583200L;

  public abstract DateTimeConverter<DATE_TYPE> getConverter();

  public abstract FastObjectTimeSeries<?, T> getFastSeries();
  
  public abstract FastBackedObjectTimeSeries<DATE_TYPE, T> operate(final FastObjectTimeSeries<?, T> other, final BinaryOperator<T> operator);
  
  public abstract FastBackedObjectTimeSeries<DATE_TYPE, T> operate(final FastBackedObjectTimeSeries<?, T> other, final BinaryOperator<T> operator);
  
  public abstract FastBackedObjectTimeSeries<DATE_TYPE, T> operate(final T other, final BinaryOperator<T> operator);
  
  public abstract FastBackedObjectTimeSeries<DATE_TYPE, T> operate(final UnaryOperator<T> operator);
  
  public abstract FastBackedObjectTimeSeries<DATE_TYPE, T> unionOperate(final FastObjectTimeSeries<?, T> other, final BinaryOperator<T> operator);
  
  public abstract FastBackedObjectTimeSeries<DATE_TYPE, T> unionOperate(final FastBackedObjectTimeSeries<?, T> other, final BinaryOperator<T> operator);

  @SuppressWarnings("unchecked")
  public FastBackedObjectTimeSeries<DATE_TYPE, T> intersectionFirstValue(ObjectTimeSeries<?, T> other) {
    if (other instanceof FastBackedObjectTimeSeries<?, ?>) {
      return operate((FastBackedObjectTimeSeries<?, T>) other, ObjectTimeSeriesOperators.<T>firstOperator());
    } else if (other instanceof FastIntObjectTimeSeries<?>) {
      return operate((FastIntObjectTimeSeries<T>) other, ObjectTimeSeriesOperators.<T>firstOperator());
    } else { // if (other instanceof FastLongObjectTimeSeries) {
      return operate((FastLongObjectTimeSeries<T>) other, ObjectTimeSeriesOperators.<T>firstOperator());
    }
  }
  public FastBackedObjectTimeSeries<DATE_TYPE, T> intersectionFirstValue(FastBackedObjectTimeSeries<?, T> other) {
    return operate(other, ObjectTimeSeriesOperators.<T>firstOperator());
  }
  public FastBackedObjectTimeSeries<DATE_TYPE, T> intersectionFirstValue(FastIntObjectTimeSeries<T> other) {
    return operate(other, ObjectTimeSeriesOperators.<T>firstOperator());
  }
  public FastBackedObjectTimeSeries<DATE_TYPE, T> intersectionFirstValue(FastLongObjectTimeSeries<T> other) {
    return operate(other, ObjectTimeSeriesOperators.<T>firstOperator());
  }
  @SuppressWarnings("unchecked")
  public FastBackedObjectTimeSeries<DATE_TYPE, T> intersectionSecondValue(ObjectTimeSeries<?, T> other) {
    if (other instanceof FastBackedObjectTimeSeries<?, ?>) {
      return operate((FastBackedObjectTimeSeries<?, T>) other, ObjectTimeSeriesOperators.<T>secondOperator());
    } else if (other instanceof FastIntObjectTimeSeries<?>) {
      return operate((FastIntObjectTimeSeries<T>) other, ObjectTimeSeriesOperators.<T>secondOperator());
    } else { // if (other instanceof FastLongObjectTimeSeries) {
      return operate((FastLongObjectTimeSeries<T>) other, ObjectTimeSeriesOperators.<T>secondOperator());
    }
  }
  public FastBackedObjectTimeSeries<DATE_TYPE, T> intersectionSecondValue(FastBackedObjectTimeSeries<?, T> other) {
    return operate(other, ObjectTimeSeriesOperators.<T>secondOperator());
  }
  public FastBackedObjectTimeSeries<DATE_TYPE, T> intersectionSecondValue(FastIntObjectTimeSeries<T> other) {
    return operate(other, ObjectTimeSeriesOperators.<T>secondOperator());
  }
  public FastBackedObjectTimeSeries<DATE_TYPE, T> intersectionSecondValue(FastLongObjectTimeSeries<T> other) {
    return operate(other, ObjectTimeSeriesOperators.<T>secondOperator());
  }

  @SuppressWarnings("unchecked")
  public FastBackedObjectTimeSeries<DATE_TYPE, T> lag(final int days) {
    DATE_TYPE[] times = timesArray();
    T[] values = valuesArray();
    if (days == 0) {
      return (FastBackedObjectTimeSeries<DATE_TYPE, T>) newInstance(times, values);
    } else if (days < 0) {
      DATE_TYPE[] resultTimes = (DATE_TYPE[]) new Object[times.length + days]; // remember days is -ve
      System.arraycopy(times, 0, resultTimes, 0, times.length + days);
      T[] resultValues = (T[]) new Object[times.length + days];
      System.arraycopy(values, -days, resultValues, 0, times.length + days);
      return (FastBackedObjectTimeSeries<DATE_TYPE, T>) newInstance(times, values);
    } else { // if (days > 0) {
      DATE_TYPE[] resultTimes = (DATE_TYPE[]) new Object[times.length - days]; // remember days is +ve
      System.arraycopy(times, days, resultTimes, 0, times.length - days);
      T[] resultValues = (T[]) new Object[times.length - days];
      System.arraycopy(values, 0, resultValues, 0, times.length - days);
      return (FastBackedObjectTimeSeries<DATE_TYPE, T>) newInstance(times, values);
    }
  }
  
  public ZoneId getTimeZone310() {
    return getConverter().getTimeZone310();
  }
  
  public TimeZone getTimeZone() {
    return getConverter().getTimeZone();
  }
  
  public FastIntObjectTimeSeries<T> toFastIntDaysOTS() {
    return getFastSeries().toFastIntObjectTimeSeries(DateTimeNumericEncoding.DATE_EPOCH_DAYS);
  }
  
  public FastMutableIntObjectTimeSeries<T> toFastMutableIntDaysOTS() {
    return getFastSeries().toFastMutableIntObjectTimeSeries(DateTimeNumericEncoding.DATE_EPOCH_DAYS);
  }
  
  public FastLongObjectTimeSeries<T> toFastLongMillisOTS() {
    return getFastSeries().toFastLongObjectTimeSeries(DateTimeNumericEncoding.TIME_EPOCH_MILLIS);
  }
  
  public FastMutableLongObjectTimeSeries<T> toFastMutableLongMillisOTS() {
    return getFastSeries().toFastMutableLongObjectTimeSeries(DateTimeNumericEncoding.TIME_EPOCH_MILLIS);
  }
  
//  @Override
//  public LocalDateObjectTimeSeries<T> toLocalDateObjectTimeSeries() {
//    return new ArrayLocalDateObjectTimeSeries(getTimeZone310(), toFastIntDaysOTS());
//  }
//
//  @Override
//  public LocalDateObjectTimeSeries<T> toLocalDateObjectTimeSeries(ZoneId timeZone) {
//    return new ArrayLocalDateObjectTimeSeries(timeZone, toFastIntDaysOTS());
//  }    
//  
//  @Override
//  public MutableLocalDateObjectTimeSeries<T> toMutableLocalDateObjectTimeSeries() {
//    return new ListLocalDateObjectTimeSeries(getTimeZone310(), toFastMutableIntDaysOTS());
//  }
//
//  @Override
//  public MutableLocalDateObjectTimeSeries<T> toMutableLocalDateObjectTimeSeries(ZoneId timeZone) {
//    return new ListLocalDateObjectTimeSeries(timeZone, toFastMutableIntDaysOTS());
//  }
//  
//  @Override
//  public DateObjectTimeSeries<T> toDateObjectTimeSeries() {
//    return new ArrayDateObjectTimeSeries(getTimeZone(), toFastIntDaysOTS());
//  }
//
//  @Override
//  public DateObjectTimeSeries<T> toDateObjectTimeSeries(TimeZone timeZone) {
//    return new ArrayDateObjectTimeSeries(timeZone, toFastIntDaysOTS());
//  }
//  
//  @Override
//  public MutableDateObjectTimeSeries<T> toMutableDateObjectTimeSeries() {
//    return new ListDateObjectTimeSeries(getTimeZone(), toFastMutableIntDaysOTS());
//  }
//
//  @Override
//  public MutableDateObjectTimeSeries<T> toMutableDateObjectTimeSeries(TimeZone timeZone) {
//    return new ListDateObjectTimeSeries(timeZone, toFastMutableIntDaysOTS());
//  }
//
//  @Override
//  public DateTimeObjectTimeSeries<T> toDateTimeObjectTimeSeries() {
//    return new ArrayDateTimeObjectTimeSeries(getTimeZone(), toFastLongMillisOTS());
//  }
//
//  @Override
//  public DateTimeObjectTimeSeries<T> toDateTimeObjectTimeSeries(TimeZone timeZone) {
//    return new ArrayDateTimeObjectTimeSeries(timeZone, toFastLongMillisOTS());
//  }
//
//  @Override
//  public MutableDateTimeObjectTimeSeries<T> toMutableDateTimeObjectTimeSeries() {
//    return new ListDateTimeObjectTimeSeries(getTimeZone(), toFastMutableLongMillisOTS());
//  }
//
//  @Override
//  public MutableDateTimeObjectTimeSeries<T> toMutableDateTimeObjectTimeSeries(
//      TimeZone timeZone) {
//    return new ListDateTimeObjectTimeSeries(timeZone, toFastMutableLongMillisOTS());
//  }
//
//  @Override
//  public SQLDateObjectTimeSeries<T> toSQLDateObjectTimeSeries() {
//    return new ArraySQLDateObjectTimeSeries(getTimeZone(), toFastIntDaysOTS());
//  }
//
//  @Override
//  public SQLDateObjectTimeSeries<T> toSQLDateObjectTimeSeries(TimeZone timeZone) {
//    return new ArraySQLDateObjectTimeSeries(timeZone, toFastIntDaysOTS());
//  }
//
//  @Override
//  public MutableSQLDateObjectTimeSeries<T> toMutableSQLDateObjectTimeSeries() {
//    return new ListSQLDateObjectTimeSeries(getTimeZone(), toFastMutableIntDaysOTS());
//  }
//
//  @Override
//  public MutableSQLDateObjectTimeSeries<T> toMutableSQLDateObjectTimeSeries(TimeZone timeZone) {
//    return new ListSQLDateObjectTimeSeries(timeZone, toFastMutableIntDaysOTS());
//  }
//
//  @Override
//  public ZonedDateTimeObjectTimeSeries<T> toZonedDateTimeObjectTimeSeries() {
//    return new ArrayZonedDateTimeObjectTimeSeries(getTimeZone310(), toFastLongMillisOTS());
//  }
//  
//  @Override
//  public ZonedDateTimeObjectTimeSeries<T> toZonedDateTimeObjectTimeSeries(ZoneId timeZone) {
//    return new ArrayZonedDateTimeObjectTimeSeries(timeZone, toFastLongMillisOTS());
//  }
//
//  @Override
//  public MutableZonedDateTimeObjectTimeSeries<T> toMutableZonedDateTimeObjectTimeSeries() {
//    return new ListZonedDateTimeObjectTimeSeries(getTimeZone310(), toFastMutableLongMillisOTS());
//  }
//  
//  @Override
//  public MutableZonedDateTimeObjectTimeSeries<T> toMutableZonedDateTimeObjectTimeSeries(ZoneId timeZone) {
//    return new ListZonedDateTimeObjectTimeSeries(timeZone, toFastMutableLongMillisOTS());
//  }
//  
//  @Override
//  public YearOffsetObjectTimeSeries<T> toYearOffsetObjectTimeSeries(ZonedDateTime zeroDate) {
//    return new ArrayYearOffsetObjectTimeSeries(zeroDate, toFastLongMillisOTS());
//  }
//
//  @Override
//  public YearOffsetObjectTimeSeries<T> toYearOffsetObjectTimeSeries(java.util.TimeZone timeZone, Date zeroDate) {
//    return new ArrayYearOffsetObjectTimeSeries(timeZone, zeroDate, toFastLongMillisOTS());
//  }
//
//  @Override
//  public MutableYearOffsetObjectTimeSeries<T> toMutableYearOffsetObjectTimeSeries(ZonedDateTime zeroDate) {
//    return new ListYearOffsetObjectTimeSeries(zeroDate, toFastMutableLongMillisOTS());
//  }
//  
//  @Override
//  public MutableYearOffsetObjectTimeSeries<T> toMutableYearOffsetObjectTimeSeries(java.util.TimeZone timeZone, Date zeroDate) {
//    return new ListYearOffsetObjectTimeSeries(timeZone, zeroDate, toFastMutableLongMillisOTS());
//  }
//  
  @Override
  public FastIntObjectTimeSeries<T> toFastIntObjectTimeSeries() {
    return getFastSeries().toFastIntObjectTimeSeries();
  }

  @Override
  public FastIntObjectTimeSeries<T> toFastIntObjectTimeSeries(
      DateTimeNumericEncoding encoding) {
    return getFastSeries().toFastIntObjectTimeSeries(encoding);
  }

  @Override
  public FastLongObjectTimeSeries<T> toFastLongObjectTimeSeries() {
    return getFastSeries().toFastLongObjectTimeSeries();
  }

  @Override
  public FastLongObjectTimeSeries<T> toFastLongObjectTimeSeries(
      DateTimeNumericEncoding encoding) {
    return getFastSeries().toFastLongObjectTimeSeries(encoding);
  }

  @Override
  public FastMutableIntObjectTimeSeries<T> toFastMutableIntObjectTimeSeries() {
    return getFastSeries().toFastMutableIntObjectTimeSeries();
  }

  @Override
  public FastMutableIntObjectTimeSeries<T> toFastMutableIntObjectTimeSeries(
      DateTimeNumericEncoding encoding) {
    return getFastSeries().toFastMutableIntObjectTimeSeries(encoding);
  }

  @Override
  public FastMutableLongObjectTimeSeries<T> toFastMutableLongObjectTimeSeries() {
    return getFastSeries().toFastMutableLongObjectTimeSeries();
  }

  @Override
  public FastMutableLongObjectTimeSeries<T> toFastMutableLongObjectTimeSeries(
      DateTimeNumericEncoding encoding) {
    return getFastSeries().toFastMutableLongObjectTimeSeries(encoding);
  }
}
