package com.rm.panzoomcanvas.components;

import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.projections.Projector;
import com.rm.panzoomcanvas.core.ScreenPoint;
import com.rm.panzoomcanvas.core.VirtualPoint;
import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.core.Point;
import com.rm.panzoomcanvas.core.SpatialRef;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;

/**
 *
 * @author rmarquez
 */
public class PositionBar extends Label {

  private final FxCanvas mapCanvas;
  private final Property<VirtualPoint> mousePosition
          = new SimpleObjectProperty<>(new VirtualPoint(Double.NaN, Double.NaN));

  /**
   *
   * @param mapCanvas
   */
  public PositionBar(FxCanvas mapCanvas) {
    this.mapCanvas = mapCanvas;
    this.mapCanvas.widthProperty().addListener((a, b, c) -> redraw());
    this.mapCanvas.heightProperty().addListener((a, b, c) -> redraw());
    this.mapCanvas.levelProperty().addListener((a, b, c) -> redraw());
    this.mapCanvas.parentProperty().addListener((e) -> {
      this.mapCanvas.getParent().setOnMouseMoved((e2) -> {
        this.mousePosition.setValue(new VirtualPoint(e2.getX(), e2.getY()));
        this.redraw();
      });
    });
  }

  /**
   *
   */
  private void redraw() {
    ScreenPoint mouseScrnPt = new ScreenPoint(this.mousePosition.getValue().getX(), this.mousePosition.getValue().getY());
    Projector projector = this.mapCanvas.getProjector();
    VirtualPoint mouseVrtPt = projector.projectScreenToVirtual(mouseScrnPt, this.mapCanvas.screenEnvelopeProperty().getValue());
    FxPoint mouseGeo = projector.projectVirtualToGeo(mouseVrtPt.asPoint(), new SpatialRef(4326, new Point(-179.999999, -89.999999), new Point(179.999999, 89.999999)) {
    });
    String text = String.format("Sreeen w: %d h: %d, Level : %d, Virtual w: %f  h %f: Cursor x: %d y: %d, Cursor x: %f y: %f",
            (int) this.mapCanvas.getWidth(), (int) this.mapCanvas.getHeight(),
            this.mapCanvas.levelProperty().getValue().getValue(),
            this.mapCanvas.virtualEnvelopeProperty().getValue().getWidth(),
            this.mapCanvas.virtualEnvelopeProperty().getValue().getHeight(),
            //            (int) this.mousePosition.getValue().getX(), (int) this.mousePosition.getValue().getY(), 
            (int) mouseVrtPt.getX(), (int) mouseVrtPt.getY(),
            mouseGeo.getX(), mouseGeo.getY()
    );
    this.textProperty().setValue(text);
  }
}
