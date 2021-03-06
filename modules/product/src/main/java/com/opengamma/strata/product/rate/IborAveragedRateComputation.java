/*
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.rate;

import static com.opengamma.strata.collect.Guavate.ensureOnlyOne;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.ImmutableConstructor;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;
import org.joda.beans.impl.direct.DirectPrivateBeanBuilder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.opengamma.strata.basics.index.IborIndex;
import com.opengamma.strata.basics.index.Index;

/**
 * Defines the computation of a rate of interest based on the average of multiple
 * fixings of a single Ibor floating rate index.
 * <p>
 * An interest rate determined from a single Ibor index observed on multiple dates.
 * For example, the average of three fixings of 'GBP-LIBOR-3M'.
 */
@BeanDefinition(builderScope = "private")
public final class IborAveragedRateComputation
    implements RateComputation, ImmutableBean, Serializable {

  /**
   * The list of fixings.
   * <p>
   * A fixing will be taken for each reset period, with the final rate
   * being an average of the fixings.
   */
  @PropertyDefinition(validate = "notEmpty")
  private final ImmutableList<IborAveragedFixing> fixings;
  /**
   * The total weight of all the fixings in this computation.
   */
  private final transient double totalWeight;  // not a property, derived and cached from input data

  //-------------------------------------------------------------------------
  /**
   * Creates an instance from the individual fixings.
   * <p>
   * All the fixings must have the same index.
   * 
   * @param fixings  the weighted fixings
   * @return the averaged rate computation
   */
  public static IborAveragedRateComputation of(List<IborAveragedFixing> fixings) {
    return new IborAveragedRateComputation(fixings);
  }

  //-------------------------------------------------------------------------
  @ImmutableConstructor
  private IborAveragedRateComputation(List<IborAveragedFixing> fixings) {
    fixings.stream()
        .map(f -> f.getObservation().getIndex())
        .distinct()
        .reduce(ensureOnlyOne());
    this.fixings = ImmutableList.copyOf(fixings);
    this.totalWeight = fixings.stream()
        .mapToDouble(f -> f.getWeight())
        .sum();
  }

  // ensure standard constructor is invoked
  private Object readResolve() {
    return new IborAveragedRateComputation(fixings);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the Ibor index.
   * <p>
   * The rate to be paid is based on this index
   * It will be a well known market index such as 'GBP-LIBOR-3M'.
   * 
   * @return the Ibor index
   */
  public IborIndex getIndex() {
    return fixings.get(0).getObservation().getIndex();
  }

  //-------------------------------------------------------------------------
  /**
   * Gets total weight of all the fixings in this observation.
   * 
   * @return the total weight
   */
  public double getTotalWeight() {
    return totalWeight;
  }

  //-------------------------------------------------------------------------
  @Override
  public void collectIndices(ImmutableSet.Builder<Index> builder) {
    builder.add(getIndex());
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code IborAveragedRateComputation}.
   * @return the meta-bean, not null
   */
  public static IborAveragedRateComputation.Meta meta() {
    return IborAveragedRateComputation.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(IborAveragedRateComputation.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  @Override
  public IborAveragedRateComputation.Meta metaBean() {
    return IborAveragedRateComputation.Meta.INSTANCE;
  }

  @Override
  public <R> Property<R> property(String propertyName) {
    return metaBean().<R>metaProperty(propertyName).createProperty(this);
  }

  @Override
  public Set<String> propertyNames() {
    return metaBean().metaPropertyMap().keySet();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the list of fixings.
   * <p>
   * A fixing will be taken for each reset period, with the final rate
   * being an average of the fixings.
   * @return the value of the property, not empty
   */
  public ImmutableList<IborAveragedFixing> getFixings() {
    return fixings;
  }

  //-----------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      IborAveragedRateComputation other = (IborAveragedRateComputation) obj;
      return JodaBeanUtils.equal(fixings, other.fixings);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(fixings);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("IborAveragedRateComputation{");
    buf.append("fixings").append('=').append(JodaBeanUtils.toString(fixings));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code IborAveragedRateComputation}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code fixings} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<ImmutableList<IborAveragedFixing>> fixings = DirectMetaProperty.ofImmutable(
        this, "fixings", IborAveragedRateComputation.class, (Class) ImmutableList.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "fixings");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -843784602:  // fixings
          return fixings;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends IborAveragedRateComputation> builder() {
      return new IborAveragedRateComputation.Builder();
    }

    @Override
    public Class<? extends IborAveragedRateComputation> beanType() {
      return IborAveragedRateComputation.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code fixings} property.
     * @return the meta-property, not null
     */
    public MetaProperty<ImmutableList<IborAveragedFixing>> fixings() {
      return fixings;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -843784602:  // fixings
          return ((IborAveragedRateComputation) bean).getFixings();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      metaProperty(propertyName);
      if (quiet) {
        return;
      }
      throw new UnsupportedOperationException("Property cannot be written: " + propertyName);
    }

  }

  //-----------------------------------------------------------------------
  /**
   * The bean-builder for {@code IborAveragedRateComputation}.
   */
  private static final class Builder extends DirectPrivateBeanBuilder<IborAveragedRateComputation> {

    private List<IborAveragedFixing> fixings = ImmutableList.of();

    /**
     * Restricted constructor.
     */
    private Builder() {
      super(meta());
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case -843784602:  // fixings
          return fixings;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case -843784602:  // fixings
          this.fixings = (List<IborAveragedFixing>) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public IborAveragedRateComputation build() {
      return new IborAveragedRateComputation(
          fixings);
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(64);
      buf.append("IborAveragedRateComputation.Builder{");
      buf.append("fixings").append('=').append(JodaBeanUtils.toString(fixings));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
