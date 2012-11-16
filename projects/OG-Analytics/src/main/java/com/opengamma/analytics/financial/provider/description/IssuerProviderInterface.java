/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.provider.description;

import java.util.List;
import java.util.Set;

import com.opengamma.analytics.financial.provider.sensitivity.multicurve.ForwardSensitivity;
import com.opengamma.util.money.Currency;
import com.opengamma.util.tuple.DoublesPair;
import com.opengamma.util.tuple.Pair;

/**
 * Interface for issuer specific 
 */
public interface IssuerProviderInterface {
  // TODO: Can probably be merged with MulticurveProvider if the Currency is replaced by a UniqueIdentifiable.

  /**
   * Gets the discount factor for one issuer in one currency.
   * @param issuerCcy The issuer name/currency pair.
   * @param time The time.
   * @return The discount factor.
   */
  double getDiscountFactor(Pair<String, Currency> issuerCcy, Double time);

  /**
   * Return the name associated to the discounting for a issuer/currency.
   * @param issuerCcy The issuer/currency.
   * @return The name.
   */
  String getName(Pair<String, Currency> issuerCcy);

  /**
   * Gets the names of all curves (discounting, forward, and issuers).
   * @return The names.
   */
  Set<String> getAllNames();

  /**
   * Returns the MulticurveProvider from which the IssuerProvider is composed.
   * @return The multi-curves provider.
   */
  MulticurveProviderInterface getMulticurveProvider();

  double[] parameterSensitivity(String name, List<DoublesPair> pointSensitivity);

  double[] parameterForwardSensitivity(String name, List<ForwardSensitivity> pointSensitivity);

  Set<Pair<String, Currency>> getIssuersCurrencies();

}
