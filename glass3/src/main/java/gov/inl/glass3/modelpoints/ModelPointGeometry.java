package gov.inl.glass3.modelpoints;

import com.vividsolutions.jts.geom.Point;
import javax.measure.Measure;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Length;

/**
 *
 * @author Ricardo Marquez
 */
public class ModelPointGeometry {

  private final Measure<Length> elevation;
  private final Measure<Angle> azimuth;
  private final Point point;

  public ModelPointGeometry(Measure<Length> elevation, Measure<Angle> azimuth, Point point) {
    this.elevation = elevation;
    this.azimuth = azimuth;
    this.point = point;
  }

  /**
   *
   * @return
   */
  public Measure<Angle> getAzimuth() {
    return this.azimuth;
  }

  /**
   * +
   *
   * @return
   */
  public Measure<Length> getElevation() {
    return this.elevation;
  }

  /**
   *
   * @return
   */
  public Point getPoint() {
    return point;
  }

  /**
   *
   * @return
   */
  public double getLatitude() {
    double result;
    if (this.point.getSRID() == 4326) {
      result = this.point.getY();
    } else {
      throw new UnsupportedOperationException();
    }
    return result;
  }

  public static class Builder {

    private Measure<Length> elevation;
    private Measure<Angle> azimuth;
    private Point point;

    public Builder() {

    }

    public Builder setElevation(Measure<Length> elevation) {
      this.elevation = elevation;
      return this;
    }

    public Builder setAzimuth(Measure<Angle> azimuth) {
      this.azimuth = azimuth;
      return this;
    }

    public Builder setPoint(Point point) {
      this.point = point;
      return this;
    }
      
    /**
     * 
     * @return 
     */
    public ModelPointGeometry build() {
      return new ModelPointGeometry(elevation, azimuth, point); 
    }

  }

}
