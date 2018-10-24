package fx.panzoomcanvas.bindings;

import fx.panzoomcanvas.FxCanvas;
import fx.panzoomcanvas.projections.ScreenPoint;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Cursor;

/**
 *
 * @author rmarquez
 */
public class PanningBinding {

  /**
   *
   * @param mapCanvas
   */
  public PanningBinding(FxCanvas mapCanvas) {
    mapCanvas.parentProperty().addListener((a, old, newParent) -> {
      if (newParent != null) {
        Property<ScreenPoint> center = mapCanvas.centerProperty();
        Property<ScreenPoint> lastMouse = new SimpleObjectProperty<>();
        lastMouse.addListener((obs, oldVal, newVal) -> {
          if (newVal != null && oldVal != null) {
            double deltaX = oldVal.getX() - newVal.getX();
            double deltaY = oldVal.getY() - newVal.getY();
            double newCenterX = center.getValue().getX() - deltaX;
            double newCenterY = center.getValue().getY() - deltaY;
            ScreenPoint newCenter = new ScreenPoint(newCenterX, newCenterY);
            center.setValue(newCenter);
          }
        });
        newParent.setOnDragDetected((event) -> {
          mapCanvas.getParent().setCursor(Cursor.MOVE);
          lastMouse.setValue(null);
        });
        newParent.setOnMouseReleased((event) -> {
          mapCanvas.getParent().setCursor(Cursor.DEFAULT);
          lastMouse.setValue(null);
        });
        newParent.setOnMouseDragged((event) -> {
          ScreenPoint last = new ScreenPoint(event.getX(), event.getY());
          lastMouse.setValue(last);
        });
      }

    });
  }
}
