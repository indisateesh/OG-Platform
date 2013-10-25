/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.sesame.example;

import com.opengamma.financial.security.equity.EquitySecurity;
import com.opengamma.sesame.config.DefaultImplementation;
import com.opengamma.sesame.config.EngineFunction;
import com.opengamma.sesame.config.Target;

/**
 * Trivial example function that returns the description of an equity security.
 */
@DefaultImplementation(EquityDescription.class)
public interface EquityDescriptionFunction {

  public static final String VALUE_NAME = "EquityDescription";

  /**
   * Returns a description of the security
   * @param security A security
   * @return A description of the security
   */
  @EngineFunction(EquityDescriptionFunction.VALUE_NAME)
  String getDescription(@Target EquitySecurity security);
}
