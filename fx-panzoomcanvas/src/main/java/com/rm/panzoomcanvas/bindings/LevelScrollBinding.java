package com.rm.panzoomcanvas.bindings;

import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.projections.Level;
import javafx.beans.property.Property;
import javafx.scene.input.ScrollEvent;

/**
 *
 * @author rmarquez
 */
public class LevelScrollBinding {

  /**
   *
   * @param mapCanvas
   */
  public LevelScrollBinding(FxCanvas mapCanvas) {
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
