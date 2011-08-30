/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.fx;

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

/**
 * A security for FX.
 */
@BeanDefinition
public class FXSecurity extends FinancialSecurity {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The security type.
   */
  public static final String SECURITY_TYPE = "FX";

  /**
   * The pay currency.
   */
  @PropertyDefinition(validate = "notNull")
  private Currency _payCurrency;
  /**
   * The receive currency.
   */
  @PropertyDefinition(validate = "notNull")
  private Currency _receiveCurrency;
  /**
   * The pay amount.
   */
  @PropertyDefinition
  private double _payAmount;
  /**
   * The receive amount.
   */
  @PropertyDefinition
  private double _receiveAmount;
  /**
   * The region.
   */
  @PropertyDefinition(validate = "notNull")
  private ExternalId _region;

  /**
   * Creates an empty instance.
   * <p>
   * The security details should be set before use.
   */
  public FXSecurity() {
  }

  public FXSecurity(Currency payCurrency, Currency receiveCurrency, double payAmount, double receiveAmount, ExternalId region) {
    super(SECURITY_TYPE);
    setPayCurrency(payCurrency);
    setReceiveCurrency(receiveCurrency);
    setPayAmount(payAmount);
    setReceiveAmount(receiveAmount);
    setRegion(region);
  }

  //-------------------------------------------------------------------------
  @Override
  public final <T> T accept(FinancialSecurityVisitor<T> visitor) {
    return visitor.visitFXSecurity(this);
  }

