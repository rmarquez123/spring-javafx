package fx.panzoomcanvas.core;

/**
 *
 * @author rmarquez
 */
public abstract class SpatialRef {

  private final int srid;
  private final Point max;
  private final Point min;

  /**
   *
   * @param srid
   */
  public SpatialRef(int srid, Point min, Point max) {
    this.srid = srid;
    this.min = min;
    this.max = max;
  }

  /**
   *
   * @return
   */
  public int getSrid() {
    return srid;
  }

  /**
   *
   * @return
   */
  public final Point getMax() {
    return this.max;
  }

  /**
   *
   * @return
   */
  public final Point getMin() {
    return this.min;
  }

  /**
   *
   * @return
   */
  public double getWidth() {
    return this.getMax().getX() - this.getMin().getX();
  }

  /**
   *
   * @return
   */
  public double getHeight() {
    return this.getMax().getY() - this.getMin().getY();
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public final int hashCode() {
    int hash = 5;
    hash = 11 * hash + this.srid;
    return hash;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public final boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final SpatialRef other = (SpatialRef) obj;
    if (this.srid != other.srid) {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public String toString() {
    return "SpatailRef{" + "srid=" + srid + ", max=" + max + ", min=" + min + '}';
  }

}
