package com.rm.panzoomcanvas.layers.image;

import javafx.scene.image.Image;

/**
 *
 * @author rmarquez
 */
public interface ImageSource {

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
