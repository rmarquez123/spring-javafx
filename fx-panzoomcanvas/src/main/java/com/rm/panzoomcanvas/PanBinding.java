package com.rm.panzoomcanvas;

import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.core.ScreenPoint;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author rmarquez
 */
public class PanBinding {

  /**
   *
   * @param mapCanvas
   */
  public PanBinding(FxCanvas mapCanvas) {
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
        
        newParent.addEventHandler(MouseEvent.DRAG_DETECTED, (event) -> {
          mapCanvas.getParent().setCursor(Cursor.MOVE);
          lastMouse.setValue(null);
        });
        
        newParent.addEventHandler(MouseEvent.MOUSE_RELEASED, (event) -> {
          mapCanvas.getParent().setCursor(Cursor.DEFAULT);
          lastMouse.setValue(null);
        });
        newParent.addEventHandler(MouseEvent.MOUSE_DRAGGED, (event) -> {
          ScreenPoint last = new ScreenPoint(event.getX(), event.getY());
          lastMouse.setValue(last);
        });
      }

    });
  }
}
