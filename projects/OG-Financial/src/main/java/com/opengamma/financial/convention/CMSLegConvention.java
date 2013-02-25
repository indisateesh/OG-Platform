/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.convention;

import org.joda.beans.BeanDefinition;
import org.joda.beans.PropertyDefinition;
import org.threeten.bp.Period;

import com.opengamma.id.ExternalIdBundle;
import java.util.Map;
import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

/**
 * 
 */
@BeanDefinition
public class CMSLegConvention extends Convention {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The swap index convention.
   */
  @PropertyDefinition(validate = "notNull")
  private String _swapIndexConvention;

  /**
   * The payment period.
   */
  @PropertyDefinition(validate = "notNull")
  private Period _paymentPeriod;

  /**
   * Is the fixing in advance (true) or in arrears (false).
   */
  @PropertyDefinition
  private boolean _isAdvanceFixing;

  /**
   * For the builder.
   */
  public CMSLegConvention() {
    super();
  }

  public CMSLegConvention(final String name, final ExternalIdBundle externalIdBundle, final String swapIndexConvention, final Period paymentPeriod, final boolean isAdvanceFixing) {
    super(name, externalIdBundle);
    setSwapIndexConvention(swapIndexConvention);
    setPaymentPeriod(paymentPeriod);
    setIsAdvanceFixing(isAdvanceFixing);
  }
  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code CMSLegConvention}.
   * @return the meta-bean, not null
   */
  public static CMSLegConvention.Meta meta() {
    return CMSLegConvention.Meta.INSTANCE;
  }
  static {
    JodaBeanUtils.registerMetaBean(CMSLegConvention.Meta.INSTANCE);
  }

  @Override
  public CMSLegConvention.Meta metaBean() {
    return CMSLegConvention.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -1606291504:  // swapIndexConvention
        return getSwapIndexConvention();
      case 1331459943:  // paymentPeriod
        return getPaymentPeriod();
      case 1363941829:  // isAdvanceFixing
        return isIsAdvanceFixing();
    }
    return super.propertyGet(propertyName, quiet);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -1606291504:  // swapIndexConvention
        setSwapIndexConvention((String) newValue);
        return;
      case 1331459943:  // paymentPeriod
        setPaymentPeriod((Period) newValue);
        return;
      case 1363941829:  // isAdvanceFixing
        setIsAdvanceFixing((Boolean) newValue);
        return;
    }
    super.propertySet(propertyName, newValue, quiet);
  }

  @Override
  protected void validate() {
    JodaBeanUtils.notNull(_swapIndexConvention, "swapIndexConvention");
    JodaBeanUtils.notNull(_paymentPeriod, "paymentPeriod");
    super.validate();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      CMSLegConvention other = (CMSLegConvention) obj;
      return JodaBeanUtils.equal(getSwapIndexConvention(), other.getSwapIndexConvention()) &&
          JodaBeanUtils.equal(getPaymentPeriod(), other.getPaymentPeriod()) &&
          JodaBeanUtils.equal(isIsAdvanceFixing(), other.isIsAdvanceFixing()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += hash * 31 + JodaBeanUtils.hashCode(getSwapIndexConvention());
    hash += hash * 31 + JodaBeanUtils.hashCode(getPaymentPeriod());
    hash += hash * 31 + JodaBeanUtils.hashCode(isIsAdvanceFixing());
    return hash ^ super.hashCode();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the swap index convention.
   * @return the value of the property, not null
   */
  public String getSwapIndexConvention() {
    return _swapIndexConvention;
  }

  /**
   * Sets the swap index convention.
   * @param swapIndexConvention  the new value of the property, not null
   */
  public void setSwapIndexConvention(String swapIndexConvention) {
    JodaBeanUtils.notNull(swapIndexConvention, "swapIndexConvention");
    this._swapIndexConvention = swapIndexConvention;
  }

  /**
   * Gets the the {@code swapIndexConvention} property.
   * @return the property, not null
   */
  public final Property<String> swapIndexConvention() {
    return metaBean().swapIndexConvention().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the payment period.
   * @return the value of the property, not null
   */
  public Period getPaymentPeriod() {
    return _paymentPeriod;
  }

  /**
   * Sets the payment period.
   * @param paymentPeriod  the new value of the property, not null
   */
  public void setPaymentPeriod(Period paymentPeriod) {
    JodaBeanUtils.notNull(paymentPeriod, "paymentPeriod");
    this._paymentPeriod = paymentPeriod;
  }

  /**
   * Gets the the {@code paymentPeriod} property.
   * @return the property, not null
   */
  public final Property<Period> paymentPeriod() {
    return metaBean().paymentPeriod().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets is the fixing in advance (true) or in arrears (false).
   * @return the value of the property
   */
  public boolean isIsAdvanceFixing() {
    return _isAdvanceFixing;
  }

  /**
   * Sets is the fixing in advance (true) or in arrears (false).
   * @param isAdvanceFixing  the new value of the property
   */
  public void setIsAdvanceFixing(boolean isAdvanceFixing) {
    this._isAdvanceFixing = isAdvanceFixing;
  }

  /**
   * Gets the the {@code isAdvanceFixing} property.
   * @return the property, not null
   */
  public final Property<Boolean> isAdvanceFixing() {
    return metaBean().isAdvanceFixing().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code CMSLegConvention}.
   */
  public static class Meta extends Convention.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code swapIndexConvention} property.
     */
    private final MetaProperty<String> _swapIndexConvention = DirectMetaProperty.ofReadWrite(
        this, "swapIndexConvention", CMSLegConvention.class, String.class);
    /**
     * The meta-property for the {@code paymentPeriod} property.
     */
    private final MetaProperty<Period> _paymentPeriod = DirectMetaProperty.ofReadWrite(
        this, "paymentPeriod", CMSLegConvention.class, Period.class);
    /**
     * The meta-property for the {@code isAdvanceFixing} property.
     */
    private final MetaProperty<Boolean> _isAdvanceFixing = DirectMetaProperty.ofReadWrite(
        this, "isAdvanceFixing", CMSLegConvention.class, Boolean.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "swapIndexConvention",
        "paymentPeriod",
        "isAdvanceFixing");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -1606291504:  // swapIndexConvention
          return _swapIndexConvention;
        case 1331459943:  // paymentPeriod
          return _paymentPeriod;
        case 1363941829:  // isAdvanceFixing
          return _isAdvanceFixing;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends CMSLegConvention> builder() {
      return new DirectBeanBuilder<CMSLegConvention>(new CMSLegConvention());
    }

    @Override
    public Class<? extends CMSLegConvention> beanType() {
      return CMSLegConvention.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code swapIndexConvention} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> swapIndexConvention() {
      return _swapIndexConvention;
    }

    /**
     * The meta-property for the {@code paymentPeriod} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Period> paymentPeriod() {
      return _paymentPeriod;
    }

    /**
     * The meta-property for the {@code isAdvanceFixing} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Boolean> isAdvanceFixing() {
      return _isAdvanceFixing;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