  /**
   * Accepts a visitor to manage traversal of the hierarchy.
   * 
   * @param <T> the result type of the visitor
   * @param visitor  the visitor, not null
   * @return the result
   */
  public <T> T accept(FXSecurityVisitor<T> visitor) {
    return visitor.visitFXSecurity(this);
  };

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code FXSecurity}.
   * @return the meta-bean, not null
   */
  public static FXSecurity.Meta meta() {
    return FXSecurity.Meta.INSTANCE;
  }
  static {
    JodaBeanUtils.registerMetaBean(FXSecurity.Meta.INSTANCE);
  }

  @Override
  public FXSecurity.Meta metaBean() {
    return FXSecurity.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -295641895:  // payCurrency
        return getPayCurrency();
      case -1228590060:  // receiveCurrency
        return getReceiveCurrency();
      case -1338781920:  // payAmount
        return getPayAmount();
      case 984267035:  // receiveAmount
        return getReceiveAmount();
      case -934795532:  // region
        return getRegion();
    }
    return super.propertyGet(propertyName, quiet);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -295641895:  // payCurrency
        setPayCurrency((Currency) newValue);
        return;
      case -1228590060:  // receiveCurrency
        setReceiveCurrency((Currency) newValue);
        return;
      case -1338781920:  // payAmount
        setPayAmount((Double) newValue);
        return;
      case 984267035:  // receiveAmount
        setReceiveAmount((Double) newValue);
        return;
      case -934795532:  // region
        setRegion((ExternalId) newValue);
        return;
    }
    super.propertySet(propertyName, newValue, quiet);
  }

  @Override
  protected void validate() {
    JodaBeanUtils.notNull(_payCurrency, "payCurrency");
    JodaBeanUtils.notNull(_receiveCurrency, "receiveCurrency");
    JodaBeanUtils.notNull(_region, "region");
    super.validate();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      FXSecurity other = (FXSecurity) obj;
      return JodaBeanUtils.equal(getPayCurrency(), other.getPayCurrency()) &&
          JodaBeanUtils.equal(getReceiveCurrency(), other.getReceiveCurrency()) &&
          JodaBeanUtils.equal(getPayAmount(), other.getPayAmount()) &&
          JodaBeanUtils.equal(getReceiveAmount(), other.getReceiveAmount()) &&
          JodaBeanUtils.equal(getRegion(), other.getRegion()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += hash * 31 + JodaBeanUtils.hashCode(getPayCurrency());
    hash += hash * 31 + JodaBeanUtils.hashCode(getReceiveCurrency());
    hash += hash * 31 + JodaBeanUtils.hashCode(getPayAmount());
    hash += hash * 31 + JodaBeanUtils.hashCode(getReceiveAmount());
    hash += hash * 31 + JodaBeanUtils.hashCode(getRegion());
    return hash ^ super.hashCode();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the pay currency.
   * @return the value of the property, not null
   */
  public Currency getPayCurrency() {
    return _payCurrency;
  }

  /**
   * Sets the pay currency.
   * @param payCurrency  the new value of the property, not null
   */
  public void setPayCurrency(Currency payCurrency) {
    JodaBeanUtils.notNull(payCurrency, "payCurrency");
    this._payCurrency = payCurrency;
  }

  /**
   * Gets the the {@code payCurrency} property.
   * @return the property, not null
   */
  public final Property<Currency> payCurrency() {
    return metaBean().payCurrency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the receive currency.
   * @return the value of the property, not null
   */
  public Currency getReceiveCurrency() {
    return _receiveCurrency;
  }

  /**
   * Sets the receive currency.
   * @param receiveCurrency  the new value of the property, not null
   */
  public void setReceiveCurrency(Currency receiveCurrency) {
    JodaBeanUtils.notNull(receiveCurrency, "receiveCurrency");
    this._receiveCurrency = receiveCurrency;
  }

  /**
   * Gets the the {@code receiveCurrency} property.
   * @return the property, not null
   */
  public final Property<Currency> receiveCurrency() {
    return metaBean().receiveCurrency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the pay amount.
   * @return the value of the property
   */
  public double getPayAmount() {
    return _payAmount;
  }

  /**
   * Sets the pay amount.
   * @param payAmount  the new value of the property
   */
  public void setPayAmount(double payAmount) {
    this._payAmount = payAmount;
  }

  /**
   * Gets the the {@code payAmount} property.
   * @return the property, not null
   */
  public final Property<Double> payAmount() {
    return metaBean().payAmount().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the receive amount.
   * @return the value of the property
   */
  public double getReceiveAmount() {
    return _receiveAmount;
  }

  /**
   * Sets the receive amount.
   * @param receiveAmount  the new value of the property
   */
  public void setReceiveAmount(double receiveAmount) {
    this._receiveAmount = receiveAmount;
  }

  /**
   * Gets the the {@code receiveAmount} property.
   * @return the property, not null
   */
  public final Property<Double> receiveAmount() {
    return metaBean().receiveAmount().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the region.
   * @return the value of the property, not null
   */
  public ExternalId getRegion() {
    return _region;
  }

  /**
   * Sets the region.
   * @param region  the new value of the property, not null
   */
  public void setRegion(ExternalId region) {
    JodaBeanUtils.notNull(region, "region");
    this._region = region;
  }

  /**
   * Gets the the {@code region} property.
   * @return the property, not null
   */
  public final Property<ExternalId> region() {
    return metaBean().region().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code FXSecurity}.
   */
  public static class Meta extends FinancialSecurity.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code payCurrency} property.
     */
    private final MetaProperty<Currency> _payCurrency = DirectMetaProperty.ofReadWrite(
        this, "payCurrency", FXSecurity.class, Currency.class);
    /**
     * The meta-property for the {@code receiveCurrency} property.
     */
    private final MetaProperty<Currency> _receiveCurrency = DirectMetaProperty.ofReadWrite(
        this, "receiveCurrency", FXSecurity.class, Currency.class);
    /**
     * The meta-property for the {@code payAmount} property.
     */
    private final MetaProperty<Double> _payAmount = DirectMetaProperty.ofReadWrite(
        this, "payAmount", FXSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code receiveAmount} property.
     */
    private final MetaProperty<Double> _receiveAmount = DirectMetaProperty.ofReadWrite(
        this, "receiveAmount", FXSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code region} property.
     */
    private final MetaProperty<ExternalId> _region = DirectMetaProperty.ofReadWrite(
        this, "region", FXSecurity.class, ExternalId.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<Object>> _map = new DirectMetaPropertyMap(
      this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "payCurrency",
        "receiveCurrency",
        "payAmount",
        "receiveAmount",
        "region");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -295641895:  // payCurrency
          return _payCurrency;
        case -1228590060:  // receiveCurrency
          return _receiveCurrency;
        case -1338781920:  // payAmount
          return _payAmount;
        case 984267035:  // receiveAmount
          return _receiveAmount;
        case -934795532:  // region
          return _region;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends FXSecurity> builder() {
      return new DirectBeanBuilder<FXSecurity>(new FXSecurity());
    }

    @Override
    public Class<? extends FXSecurity> beanType() {
      return FXSecurity.class;
    }

    @Override
    public Map<String, MetaProperty<Object>> metaPropertyMap() {
      return _map;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code payCurrency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Currency> payCurrency() {
      return _payCurrency;
    }

    /**
     * The meta-property for the {@code receiveCurrency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Currency> receiveCurrency() {
      return _receiveCurrency;
    }

    /**
     * The meta-property for the {@code payAmount} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> payAmount() {
      return _payAmount;
    }

    /**
     * The meta-property for the {@code receiveAmount} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> receiveAmount() {
      return _receiveAmount;
    }

    /**
     * The meta-property for the {@code region} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalId> region() {
      return _region;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
