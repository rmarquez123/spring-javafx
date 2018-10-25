package com.rm.panzoomcanvas.projections;

import com.rm.panzoomcanvas.core.FxEnvelope;
import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.core.Point;
import com.rm.panzoomcanvas.core.SpatialRef;

/**
 *
 * @author rmarquez
 */
public class Projector {
  private final MapCanvasSR virtualSr = new MapCanvasSR();
  private final SpatialRef baseSpatialRef;
  private final GeometryProjection geomProject;

  /**
   *
   * @param baseSpatialRef
   * @param geomProj
   */
  public Projector(SpatialRef baseSpatialRef, GeometryProjection geomProj) {
    this.baseSpatialRef = baseSpatialRef;
    this.geomProject = geomProj;
  }

  /**
   *
   * @param screenEnvVal
   * @return
   */
  public VirtualEnvelope projectScreenToVirtualStrict(ScreenEnvelope screenEnvVal) {
    VirtualPoint vMinCheck = this.projectScreenToVirtual(screenEnvVal.getMin(), screenEnvVal);
    VirtualPoint vMaxCheck = this.projectScreenToVirtual(screenEnvVal.getMax(), screenEnvVal);
    double width = this.virtualSr.getWidth();
    double height = this.virtualSr.getHeight();
    double vxMin = Math.max(0 - 0.5 * width, vMinCheck.getX());
    double vyMin = Math.max(0 - 0.5 * height, vMaxCheck.getY());
    double vxMax = Math.min(0 + 0.5 * width, vMaxCheck.getX());
    double vyMax = Math.min(0 + 0.5 * height, vMinCheck.getY());

    VirtualPoint vMin = new VirtualPoint(vxMin, vyMin);
    VirtualPoint vMax = new VirtualPoint(vxMax, vyMax);
    VirtualEnvelope result = new VirtualEnvelope(vMin, vMax);
    return result;
  }

  /**
   * Projects screen envelope to a virtual envelope.
   *
   * @param screenEnv
   * @return the virtual envelope
   */
  public VirtualEnvelope projectScreenToVirtual(ScreenEnvelope screenEnv) {
    VirtualEnvelope result;
    ScreenPoint max = screenEnv.getMax();
    VirtualPoint vMax = this.projectScreenToVirtual(max, screenEnv);
    ScreenPoint min = screenEnv.getMin();
    VirtualPoint vMin = this.projectScreenToVirtual(min, screenEnv);
    result = new VirtualEnvelope(vMin, vMax);
    return result;
  }

  /**
   * Projects screen coordinate to a virtual coordinate.
   *
   * @param scrnPt
   * @param env
   * @return the virtual point.
   */
  public VirtualPoint projectScreenToVirtual(ScreenPoint scrnPt, ScreenEnvelope env) {
    VirtualPoint result;
    Level level = env.getLevel();
    ScreenPoint scrnCenter = env.getCenter();
    double width = this.virtualSr.getWidth();
    double height = this.virtualSr.getHeight();
    Point virtualMin = this.virtualSr.getMin();
    Point virtualMax = this.virtualSr.getMax();
    double f = Math.pow(2, -level.getValue());
    double scaleX = 1;
    double scaleY = 1;
    double vX = virtualMin.getX() + scaleX * f * (scrnPt.getX() - scrnCenter.getX()) + 0.5 * width;
    double vY = virtualMax.getY() - scaleY * f * (scrnPt.getY() - scrnCenter.getY()) - 0.5 * height;
    result = new VirtualPoint(vX, vY);
    return result;
  }

  /**
   *
   * @param virtualEnv
   * @param screenEnv
   * @return
   */
  public ScreenEnvelope projectVirtualToScreen(
          VirtualEnvelope virtualEnv, ScreenEnvelope screenEnv) {
    ScreenPoint min = this.projectVirtualToScreen(virtualEnv.getMin(), screenEnv);
    ScreenPoint max = this.projectVirtualToScreen(virtualEnv.getMax(), screenEnv);
    return new ScreenEnvelope(
            new ScreenPoint(min.getX(), max.getY()),
            new ScreenPoint(max.getX(), min.getY()),
            screenEnv.getLevel(),
            screenEnv.getCenter());
  }

  /**
   *
   * @param pt
   * @param scrnEvn
   * @return
   */
  public ScreenPoint projectVirtualToScreen(Point pt, ScreenEnvelope scrnEvn) {
    Level level = scrnEvn.getLevel();
    ScreenPoint scrnCenter = scrnEvn.getCenter();
    double width = this.virtualSr.getWidth();
    double height = this.virtualSr.getHeight();
    Point virtualMin = this.virtualSr.getMin();
    Point virtualMax = this.virtualSr.getMax();
    double f = Math.pow(2, -level.getValue());
    double scaleX = 1.0;
    double scaleY = 1.0;
    double vX = pt.getX();
    double vY = pt.getY();
    double sX = (vX - virtualMin.getX() - 0.5 * width) / (scaleX * f) + scrnCenter.getX();
    double sY = (-vY - virtualMax.getY() + 0.5 * height) / (scaleY * f) + scrnCenter.getY();
    ScreenPoint result = new ScreenPoint(sX, sY);
    return result;
  }

