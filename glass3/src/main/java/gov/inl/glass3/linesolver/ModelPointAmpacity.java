package gov.inl.glass3.linesolver;

import java.time.ZonedDateTime;
import java.util.Objects;
import javax.measure.Measure;
import javax.measure.quantity.ElectricCurrent;

/**
 *
 * @author Ricardo Marquez
 */
public class ModelPointAmpacity implements Comparable<ModelPointAmpacity>{

  /**
   *
   */
  private final String modelPointId;

  /**
   *
   */
  private final ZonedDateTime dateTime;
  /**
   *
   */
  private final Measure<ElectricCurrent> ampacity;

  /**
   *
   * @param modelPointId
   * @param dateTime
   * @param ampacity
   */
  public ModelPointAmpacity(String modelPointId, ZonedDateTime dateTime, Measure<ElectricCurrent> ampacity) {
    this.modelPointId = modelPointId;
    this.dateTime = dateTime;
    this.ampacity = ampacity;
  }

  public String getModelPointId() {
    return modelPointId;
  }

  public ZonedDateTime getDateTime() {
    return dateTime;
  }

  public Measure<ElectricCurrent> getAmpacity() {
    return ampacity;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 97 * hash + Objects.hashCode(this.modelPointId);
    hash = 97 * hash + Objects.hashCode(this.dateTime);
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
    final ModelPointAmpacity other = (ModelPointAmpacity) obj;
    if (!Objects.equals(this.modelPointId, other.modelPointId)) {
      return false;
    }
    if (!Objects.equals(this.dateTime, other.dateTime)) {
      return false;
    }
    return true;
  }

  @Override
  public int compareTo(ModelPointAmpacity o) {
    int result = this.modelPointId.compareTo(o.modelPointId); 
    if (result != 0) {
      return result;
    }
    return this.dateTime.compareTo(o.dateTime);
  }
  
  
  
  

  @Override
  public String toString() {
    return "ModelPointAmpacity{" + "modelPointId=" + modelPointId + ", dateTime=" + dateTime + ", ampacity=" + ampacity + '}';
  }

}
