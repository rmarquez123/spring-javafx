package gov.inl.glass3.conductors;

import gov.inl.glass3.customunits.LinearElectricalResistence;
import gov.inl.glass3.customunits.LinearHeatCapacity;
import gov.inl.glass3.customunits.RmSI;
import javax.measure.Measure;
import javax.measure.quantity.Length;
import javax.measure.quantity.Temperature;

/**
 *
 * @author Ricardo Marquez
 */
public class Conductor {

  private final String name;
  private final String type;
  private final Measure<Length> diameter;
  private final boolean steelCore;
  private final String cond_strand;
  private final String core_strand;
  private final String core_od;
  private final String metal_od;
  private final String strandLayer;
  private final String twType;
  private final String twLayers;
  private final String alWeight;
  private final String stWeight;
  private final Measure<Temperature> minTemperature;
  private final Measure<Temperature> maxTemperature;
  private final Measure<LinearElectricalResistence> minResistence;
  private final Measure<LinearElectricalResistence> maxResistence;

  /**
   *
   * @param name
   * @param type
   * @param diameter
   * @param steelCore
   * @param cond_strand
   * @param core_strand
   * @param core_od
   * @param metal_od
   * @param strandLayer
   * @param twType
   * @param twLayers
   * @param alWeight
   * @param stWeight
   * @param minTemperature
   * @param maxTemperature
   * @param minResistence
   * @param maxResistence
   */
  public Conductor(String name, String type, Measure<Length> diameter, boolean steelCore,
    String cond_strand, String core_strand, String core_od, String metal_od,
    String strandLayer, String twType, String twLayers, String alWeight,
    String stWeight,
    Measure<Temperature> minTemperature,
    Measure<Temperature> maxTemperature,
    Measure<LinearElectricalResistence> minResistence,
    Measure<LinearElectricalResistence> maxResistence
  ) {
    this.name = name;
    this.type = type;
    this.diameter = diameter;
    this.steelCore = steelCore;
    this.cond_strand = cond_strand;
    this.core_strand = core_strand;
    this.core_od = core_od;
    this.metal_od = metal_od;
    this.strandLayer = strandLayer;
    this.twType = twType;
    this.twLayers = twLayers;
    this.alWeight = alWeight;
    this.stWeight = stWeight;
    this.minTemperature = minTemperature;
    this.maxTemperature = maxTemperature;
    this.minResistence = minResistence;
    this.maxResistence = maxResistence;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public Measure<Length> getDiameter() {
    return diameter;
  }

  public boolean isSteelCore() {
    return steelCore;
  }

  public String getCond_strand() {
    return cond_strand;
  }

  public String getCore_strand() {
    return core_strand;
  }

  public String getCore_od() {
    return core_od;
  }

  public String getMetal_od() {
    return metal_od;
  }

  public String getStrandLayer() {
    return strandLayer;
  }

  public String getTwType() {
    return twType;
  }

  public String getTwLayers() {
    return twLayers;
  }

  public String getAlWeight() {
    return alWeight;
  }

  public String getStWeight() {
    return stWeight;
  }

  public Measure<Temperature> getMinTemperature() {
    return minTemperature;
  }

  public Measure<Temperature> getMaxTemperature() {
    return maxTemperature;
  }

  public Measure<LinearElectricalResistence> getMinResistence() {
    return minResistence;
  }

  public Measure<LinearElectricalResistence> getMaxResistence() {
    return maxResistence;
  }

  @Override
  public String toString() {
    return "Conductor{" + "name=" + name + '}';
  }

  public Measure<LinearHeatCapacity> getLinearHeatCapacity() {
    Measure<LinearHeatCapacity> result = Measure.valueOf(1.116*897, RmSI.JL_PER_METER_PER_KEL);
    return result;
  }

  /**
   *
   */
  public static class Builder {

    private String name;
    private String type;
    private Measure<Length> diameter;
    private boolean steelCore;
    private String cond_strand;
    private String core_strand;
    private String core_od;
    private String metal_od;
    private String strandLayer;
    private String twType;
    private String twLayers;
    private String alWeight;
    private String stWeight;
    private Measure<Temperature> minTemperature;
    private Measure<Temperature> maxTemperature;
    private Measure<LinearElectricalResistence> minResistence;
    private Measure<LinearElectricalResistence> maxResistence;

    public Builder() {
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setType(String type) {
      this.type = type;
      return this;
    }

    public Builder setDiameter(Measure<Length> diameter) {
      this.diameter = diameter;
      return this;
    }

    public Builder setSteelCore(boolean steelCore) {
      this.steelCore = steelCore;
      return this;
    }

    public Builder setCond_strand(String cond_strand) {
      this.cond_strand = cond_strand;
      return this;
    }

    public Builder setCore_strand(String core_strand) {
      this.core_strand = core_strand;
      return this;
    }

    public Builder setCore_od(String core_od) {
      this.core_od = core_od;
      return this;
    }

    public Builder setMetal_od(String metal_od) {
      this.metal_od = metal_od;
      return this;
    }

    public Builder setStrandLayer(String strandLayer) {
      this.strandLayer = strandLayer;
      return this;
    }

    public Builder setTwType(String twType) {
      this.twType = twType;
      return this;
    }

    public Builder setTwLayers(String twLayers) {
      this.twLayers = twLayers;
      return this;
    }

    public Builder setAlWeight(String alWeight) {
      this.alWeight = alWeight;
      return this;
    }

    public Builder setStWeight(String stWeight) {
      this.stWeight = stWeight;
      return this;
    }

    public Builder setMinTemperature(Measure<Temperature> minTemperature) {
      this.minTemperature = minTemperature;
      return this;
    }

    public Builder setMaxTemperature(Measure<Temperature> maxTemperature) {
      this.maxTemperature = maxTemperature;
      return this;
    }

    public Builder setMinResistence(Measure<LinearElectricalResistence> minResistence) {
      this.minResistence = minResistence;
      return this;
    }

    public Builder setMaxResistence(Measure<LinearElectricalResistence> maxResistence) {
      this.maxResistence = maxResistence;
      return this;
    }
    
    /**
     * 
     * @return 
     */
    public Conductor build() {
      return new Conductor(
        name, type, diameter,
        steelCore, cond_strand, core_strand, core_od,
        metal_od, strandLayer, twType, twLayers,
        alWeight, stWeight, minTemperature,
        maxTemperature, minResistence, maxResistence);
    }

  }

}
