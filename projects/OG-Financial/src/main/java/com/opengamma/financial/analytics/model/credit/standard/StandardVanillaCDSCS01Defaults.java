/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.credit.standard;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueRequirementNames;
import com.opengamma.financial.analytics.model.credit.CreditInstrumentPropertyNamesAndValues;
import com.opengamma.financial.property.DefaultPropertyFunction;
import com.opengamma.financial.security.FinancialSecurityTypes;
import com.opengamma.financial.security.FinancialSecurityUtils;
import com.opengamma.util.ArgumentChecker;

/**
 * 
 */
public class StandardVanillaCDSCS01Defaults extends DefaultPropertyFunction {
  private static final String[] VALUE_REQUIREMENT = new String[] {
    ValueRequirementNames.CS01,
    ValueRequirementNames.GAMMA_CS01,
  };
  private final PriorityClass _priority;
  private final Map<String, String> _currencyToSpreadCurveBump;
  private final Map<String, String> _currencyToSpreadBumpType;
  private final Map<String, String> _currencyToPriceType;

  public StandardVanillaCDSCS01Defaults(final String priority, final String... perCurrencyDefaults) {
    super(FinancialSecurityTypes.STANDARD_VANILLA_CDS_SECURITY.or(FinancialSecurityTypes.LEGACY_VANILLA_CDS_SECURITY), true);
    ArgumentChecker.notNull(priority, "priority");
    ArgumentChecker.notNull(perCurrencyDefaults, "per currency defaults");
    ArgumentChecker.isTrue(perCurrencyDefaults.length % 4 == 0, "Must have one spread curve bump, spread bump type and price type per currency");
    _priority = PriorityClass.valueOf(priority);
    _currencyToSpreadCurveBump = new HashMap<>();
    _currencyToSpreadBumpType = new HashMap<>();
    _currencyToPriceType = new HashMap<>();
    for (int i = 0; i < perCurrencyDefaults.length; i += 4) {
      final String currency = perCurrencyDefaults[i];
      _currencyToSpreadCurveBump.put(currency, perCurrencyDefaults[i + 1]);
      _currencyToSpreadBumpType.put(currency, perCurrencyDefaults[i + 2]);
      _currencyToPriceType.put(currency, perCurrencyDefaults[i + 3]);
    }
  }

  @Override
  public boolean canApplyTo(final FunctionCompilationContext context, final ComputationTarget target) {
    return _currencyToSpreadCurveBump.containsKey(FinancialSecurityUtils.getCurrency(target.getSecurity()).getCode());
  }

  @Override
  protected void getDefaults(final PropertyDefaults defaults) {
    for (final String valueRequirement : VALUE_REQUIREMENT) {
      defaults.addValuePropertyName(valueRequirement, CreditInstrumentPropertyNamesAndValues.PROPERTY_SPREAD_CURVE_BUMP);
      defaults.addValuePropertyName(valueRequirement, CreditInstrumentPropertyNamesAndValues.PROPERTY_SPREAD_BUMP_TYPE);
      defaults.addValuePropertyName(valueRequirement, CreditInstrumentPropertyNamesAndValues.PROPERTY_CDS_PRICE_TYPE);
    }
  }

  @Override
  protected Set<String> getDefaultValue(final FunctionCompilationContext context, final ComputationTarget target, final ValueRequirement desiredValue, final String propertyName) {
    final String currency = FinancialSecurityUtils.getCurrency(target.getSecurity()).getCode();
    if (CreditInstrumentPropertyNamesAndValues.PROPERTY_SPREAD_CURVE_BUMP.equals(propertyName)) {
      return Collections.singleton(_currencyToSpreadCurveBump.get(currency));
    }
    if (CreditInstrumentPropertyNamesAndValues.PROPERTY_SPREAD_BUMP_TYPE.equals(propertyName)) {
      return Collections.singleton(_currencyToSpreadBumpType.get(currency));
    }
    if (CreditInstrumentPropertyNamesAndValues.PROPERTY_CDS_PRICE_TYPE.equals(propertyName)) {
      return Collections.singleton(_currencyToPriceType.get(currency));
    }
    return null;
  }

  @Override
  public PriorityClass getPriority() {
    return _priority;
  }
}
