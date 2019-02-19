package gov.inl.glass3.modelpoints;

import java.util.Objects;

/**
 *
 * @author Ricardo Marquez
 */
public class ModelPoint {
  
  private final String modelPointId;
  private final String weatherStationId;
  private final String lineId;
  private final ModelPointGeometry geometry;

  /**
   * 
   * @param modelPointId
   * @param weatherStationId
   * @param lineId
   * @param geometry 
   */
  public ModelPoint(String modelPointId, String weatherStationId, String lineId, ModelPointGeometry geometry) {
    this.modelPointId = modelPointId;
    this.weatherStationId = weatherStationId;
    this.lineId = lineId;
    this.geometry = geometry;
  }

  public String getModelPointId() {
    return modelPointId;
  }

  public String getWeatherStationId() {
    return weatherStationId;
  }

  public String getLineId() {
    return lineId;
  }

  public ModelPointGeometry getGeometry() {
    return geometry;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 61 * hash + Objects.hashCode(this.modelPointId);
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
    final ModelPoint other = (ModelPoint) obj;
    if (!Objects.equals(this.modelPointId, other.modelPointId)) {
      return false;
    }
    return true;
  }
  
  
  
  @Override
  public String toString() {
    return "ModelPoint{" + "modelPointId=" + modelPointId + ", weatherStationId=" + weatherStationId + ", lineId=" + lineId + ", geometry=" + geometry + '}';
  }
  
  
  
  
  
  
  
  
}
