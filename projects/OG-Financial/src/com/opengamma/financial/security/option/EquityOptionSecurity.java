/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.option;

import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.financial.security.FinancialSecurity;
import com.opengamma.financial.security.FinancialSecurityVisitor;
import com.opengamma.id.ExternalId;
import com.opengamma.util.money.Currency;
import com.opengamma.util.time.Expiry;

/**
 * A security for equity options.
 */
@BeanDefinition
public class EquityOptionSecurity extends FinancialSecurity {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The security type.
   */
  public static final String SECURITY_TYPE = "EQUITY_OPTION";

  /**
   * The option type.
   */
  @PropertyDefinition(validate = "notNull")
  private OptionType _optionType;
  /**
   * The strike.
   */
  @PropertyDefinition
  private double _strike;
  /**
   * The currency.
   */
  @PropertyDefinition(validate = "notNull")
  private Currency _currency;
  /**
   * The underlying identifier.
   */
  @PropertyDefinition(validate = "notNull")
  private ExternalId _underlyingIdentifier;
  /**
   * The exercise type.
   */
  @PropertyDefinition(validate = "notNull")
  private ExerciseType _exerciseType;
  /**
   * The expiry.
   */
  @PropertyDefinition(validate = "notNull")
  private Expiry _expiry;
  /**
   * The point value.
   */
  @PropertyDefinition
  private double _pointValue;
  /**
   * The exchange.
   */
  @PropertyDefinition(validate = "notNull")
  private String _exchange;

  /**
   * Creates an empty instance.
   * <p>
   * The security details should be set before use.
   */
  public EquityOptionSecurity() {
  }

  public EquityOptionSecurity(OptionType optionType, double strike, Currency currency, ExternalId underlyingIdentifier,
      ExerciseType exerciseType, Expiry expiry, double pointValue, String exchange) {
    super(SECURITY_TYPE);
    setOptionType(optionType);
    setStrike(strike);
    setCurrency(currency);
    setUnderlyingIdentifier(underlyingIdentifier);
    setExerciseType(exerciseType);
    setExpiry(expiry);
    setPointValue(pointValue);
    setExchange(exchange);
  }

  //-------------------------------------------------------------------------
  @Override
  public final <T> T accept(FinancialSecurityVisitor<T> visitor) {
    return visitor.visitEquityOptionSecurity(this);
  }

