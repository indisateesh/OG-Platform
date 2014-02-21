/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.sesame.irs;

import static com.opengamma.util.result.ResultGenerator.map;
import static com.opengamma.util.result.ResultGenerator.success;

import com.opengamma.analytics.financial.forex.method.FXMatrix;
import com.opengamma.analytics.financial.provider.curve.CurveBuildingBlockBundle;
import com.opengamma.analytics.financial.provider.description.interestrate.MulticurveProviderDiscount;
import com.opengamma.financial.security.irs.InterestRateSwapSecurity;
import com.opengamma.sesame.DiscountingMulticurveCombinerFn;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.result.Result;
import com.opengamma.util.result.ResultGenerator;
import com.opengamma.util.tuple.Pair;


public class InterestRateSwapDiscountingCalculatorFn implements InterestRateSwapCalculatorFn {

  /**
   * Factory for creating a calculator for Swap securities.
   */
  private final InterestRateSwapCalculatorFactory _factory;

  /**
   * Generates a combined multicurve bundle suitable for use with a particular security.
   */
  private final DiscountingMulticurveCombinerFn _discountingMulticurveCombinerFn;

  public InterestRateSwapDiscountingCalculatorFn(InterestRateSwapCalculatorFactory factory,
                                                 DiscountingMulticurveCombinerFn discountingMulticurveCombinerFn) {
    _factory = ArgumentChecker.notNull(factory, "factory");
    _discountingMulticurveCombinerFn =
        ArgumentChecker.notNull(discountingMulticurveCombinerFn, "discountingMulticurveCombinerFn");
  }

  @Override
  public Result<InterestRateSwapCalculator> generateCalculator(final InterestRateSwapSecurity security) {

    return map(createBundle(security),
               new ResultGenerator.ResultMapper<Pair<MulticurveProviderDiscount, CurveBuildingBlockBundle>, InterestRateSwapCalculator>() {
        @Override
        public Result<InterestRateSwapCalculator> map(Pair<MulticurveProviderDiscount, CurveBuildingBlockBundle> result) {
          return success(_factory.createCalculator(security, result.getFirst()));
        }
      });
  }

  private Result<Pair<MulticurveProviderDiscount, CurveBuildingBlockBundle>> createBundle(InterestRateSwapSecurity security) {
    return _discountingMulticurveCombinerFn.createMergedMulticurveBundle(security, success(new FXMatrix()));
  }
}
