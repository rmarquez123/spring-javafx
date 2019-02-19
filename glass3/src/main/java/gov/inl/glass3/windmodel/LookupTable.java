package gov.inl.glass3.windmodel;

import java.util.Objects;

/**
 *
 * @author Ricardo Marquez
 */
public class LookupTable {

  private final String modelPointId;
  private final double sectorNum;
  private final double speedUp;
  private final double directionShift;

  public LookupTable(String modelPointId, double sectorNum, double speedUp, double directionShift) {
    this.modelPointId = modelPointId;
    this.sectorNum = sectorNum;
    this.speedUp = speedUp;
    this.directionShift = directionShift;
  }

  public String getModelPointId() {
    return modelPointId;
  }

  public double getSectorNum() {
    return sectorNum;
  }

  public double getSpeedUp() {
    return speedUp;
  }

  public double getDirectionShift() {
    return directionShift;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 29 * hash + Objects.hashCode(this.modelPointId);
    hash = 29 * hash + (int) (Double.doubleToLongBits(this.sectorNum) ^ (Double.doubleToLongBits(this.sectorNum) >>> 32));
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
    final LookupTable other = (LookupTable) obj;
    if (Double.doubleToLongBits(this.sectorNum) != Double.doubleToLongBits(other.sectorNum)) {
      return false;
    }
    if (Double.doubleToLongBits(this.directionShift) != Double.doubleToLongBits(other.directionShift)) {
      return false;
    }
    if (!Objects.equals(this.modelPointId, other.modelPointId)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "LookupTable{" + "modelPointId=" + modelPointId + ", sectorNum=" + sectorNum + ", speedUp=" + speedUp + ", directionShift=" + directionShift + '}';
  }

  /**
   *
   */
  public static class Builder {

    private String modelPointId;
    private double sectorNum;
    private double speedUp;
    private double directionShift;

    public Builder() {
    }
    
    public Builder setModelPointId(String modelPointId) {
      this.modelPointId = modelPointId;
      return this;
    }

    public Builder setSectorNum(double sectorNum) {
      this.sectorNum = sectorNum;
      return this;
    }

    public Builder setSpeedUp(double speedUp) {
      this.speedUp = speedUp;
      return this;
    }

    public Builder setDirectionShift(double directionShift) {
      this.directionShift = directionShift;
      return this;
    }

    /**
     *
     * @return
     */
    public LookupTable build() {
      return new LookupTable(modelPointId, sectorNum, speedUp, directionShift);
    }

  }

}
