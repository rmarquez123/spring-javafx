package com.rm.wpls.powerline;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import java.util.Objects;

/**
 *
 * @author Ricardo Marquez
 */
public class TransmissionLine implements Comparable<TransmissionLine> {

  private final String lineName;
  private final LineString geom;

  /**
   *
   * @param lineName
   * @param geom
   */
  public TransmissionLine(String lineName, LineString geom) {
    if (lineName == null) {
      throw new NullPointerException("linename cannot be null");
    }
    if (geom == null) {
      throw new NullPointerException("geometry `cannot be null");
    }
    this.lineName = lineName;
    this.geom = geom;
  }

  public String getLineName() {
    return lineName;
  }

  public LineString getGeom() {
    return geom;
  }
  
  /**
   * 
   * @return 
   */
  public Point getCenter() {
    return this.geom.getCentroid();
  }
  
  /**
   *
   * @return
   */
  public Envelope getEnvelope() {
    return this.geom.getEnvelopeInternal();
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 79 * hash + Objects.hashCode(this.lineName);
    hash = 79 * hash + Objects.hashCode(this.geom);
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
    final TransmissionLine other = (TransmissionLine) obj;
    if (!Objects.equals(this.lineName, other.lineName)) {
      return false;
    }
    if (!Objects.equals(this.geom, other.geom)) {
      return false;
    }
    return true;
  }

  @Override
  public int compareTo(TransmissionLine other) {
    int result = this.lineName.compareTo(other.lineName);
    return result;
  }

  @Override
  public String toString() {
    return "TransmissionLine{" + "lineName=" + lineName + ", geom=" + geom + '}';
  }
}
