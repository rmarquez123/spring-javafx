package com.rm.panzoomcanvas.layers;

import com.rm.panzoomcanvas.Content;
import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.Layer;
import com.rm.panzoomcanvas.core.ScreenEnvelope;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.Parent;

/**
 *
 * @author rmarquez
 */
public abstract class BaseLayer implements Layer {

  private final String name;
  private final BooleanProperty visible = new SimpleBooleanProperty(true);
  private Node layerCanvas = null;
  private FxCanvas canvas;
  private final long uuid = (long) (Math.random() * 100000);

  /**
   *
   * @param name
   */
  public BaseLayer(String name) {
    this.name = name;
  }

  @Override
  public final String getName() {
    return this.name;
  }

  @Override
  public BooleanProperty getVisible() {
    return this.visible;
  }

  @Override
  public long getUuid() {
    return uuid;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public Content getContent() {
    return canvas == null ? null : this.canvas.getContent();
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public final void redraw(FxCanvas canvas) {
    if (canvas != null) {

      this.clearCanvas(canvas);
      Parent p = canvas.getParent();
      if (p != null) {
        ScreenEnvelope layerScreenEnv = this.onGetScreenEnvelope(canvas);
        if (layerScreenEnv != null) {
          double areaX = layerScreenEnv.getMin().getX();
          double areaY = layerScreenEnv.getMin().getY();
          double areaWidth = layerScreenEnv.getWidth();
          double areaHeight = layerScreenEnv.getHeight();
          double width = canvas.getWidth();
          double height = canvas.getHeight();
          this.layerCanvas = this.createLayerCanvas(width, height);
          canvas.getContent().addLayerCanvas(this.uuid, this.layerCanvas);
          this.onDraw(new DrawArgs(canvas, this.layerCanvas, areaX, areaY, areaHeight, areaWidth));
        }
      }
      this.canvas = canvas;
    }
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public final void purge(FxCanvas virtualCanvas) {
    this.clearCanvas(virtualCanvas);
  }

  /**
   *
   * @return
   */
  protected final Node getLayerCanvas() {
    return layerCanvas;
  }

  /**
   *
   */
  protected final void repaint() {
    if (this.canvas != null) {
      this.redraw(canvas);
    }
  }

  /**
   *
   * @param canvas
   * @return
   */
  protected abstract ScreenEnvelope onGetScreenEnvelope(FxCanvas canvas);

  /**
   *
   * @param args
   */
  protected abstract void onDraw(DrawArgs args);

  /**
   *
   * @param width
   * @param height
   * @return
   */
  protected abstract Node createLayerCanvas(double width, double height);

  /**
   *
   */
  private void clearCanvas(FxCanvas contentCanvas) {
    if (contentCanvas != null && this.layerCanvas != null) {
      contentCanvas.getContent().removeLayerCanvas(this.uuid, this.layerCanvas);
      this.layerCanvas = null;
    }
  }
}
