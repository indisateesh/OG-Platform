/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.math.rootfinding;


/**
 * 
 */
public class BisectionSingleRootFinderTest extends RealSingleRootFinderTestCase {
  private static final RealSingleRootFinder FINDER = new BisectionSingleRootFinder();

  @Override
  protected RealSingleRootFinder getRootFinder() {
    return FINDER;
  }

}
