package com.rm.panzoomcanvas.layers.image;

import com.rm.panzoomcanvas.core.FxEnvelope;
import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.core.ScreenEnvelope;
import com.rm.panzoomcanvas.core.SpatialRef;
import com.rm.panzoomcanvas.projections.Projector;

/**
 *
 * @author rmarquez
 */
public class ImageBounds {
  
  private final double upperLeftX;  
  private final double upperLeftY;  
  private final double width;  
  private final double height;
  private final SpatialRef spatialRef;
  
  /**
   * 
   * @param spatialRef
   * @param upperLeftX
   * @param upperLeftY
   * @param width
   * @param height 
   */
  public ImageBounds(SpatialRef spatialRef, 
          double upperLeftX, double upperLeftY, double width, double height) {  
    this.spatialRef = spatialRef;
    this.upperLeftX = upperLeftX;
    this.upperLeftY = upperLeftY;
    this.width = width;
    this.height = height;
  }
  
  /**
   * 
   * @return 
   */
  public double getUpperLeftX() {
    return upperLeftX;
  }
  
  /**
   * 
   * @return 
   */
  public double getUpperLeftY() {
    return upperLeftY;
  }
  
  /**
   * 
   * @return 
   */
  public double getWidth() {
    return width;
  }
  
  /**
   * 
   * @return 
   */
  public double getHeight() {
    return height;
  }
    
  /**
   * 
   * @param projector
   * @param screenEnv
   * @return 
   */
  ImageBounds toScreen(Projector projector, ScreenEnvelope screenEnv) {
    FxPoint min = new FxPoint(this.upperLeftX, this.upperLeftY - this.height, spatialRef);
    FxPoint max = new FxPoint(this.upperLeftX-this.width, this.upperLeftY, spatialRef);
    FxEnvelope geomEnv = new FxEnvelope(min, max);
    ScreenEnvelope projected = projector.projectGeoToScreen(geomEnv, screenEnv);
    double screenUpperLeftX = projected.getMin().getX();
    double screenUpperLeftY = projected.getMax().getY();
    double screenWidth = projected.getWidth();
    double screenHeight = projected.getHeight(); 
    ImageBounds result = new ImageBounds(this.spatialRef, screenUpperLeftX, screenUpperLeftY, screenWidth, screenHeight);
    return result;
  }
  
  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public String toString() {
    return "ImageBounds{" + "upperLeftX=" + upperLeftX + ", upperLeftY=" + upperLeftY + ", width=" + width + ", height=" + height + '}';
  }  
}
