package com.rm.panzoomcanvas.layers;

import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.projections.Level;
import com.rm.panzoomcanvas.projections.ScreenEnvelope;
import com.rm.panzoomcanvas.projections.ScreenPoint;
import javafx.scene.Node;

/**
 *
 * @author rmarquez
 */
public class DrawArgs {

  private final double areaX;
  private final double areaY;
  private final double areaHeight;
  private final double areaWidth;
  private final FxCanvas canvas;
  private final Node layerCanvas;
  private final ScreenEnvelope screenEnv;
  private final ScreenPoint center;
  private final Level level;
  
  /**
   * 
   * @param canvas
   * @param layerCanvas
   * @param areaX
   * @param areaY
   * @param areaHeight
   * @param areaWidth 
   */
  public DrawArgs(FxCanvas canvas, Node layerCanvas, double areaX, 
          double areaY, double areaHeight, double areaWidth) {
    this.areaX = areaX;
    this.areaY = areaY;
    this.areaHeight = areaHeight;
    this.areaWidth = areaWidth;
    this.canvas = canvas;
    this.layerCanvas = layerCanvas;
    this.screenEnv = this.canvas.screenEnvelopeProperty().getValue(); 
    this.center = this.canvas.centerProperty().getValue(); 
    this.level = this.canvas.levelProperty().getValue();
  }

  /**
   *
   * @return
   */
  public double getAreaX() {
    return areaX;
  }

  /**
   *
   * @return
   */
  public double getAreaY() {
    return areaY;
  }

  /**
   *
   * @return
   */
  public double getAreaHeight() {
    return areaHeight;
  }

  /**
   *
   * @return
   */
  public double getAreaWidth() {
    return areaWidth;
  }

  /**
   *
   * @return
   */
  public FxCanvas getCanvas() {
    return canvas;
  }

  /**
   *
   * @return
   */
  public Node getLayerCanvas() {
    return layerCanvas;
  }

  public ScreenEnvelope getScreenEnv() {
    return screenEnv;
  }

  public ScreenPoint getCenter() {
    return center;
  }

  public Level getLevel() {
    return level;
  }
  
  
  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public String toString() {
    return "{" + "areaX=" + areaX
            + ", areaY=" + areaY
            + ", areaHeight=" + areaHeight
            + ", areaWidth=" + areaWidth
            + ", canvas=" + canvas + '}';
  }

}
