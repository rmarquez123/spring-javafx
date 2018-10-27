package com.rm.panzoomcanvas.layers.image;

import com.rm.panzoomcanvas.LayerGeometry;
import javafx.scene.image.Image;

/**
 *
 * @author rmarquez
 */
public interface ImageSource extends LayerGeometry {

  /**
   *
   * @return
   */
  public ImageBounds getBounds();

  /**
   *
   * @return
   */
  public Image getImage();
}