  /**
   * Accepts a visitor to manage traversal of the hierarchy.
   * 
   * @param <T> the result type of the visitor
   * @param visitor  the visitor, not null
   * @return the result
   */
  public <T> T accept(EquityOptionSecurityVisitor<T> visitor) {
    return visitor.visitEquityOptionSecurity(this);
  };

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code EquityOptionSecurity}.
   * @return the meta-bean, not null
   */
  public static EquityOptionSecurity.Meta meta() {
    return EquityOptionSecurity.Meta.INSTANCE;
  }
  static {
    JodaBeanUtils.registerMetaBean(EquityOptionSecurity.Meta.INSTANCE);
  }

  @Override
  public EquityOptionSecurity.Meta metaBean() {
    return EquityOptionSecurity.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    switch (propertyName.hashCode()) {
      case 1373587791:  // optionType
        return getOptionType();
      case -891985998:  // strike
        return getStrike();
      case 575402001:  // currency
        return getCurrency();
      case 368639974:  // underlyingIdentifier
        return getUnderlyingIdentifier();
      case -466331342:  // exerciseType
        return getExerciseType();
      case -1289159373:  // expiry
        return getExpiry();
      case 1257391553:  // pointValue
        return getPointValue();
      case 1989774883:  // exchange
        return getExchange();
    }
    return super.propertyGet(propertyName, quiet);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    switch (propertyName.hashCode()) {
      case 1373587791:  // optionType
        setOptionType((OptionType) newValue);
        return;
      case -891985998:  // strike
        setStrike((Double) newValue);
        return;
      case 575402001:  // currency
        setCurrency((Currency) newValue);
        return;
      case 368639974:  // underlyingIdentifier
        setUnderlyingIdentifier((ExternalId) newValue);
        return;
      case -466331342:  // exerciseType
        setExerciseType((ExerciseType) newValue);
        return;
      case -1289159373:  // expiry
        setExpiry((Expiry) newValue);
        return;
      case 1257391553:  // pointValue
        setPointValue((Double) newValue);
        return;
      case 1989774883:  // exchange
        setExchange((String) newValue);
        return;
    }
    super.propertySet(propertyName, newValue, quiet);
  }

  @Override
  protected void validate() {
    JodaBeanUtils.notNull(_optionType, "optionType");
    JodaBeanUtils.notNull(_currency, "currency");
    JodaBeanUtils.notNull(_underlyingIdentifier, "underlyingIdentifier");
    JodaBeanUtils.notNull(_exerciseType, "exerciseType");
    JodaBeanUtils.notNull(_expiry, "expiry");
    JodaBeanUtils.notNull(_exchange, "exchange");
    super.validate();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      EquityOptionSecurity other = (EquityOptionSecurity) obj;
      return JodaBeanUtils.equal(getOptionType(), other.getOptionType()) &&
          JodaBeanUtils.equal(getStrike(), other.getStrike()) &&
          JodaBeanUtils.equal(getCurrency(), other.getCurrency()) &&
          JodaBeanUtils.equal(getUnderlyingIdentifier(), other.getUnderlyingIdentifier()) &&
          JodaBeanUtils.equal(getExerciseType(), other.getExerciseType()) &&
          JodaBeanUtils.equal(getExpiry(), other.getExpiry()) &&
          JodaBeanUtils.equal(getPointValue(), other.getPointValue()) &&
          JodaBeanUtils.equal(getExchange(), other.getExchange()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += hash * 31 + JodaBeanUtils.hashCode(getOptionType());
    hash += hash * 31 + JodaBeanUtils.hashCode(getStrike());
    hash += hash * 31 + JodaBeanUtils.hashCode(getCurrency());
    hash += hash * 31 + JodaBeanUtils.hashCode(getUnderlyingIdentifier());
    hash += hash * 31 + JodaBeanUtils.hashCode(getExerciseType());
    hash += hash * 31 + JodaBeanUtils.hashCode(getExpiry());
    hash += hash * 31 + JodaBeanUtils.hashCode(getPointValue());
    hash += hash * 31 + JodaBeanUtils.hashCode(getExchange());
    return hash ^ super.hashCode();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the option type.
   * @return the value of the property, not null
   */
  public OptionType getOptionType() {
    return _optionType;
  }

  /**
   * Sets the option type.
   * @param optionType  the new value of the property, not null
   */
  public void setOptionType(OptionType optionType) {
    JodaBeanUtils.notNull(optionType, "optionType");
    this._optionType = optionType;
  }

  /**
   * Gets the the {@code optionType} property.
   * @return the property, not null
   */
  public final Property<OptionType> optionType() {
    return metaBean().optionType().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the strike.
   * @return the value of the property
   */
  public double getStrike() {
    return _strike;
  }

  /**
   * Sets the strike.
   * @param strike  the new value of the property
   */
  public void setStrike(double strike) {
    this._strike = strike;
  }

  /**
   * Gets the the {@code strike} property.
   * @return the property, not null
   */
  public final Property<Double> strike() {
    return metaBean().strike().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the currency.
   * @return the value of the property, not null
   */
  public Currency getCurrency() {
    return _currency;
  }

  /**
   * Sets the currency.
   * @param currency  the new value of the property, not null
   */
  public void setCurrency(Currency currency) {
    JodaBeanUtils.notNull(currency, "currency");
    this._currency = currency;
  }

  /**
   * Gets the the {@code currency} property.
   * @return the property, not null
   */
  public final Property<Currency> currency() {
    return metaBean().currency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the underlying identifier.
   * @return the value of the property, not null
   */
  public ExternalId getUnderlyingIdentifier() {
    return _underlyingIdentifier;
  }

  /**
   * Sets the underlying identifier.
   * @param underlyingIdentifier  the new value of the property, not null
   */
  public void setUnderlyingIdentifier(ExternalId underlyingIdentifier) {
    JodaBeanUtils.notNull(underlyingIdentifier, "underlyingIdentifier");
    this._underlyingIdentifier = underlyingIdentifier;
  }

  /**
   * Gets the the {@code underlyingIdentifier} property.
   * @return the property, not null
   */
  public final Property<ExternalId> underlyingIdentifier() {
    return metaBean().underlyingIdentifier().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the exercise type.
   * @return the value of the property, not null
   */
  public ExerciseType getExerciseType() {
    return _exerciseType;
  }

  /**
   * Sets the exercise type.
   * @param exerciseType  the new value of the property, not null
   */
  public void setExerciseType(ExerciseType exerciseType) {
    JodaBeanUtils.notNull(exerciseType, "exerciseType");
    this._exerciseType = exerciseType;
  }

  /**
   * Gets the the {@code exerciseType} property.
   * @return the property, not null
   */
  public final Property<ExerciseType> exerciseType() {
    return metaBean().exerciseType().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the expiry.
   * @return the value of the property, not null
   */
  public Expiry getExpiry() {
    return _expiry;
  }

  /**
   * Sets the expiry.
   * @param expiry  the new value of the property, not null
   */
  public void setExpiry(Expiry expiry) {
    JodaBeanUtils.notNull(expiry, "expiry");
    this._expiry = expiry;
  }

  /**
   * Gets the the {@code expiry} property.
   * @return the property, not null
   */
  public final Property<Expiry> expiry() {
    return metaBean().expiry().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the point value.
   * @return the value of the property
   */
  public double getPointValue() {
    return _pointValue;
  }

  /**
   * Sets the point value.
   * @param pointValue  the new value of the property
   */
  public void setPointValue(double pointValue) {
    this._pointValue = pointValue;
  }

  /**
   * Gets the the {@code pointValue} property.
   * @return the property, not null
   */
  public final Property<Double> pointValue() {
    return metaBean().pointValue().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the exchange.
   * @return the value of the property, not null
   */
  public String getExchange() {
    return _exchange;
  }

  /**
   * Sets the exchange.
   * @param exchange  the new value of the property, not null
   */
  public void setExchange(String exchange) {
    JodaBeanUtils.notNull(exchange, "exchange");
    this._exchange = exchange;
  }

  /**
   * Gets the the {@code exchange} property.
   * @return the property, not null
   */
  public final Property<String> exchange() {
    return metaBean().exchange().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code EquityOptionSecurity}.
   */
  public static class Meta extends FinancialSecurity.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code optionType} property.
     */
    private final MetaProperty<OptionType> _optionType = DirectMetaProperty.ofReadWrite(
        this, "optionType", EquityOptionSecurity.class, OptionType.class);
    /**
     * The meta-property for the {@code strike} property.
     */
    private final MetaProperty<Double> _strike = DirectMetaProperty.ofReadWrite(
        this, "strike", EquityOptionSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code currency} property.
     */
    private final MetaProperty<Currency> _currency = DirectMetaProperty.ofReadWrite(
        this, "currency", EquityOptionSecurity.class, Currency.class);
    /**
     * The meta-property for the {@code underlyingIdentifier} property.
     */
    private final MetaProperty<ExternalId> _underlyingIdentifier = DirectMetaProperty.ofReadWrite(
        this, "underlyingIdentifier", EquityOptionSecurity.class, ExternalId.class);
    /**
     * The meta-property for the {@code exerciseType} property.
     */
    private final MetaProperty<ExerciseType> _exerciseType = DirectMetaProperty.ofReadWrite(
        this, "exerciseType", EquityOptionSecurity.class, ExerciseType.class);
    /**
     * The meta-property for the {@code expiry} property.
     */
    private final MetaProperty<Expiry> _expiry = DirectMetaProperty.ofReadWrite(
        this, "expiry", EquityOptionSecurity.class, Expiry.class);
    /**
     * The meta-property for the {@code pointValue} property.
     */
    private final MetaProperty<Double> _pointValue = DirectMetaProperty.ofReadWrite(
        this, "pointValue", EquityOptionSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code exchange} property.
     */
    private final MetaProperty<String> _exchange = DirectMetaProperty.ofReadWrite(
        this, "exchange", EquityOptionSecurity.class, String.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<Object>> _map = new DirectMetaPropertyMap(
      this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "optionType",
        "strike",
        "currency",
        "underlyingIdentifier",
        "exerciseType",
        "expiry",
        "pointValue",
        "exchange");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 1373587791:  // optionType
          return _optionType;
        case -891985998:  // strike
          return _strike;
        case 575402001:  // currency
          return _currency;
        case 368639974:  // underlyingIdentifier
          return _underlyingIdentifier;
        case -466331342:  // exerciseType
          return _exerciseType;
        case -1289159373:  // expiry
          return _expiry;
        case 1257391553:  // pointValue
          return _pointValue;
        case 1989774883:  // exchange
          return _exchange;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends EquityOptionSecurity> builder() {
      return new DirectBeanBuilder<EquityOptionSecurity>(new EquityOptionSecurity());
    }

    @Override
    public Class<? extends EquityOptionSecurity> beanType() {
      return EquityOptionSecurity.class;
    }

    @Override
    public Map<String, MetaProperty<Object>> metaPropertyMap() {
      return _map;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code optionType} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<OptionType> optionType() {
      return _optionType;
    }

    /**
     * The meta-property for the {@code strike} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> strike() {
      return _strike;
    }

    /**
     * The meta-property for the {@code currency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Currency> currency() {
      return _currency;
    }

    /**
     * The meta-property for the {@code underlyingIdentifier} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalId> underlyingIdentifier() {
      return _underlyingIdentifier;
    }

    /**
     * The meta-property for the {@code exerciseType} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExerciseType> exerciseType() {
      return _exerciseType;
    }

    /**
     * The meta-property for the {@code expiry} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Expiry> expiry() {
      return _expiry;
    }

    /**
     * The meta-property for the {@code pointValue} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> pointValue() {
      return _pointValue;
    }

    /**
     * The meta-property for the {@code exchange} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> exchange() {
      return _exchange;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