  /**
   * Projects virtual envelope to a geometric envelope.
   *
   * @param virtualEnv
   * @param destSr
   * @return the geometric envelope.
   */
  public FxEnvelope projectVirtualToGeo(VirtualEnvelope virtualEnv, SpatialRef destSr) {
    FxPoint min = this.projectVirtualToGeo(virtualEnv.getMin(), destSr);
    FxPoint max = this.projectVirtualToGeo(virtualEnv.getMax(), destSr);
    return new FxEnvelope(min, max);
  }

  /**
   * Projects virtual coordinate to a geometric coordinate.
   *
   * @param virtualPt
   * @param destSr
   * @return the geometric point.
   */
  public FxPoint projectVirtualToGeo(Point virtualPt, SpatialRef destSr) {
    Point srcMax = destSr.getMax();
    Point srcMin = destSr.getMin();
    Point virtualMax = virtualSr.getMax();
    Point virtualMin = virtualSr.getMin();
    double srcDeltaX = srcMax.getX() - srcMin.getX();
    double srcDeltaY = srcMax.getY() - srcMin.getY();
    double virtualDeltaX = virtualMax.getX() - virtualMin.getX();
    double virtualDeltaY = virtualMax.getY() - virtualMin.getY();
    double x = srcMin.getX() + (virtualPt.getX() - virtualMin.getX()) * srcDeltaX / virtualDeltaX;
    double y = srcMin.getY() + (virtualPt.getY() - virtualMin.getY()) * srcDeltaY / virtualDeltaY;
    FxPoint result = new FxPoint(x, y, destSr);
    return result;
  }

  /**
   *
   * @param geomPoint
   * @return
   */
  public VirtualPoint projectGeoToVirtual(FxPoint geomPoint) {
    SpatialRef spatialRef = geomPoint.getSpatialRef();
    Point srcMax = spatialRef.getMax();
    Point srcMin = spatialRef.getMin();
    Point virtualMax = virtualSr.getMax();
    Point virtualMin = virtualSr.getMin();
    double srcDeltaX = srcMax.getX() - srcMin.getX();
    double srcDeltaY = srcMax.getY() - srcMin.getY();
    double virtualDeltaX = virtualMax.getX() - virtualMin.getX();
    double virtualDeltaY = virtualMax.getY() - virtualMin.getY();
    double vX = (geomPoint.getX() - srcMin.getX()) * virtualDeltaX / srcDeltaX + virtualMin.getX();
    double vY = (geomPoint.getY() - srcMin.getY()) * virtualDeltaY / srcDeltaY + virtualMin.getY();
    return new VirtualPoint(vX, vY);
  }

  /**
   *
   * @param geomEnv
   * @return
   */
  private VirtualEnvelope projectGeoToVirtual(FxEnvelope geomEnv) {
    VirtualPoint min = this.projectGeoToVirtual(geomEnv.getMinFxPoint());
    VirtualPoint max = this.projectGeoToVirtual(geomEnv.getMaxFxPoint());
    VirtualEnvelope result = new VirtualEnvelope(min, max);
    return result;
  }

  /**
   *
   * @param geomEnv
   * @param screenEnv
   * @return
   */
  public ScreenEnvelope projectGeoToScreen(FxEnvelope geomEnv, ScreenEnvelope screenEnv) {
    FxEnvelope geomEnvMerc = projectGeometry(geomEnv, this.baseSpatialRef);
    VirtualEnvelope virtualEnv = this.projectGeoToVirtual(geomEnvMerc);
    return this.projectVirtualToScreen(virtualEnv, screenEnv);
  }

  /**
   *
   * @param geomPoint
   * @param screenEnv
   * @return
   */
  public ScreenPoint projectGeoToScreen(FxPoint geomPoint, ScreenEnvelope screenEnv) {
    FxPoint geomPointMerc = this.projectGeometry(geomPoint, this.baseSpatialRef);
    VirtualPoint virtualPt = this.projectGeoToVirtual(geomPointMerc);
    ScreenPoint result = this.projectVirtualToScreen(virtualPt.asPoint(), screenEnv);
    return result;
  }

  /**
   *
   * @param geomPoint
   * @param baseSpatialRef
   * @return
   */
  private FxPoint projectGeometry(FxPoint geomPoint, SpatialRef baseSpatialRef) {
    FxPoint result;
    if (geomPoint.getSpatialRef().equals(baseSpatialRef)) {
      result = geomPoint;
    } else {
      result = this.geomProject.project(geomPoint, baseSpatialRef);
    }
    return result;
  }
  
  /**
   *
   * @param geomEnv
   * @param baseSpatialRef
   * @return
   */
  private FxEnvelope projectGeometry(FxEnvelope geomEnv, SpatialRef baseSpatialRef) {
    FxEnvelope result;
    if (geomEnv.getSr().equals(baseSpatialRef)) {
      result = geomEnv;
    } else {
      FxPoint newMin = this.geomProject.project(geomEnv.getMinFxPoint(), baseSpatialRef);
      FxPoint newMax = this.geomProject.project(geomEnv.getMaxFxPoint(), baseSpatialRef);
      result = new FxEnvelope(newMin, newMax);
    }
    return result;
  }
}
