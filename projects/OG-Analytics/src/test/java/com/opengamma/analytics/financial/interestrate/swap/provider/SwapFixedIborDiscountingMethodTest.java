/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.interestrate.swap.provider;

import static org.testng.AssertJUnit.assertEquals;

import javax.time.calendar.Period;
import javax.time.calendar.ZonedDateTime;

import org.testng.annotations.Test;

import com.opengamma.analytics.financial.instrument.annuity.AnnuityCouponFixedDefinition;
import com.opengamma.analytics.financial.instrument.annuity.AnnuityDefinition;
import com.opengamma.analytics.financial.instrument.index.GeneratorSwapFixedIbor;
import com.opengamma.analytics.financial.instrument.index.GeneratorSwapFixedIborMaster;
import com.opengamma.analytics.financial.instrument.index.IborIndex;
import com.opengamma.analytics.financial.instrument.payment.CouponDefinition;
import com.opengamma.analytics.financial.instrument.payment.CouponIborDefinition;
import com.opengamma.analytics.financial.instrument.swap.SwapDefinition;
import com.opengamma.analytics.financial.interestrate.annuity.derivative.Annuity;
import com.opengamma.analytics.financial.interestrate.payments.derivative.CouponIbor;
import com.opengamma.analytics.financial.interestrate.payments.derivative.Payment;
import com.opengamma.analytics.financial.interestrate.payments.provider.CouponIborDiscountingProviderMethod;
import com.opengamma.analytics.financial.interestrate.swap.derivative.Swap;
import com.opengamma.analytics.financial.provider.calculator.discounting.PresentValueDiscountingCalculator;
import com.opengamma.analytics.financial.provider.description.MulticurveProviderDiscount;
import com.opengamma.analytics.financial.provider.description.MulticurveProviderDiscountDataSets;
import com.opengamma.analytics.financial.schedule.ScheduleCalculator;
import com.opengamma.financial.convention.calendar.Calendar;
import com.opengamma.util.money.Currency;
import com.opengamma.util.money.MultipleCurrencyAmount;
import com.opengamma.util.time.DateUtils;

/**
 * Tests discounting methods/calculator for swap with non-standard features.
 */
public class SwapFixedIborDiscountingMethodTest {

  private static final ZonedDateTime REFERENCE_DATE = DateUtils.getUTCDate(2012, 11, 5);
  private static final MulticurveProviderDiscount MULTICURVES = MulticurveProviderDiscountDataSets.createProvider3();
  private static final IborIndex[] INDEX_LIST = MulticurveProviderDiscountDataSets.getIndexesIbor();
  private static final IborIndex EURIBOR3M = INDEX_LIST[0];
  private static final IborIndex EURIBOR6M = INDEX_LIST[1];
  private static final Currency EUR = EURIBOR3M.getCurrency();
  private static final Calendar CALENDAR = EURIBOR3M.getCalendar();

