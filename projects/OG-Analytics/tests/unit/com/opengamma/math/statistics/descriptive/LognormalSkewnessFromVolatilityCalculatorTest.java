/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.math.statistics.descriptive;

import static org.testng.AssertJUnit.assertEquals;

import org.testng.annotations.Test;

import com.opengamma.math.function.Function2D;

/**
 * 
 */
public class LognormalSkewnessFromVolatilityCalculatorTest {
  private static final Function2D<Double, Double> F = new LognormalSkewnessFromVolatilityCalculator();
  private static final double SIGMA = 0.3;
  private static final double T = 0.25;

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNullSigma() {
    F.evaluate(null, T);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNullT() {
    F.evaluate(SIGMA, null);
  }

  @Test
  public void test() {
    assertEquals(F.evaluate(SIGMA, T), 0.4560, 1e-4);
  }
}
