package com.rm.panzoomcanvas;

import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.core.Level;
import javafx.beans.property.Property;
import javafx.scene.input.ScrollEvent;

/**
 *
 * @author rmarquez
 */
public class ScrollBinding {

  /**
   *
   * @param mapCanvas
   */
  public ScrollBinding(FxCanvas mapCanvas) {
    Property<Level> level = mapCanvas.levelProperty();
    mapCanvas.parentProperty().addListener((obs, old, newp) -> {
      if (newp != null) {
        newp.setOnScroll((ScrollEvent event) -> {
          Level newVal;
          if (event.getDeltaY() > 0) {
            newVal = level.getValue().addOne(event);
          } else {
            newVal = level.getValue().subtractOne(event);
          }
          level.setValue(newVal);
        });
      }
    });
  }
}
