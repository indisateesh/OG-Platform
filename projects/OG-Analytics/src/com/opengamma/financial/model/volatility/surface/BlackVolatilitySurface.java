/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.model.volatility.surface;

import com.opengamma.math.surface.Surface;
import com.opengamma.util.tuple.DoublesPair;

/**
 * 
 */
public class BlackVolatilitySurface extends VolatilitySurface {

  /**
   * @param surface
   */
  public BlackVolatilitySurface(Surface<Double, Double, Double> surface) {
    super(surface);
  }

  public double getVolatility(final double t, final double k) {
    DoublesPair temp = new DoublesPair(t, k);
    return getVolatility(temp);
  }

}
