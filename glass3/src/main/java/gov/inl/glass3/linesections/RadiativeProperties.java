package gov.inl.glass3.linesections;

/**
 *
 * @author Ricardo Marquez
 */
public class RadiativeProperties {

  private final double absorptivity;
  private final double emissivity;

  /**
   *
   * @param absorptivity
   * @param emissivity
   */
  public RadiativeProperties(double absorptivity, double emissivity) {
    if (!(0.0 <= absorptivity && absorptivity <= 1.0)) {
      throw new IllegalArgumentException("Absorptivity must be between 0 and 1.0, instead value was '" + absorptivity + "'");
    }
    if (!(0.0 <= emissivity && emissivity <= 1.0)) {
      throw new IllegalArgumentException("Emissivity must be between 0 and 1.0, instead value was '" + emissivity + "'");
    }
    this.absorptivity = absorptivity;
    this.emissivity = emissivity;
  }

  public double getAbsorptivity() {
    return absorptivity;
  }

  public double getEmissivity() {
    return emissivity;
  }
  
  @Override
  public String toString() {
    return "RadiativeProperties{" + "absorptivity=" + absorptivity + ", emissivity=" + emissivity + '}';
  }

}
