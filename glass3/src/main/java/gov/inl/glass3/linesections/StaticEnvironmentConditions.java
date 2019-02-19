package gov.inl.glass3.linesections;

/**
 *
 * @author Ricardo Marquez
 */
public class StaticEnvironmentConditions {

  private final double windspeed;
  private final double windir;
  private final double solar;

  /**
   *
   * @param windspeed
   * @param windir
   * @param solar
   */
  public StaticEnvironmentConditions(double windspeed, double windir, double solar) {
    this.windspeed = windspeed;
    this.windir = windir;
    this.solar = solar;
  }

  public double getWindspeed() {
    return windspeed;
  }

  public double getWindir() {
    return windir;
  }

  public double getSolar() {
    return solar;
  }

  @Override
  public String toString() {
    return "StaticEnvironmentConditions{" + "windspeed=" + windspeed + ", windir=" + windir + ", solar=" + solar + '}';
  }

}
