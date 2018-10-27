package com.rm.panzoomcanvas;

import javafx.beans.property.BooleanProperty;

/**
 *
 * @author rmarquez
 */
public interface Layer {

  /**
   *
   * @return
   */
  public String getName();

  /**
   *
   * @param canvas
   */
  public void redraw(FxCanvas canvas);

  /**
   *
   * @param canvas
   */
  public void purge(FxCanvas canvas);

  /**
   *
   * @return
   */
  public BooleanProperty getVisible();

  /**
   *
   * @return
   */
  public long getUuid();

  /**
   *
   * @return
   */
  public Content getContent();

  /**
   * 
   * @return 
   */
  public LayerGeometry getLayerGeometry(); 

  /**
   * 
   * @param e 
   */
  public void onMouseClicked(LayerMouseEvent e);

}
