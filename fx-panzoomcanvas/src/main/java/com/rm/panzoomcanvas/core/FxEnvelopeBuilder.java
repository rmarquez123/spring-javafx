package com.rm.panzoomcanvas.core;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rmarquez
 */
public class FxEnvelopeBuilder {

  private final SpatialRef spatialRef;
  private final List<Point> points = new ArrayList<>();

  public FxEnvelopeBuilder(SpatialRef spatialRef) {
    this.spatialRef = spatialRef;
  }

  /**
   *
   * @param pt
   */
  public void expandToInclude(Point pt) {
    this.points.add(pt);
  }

  /**
   *
   * @return
   */
  public FxEnvelope createEnvelope() {
    FxEnvelope result;
    if (this.points.size() > 1) {
      
      double minX = this.points.stream().map((p) -> p.getX())
              .min((Double o1, Double o2) -> o1.compareTo(o2)).get();
      double maxX = this.points.stream().map((p) -> p.getX())
              .max((Double o1, Double o2) -> o1.compareTo(o2)).get();
      
      
      double minY = this.points.stream().map((p) -> p.getY())
              .min((Double o1, Double o2) -> o1.compareTo(o2)).get();
      double maxY = this.points.stream().map((p) -> p.getY())
              .max((Double o1, Double o2) -> o1.compareTo(o2)).get();
      
      FxPoint minPt = new FxPoint(minX, minY, this.spatialRef);
      FxPoint maxPt = new FxPoint(maxX, maxY, this.spatialRef);
      result = new FxEnvelope(minPt, maxPt);
    } else {
      throw new RuntimeException("Number of points must be greater than 1"); 
    }
    return result;
  }
}