  private static final ZonedDateTime START_DATE = DateUtils.getUTCDate(2013, 9, 9);
  private static final ZonedDateTime END_DATE_3 = ScheduleCalculator.getAdjustedDate(START_DATE, EURIBOR3M.getTenor(), EURIBOR3M);
  private static final Period TOTAL_TENOR = EURIBOR3M.getTenor().plus(EURIBOR6M.getTenor());
  private static final ZonedDateTime END_DATE_6 = ScheduleCalculator.getAdjustedDate(START_DATE, TOTAL_TENOR, EURIBOR3M);
  // Definitions
  private static final double NOTIONAL = 100000000.0; // 100m
  private static final double RATE = 0.0250; // 2.5%
  private static final CouponIborDefinition CPN_IBOR_3_DEFINITION = CouponIborDefinition.from(START_DATE, END_DATE_3, NOTIONAL, EURIBOR3M);
  private static final CouponIborDefinition CPN_IBOR_6_DEFINITION = CouponIborDefinition.from(END_DATE_3, END_DATE_6, NOTIONAL, EURIBOR6M);
  private static final AnnuityDefinition<CouponDefinition> ANNUITY_IBOR_DEFINITION = new AnnuityDefinition<CouponDefinition>(new CouponIborDefinition[] {CPN_IBOR_3_DEFINITION, CPN_IBOR_6_DEFINITION});
  private static final GeneratorSwapFixedIbor EUR1YEURIBOR6M = GeneratorSwapFixedIborMaster.getInstance().getGenerator("EUR1YEURIBOR6M", CALENDAR);
  private static final AnnuityCouponFixedDefinition ANNUITY_FIXED_DEFINITION = AnnuityCouponFixedDefinition.from(START_DATE, TOTAL_TENOR, EUR1YEURIBOR6M, NOTIONAL, RATE, true);
  private static final SwapDefinition SWAP_DEFINITION = new SwapDefinition(ANNUITY_FIXED_DEFINITION, ANNUITY_IBOR_DEFINITION);
  // Derivatives
  public static final String NOT_USED = "Not used";
  public static final String[] NOT_USED_A = {NOT_USED, NOT_USED, NOT_USED};
  private static final CouponIbor CPN_IBOR_3 = (CouponIbor) CPN_IBOR_3_DEFINITION.toDerivative(REFERENCE_DATE, NOT_USED_A);
  private static final CouponIbor CPN_IBOR_6 = (CouponIbor) CPN_IBOR_6_DEFINITION.toDerivative(REFERENCE_DATE, NOT_USED_A);
  private static final Annuity<? extends Payment> ANNUITY_IBOR = ANNUITY_IBOR_DEFINITION.toDerivative(REFERENCE_DATE, NOT_USED_A);
  private static final Annuity<? extends Payment> ANNUITY_FIXED = ANNUITY_FIXED_DEFINITION.toDerivative(REFERENCE_DATE, NOT_USED_A);
  private static final Swap<? extends Payment, ? extends Payment> SWAP = SWAP_DEFINITION.toDerivative(REFERENCE_DATE, NOT_USED_A);

  private static final CouponIborDiscountingProviderMethod METHOD_CPN_IBOR = CouponIborDiscountingProviderMethod.getInstance();

  private static final PresentValueDiscountingCalculator PVDC = PresentValueDiscountingCalculator.getInstance();

  private static final double TOLERANCE_PV = 1.0E-2;

  @Test
  /**
   * Tests the present value for a swap with the Ibor leg having different Ibor indexes (EURIBOR3M and EURIBOR6M).
   */
  public void presentValue2Iborindex() {
    MultipleCurrencyAmount pvCalcCpn3 = PVDC.visit(CPN_IBOR_3, MULTICURVES);
    MultipleCurrencyAmount pvMethCpn3 = METHOD_CPN_IBOR.presentValue(CPN_IBOR_3, MULTICURVES);
    MultipleCurrencyAmount pvCalcCpn6 = PVDC.visit(CPN_IBOR_6, MULTICURVES);
    MultipleCurrencyAmount pvMethCpn6 = METHOD_CPN_IBOR.presentValue(CPN_IBOR_6, MULTICURVES);
    MultipleCurrencyAmount pvCalcAnnIbor = PVDC.visit(ANNUITY_IBOR, MULTICURVES);
    MultipleCurrencyAmount pvCalcAnnFixed = PVDC.visit(ANNUITY_FIXED, MULTICURVES);
    MultipleCurrencyAmount pvCalcSwap = PVDC.visit(SWAP, MULTICURVES);
    assertEquals("PresentValueDiscountingProviderCalculator: multiple Ibor index", pvMethCpn3.getAmount(EUR), pvCalcCpn3.getAmount(EUR), TOLERANCE_PV);
    assertEquals("PresentValueDiscountingProviderCalculator: multiple Ibor index", pvMethCpn6.getAmount(EUR), pvCalcCpn6.getAmount(EUR), TOLERANCE_PV);
    assertEquals("PresentValueDiscountingProviderCalculator: multiple Ibor index", pvCalcCpn3.plus(pvCalcCpn6).getAmount(EUR), pvCalcAnnIbor.getAmount(EUR), TOLERANCE_PV);
    assertEquals("PresentValueDiscountingProviderCalculator: multiple Ibor index", pvCalcCpn3.plus(pvCalcCpn6).getAmount(EUR), pvCalcAnnIbor.getAmount(EUR), TOLERANCE_PV);
    assertEquals("PresentValueDiscountingProviderCalculator: multiple Ibor index", pvCalcAnnFixed.plus(pvCalcAnnIbor).getAmount(EUR), pvCalcSwap.getAmount(EUR), TOLERANCE_PV);
  }

}
