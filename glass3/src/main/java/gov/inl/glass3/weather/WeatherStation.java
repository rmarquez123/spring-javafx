package gov.inl.glass3.weather;

import com.vividsolutions.jts.geom.Point;
import java.util.Objects;

/**
 *
 * @author Ricardo Marquez
 */
public class WeatherStation {

  private final String name;
  private final double elevation;
  private final Point geometry;
  
  /**
   * 
   * @param name
   * @param elevation
   * @param geometry 
   */
  private WeatherStation(String name, double elevation, Point geometry) {
    this.name = name;
    this.elevation = elevation;
    this.geometry = geometry;
  }

  public String getName() {
    return name;
  }

  public double getElevation() {
    return elevation;
  }

  public Point getGeometry() {
    return geometry;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 97 * hash + Objects.hashCode(this.name);
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
    final WeatherStation other = (WeatherStation) obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "WeatherStation{" + "name=" + name + ", elevation=" + elevation + ", geometry=" + geometry + '}';
  }
  
  
  
  /**
   * 
   */
  public static class Builder {

    private String name;
    private double elevation;
    private Point geometry;

    public Builder() {
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setElevation(double elevation) {
      this.elevation = elevation;
      return this;
    }

    public Builder setGeometry(Point geometry) {
      this.geometry = geometry;
      return this;
    }

    /**
     *
     * @return
     */
    public WeatherStation build() {
      if (this.name == null) {
        throw new NullPointerException("name cannot be null");
      }
      if (this.geometry == null) {
        throw new NullPointerException("geometry cannot be null");
      }
      WeatherStation result = new WeatherStation(name, elevation, geometry);
      return result;
    }
  }
}
