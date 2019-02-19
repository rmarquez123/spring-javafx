package gov.inl.glass3.linesections;

import java.util.Objects;
import javax.measure.Measure;
import javax.measure.quantity.Temperature;

/**
 *
 * @author Ricardo Marquez
 */
public class LineSection {

  private final String lineSectionId;
  private final String conductorId;
  private final int bundle;
  private final int derating;
  private final int airQuality;
  private final Measure<Temperature> emergencyTemperature;
  private final Measure<Temperature> maxTemperature;
  private final RadiativeProperties radProps;
  private final StaticEnvironmentConditions staticWeather;

  /**
   *
   * @param lineSectionId
   * @param conductorId
   * @param bundle
   * @param derating
   * @param airQuality
   * @param emergencyTemperature
   * @param maxTemperature
   * @param radProps
   * @param staticWeather
   */
  public LineSection(String lineSectionId, String conductorId, int bundle,
    int derating, int airQuality, Measure<Temperature> emergencyTemperature, Measure<Temperature> maxTemperature,
    RadiativeProperties radProps, StaticEnvironmentConditions staticWeather) {
    this.lineSectionId = lineSectionId;
    this.conductorId = conductorId;
    this.bundle = bundle;
    this.derating = derating;
    this.emergencyTemperature = emergencyTemperature;
    this.maxTemperature = maxTemperature;
    this.radProps = radProps;
    this.staticWeather = staticWeather;
    this.airQuality = airQuality;
  }

  public String getLineSectionId() {
    return lineSectionId;
  }

  public String getConductorId() {
    return conductorId;
  }

  public int getBundle() {
    return bundle;
  }

  public int getDerating() {
    return derating;
  }

  public Measure<Temperature> getEmergencyTemperature() {
    return emergencyTemperature;
  }

  public Measure<Temperature> getMaxTemperature() {
    return maxTemperature;
  }

  public RadiativeProperties getRadProps() {
    return radProps;
  }

  public StaticEnvironmentConditions getStaticWeather() {
    return staticWeather;
  }
  
  public double getEmissivity() {
    return this.radProps.getEmissivity();
  }

  public int getAirQuality() {
    return this.airQuality;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.lineSectionId);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final LineSection other = (LineSection) obj;
    if (!Objects.equals(this.lineSectionId, other.lineSectionId)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "LineSection{" + "lineSectionId=" + lineSectionId + ", conductorId=" + conductorId + ", bundle=" + bundle + ", derating=" + derating + ", emergencyTemperature=" + emergencyTemperature + ", maxTemperature=" + maxTemperature + ", radProps=" + radProps + ", staticWeather=" + staticWeather + '}';
  }

  

  public static class Builder {

    private String lineSectionId;
    private String conductorId;
    private int bundle;
    private int derating;
    private int airQuality;
    private Measure<Temperature> emergencyTemperature;
    private Measure<Temperature> maxTemperature;
    private RadiativeProperties radProps;
    private StaticEnvironmentConditions staticWeather;

    public Builder() {

    }

    public Builder setLineSectionId(String lineSectionId) {
      this.lineSectionId = lineSectionId;
      return this;
    }

    public Builder setConductorId(String conductorId) {
      this.conductorId = conductorId;
      return this;
    }

    public Builder setBundle(int bundle) {
      this.bundle = bundle;
      return this;
    }

    public Builder setDerating(int derating) {
      this.derating = derating;
      return this;
    }
    
    public Builder setAirQuality(int airQuality) {
      this.airQuality = airQuality;
      return this;
    }

    public Builder setEmergencyTemperature(Measure<Temperature> emergencyTemperature) {
      this.emergencyTemperature = emergencyTemperature;
      return this;
    }

    public Builder setMaxTemperature(Measure<Temperature> maxTemperature) {
      this.maxTemperature = maxTemperature;
      return this;
    }

    public Builder setRadProps(RadiativeProperties radProps) {
      this.radProps = radProps;
      return this;
    }

    public Builder setStaticWeather(StaticEnvironmentConditions staticWeather) {
      this.staticWeather = staticWeather;
      return this;
    }

    /**
     *
     * @return
     */
    public LineSection build() {
      return new LineSection(lineSectionId, conductorId, bundle, derating, airQuality, emergencyTemperature, maxTemperature, radProps, staticWeather);
    }
  }

}
