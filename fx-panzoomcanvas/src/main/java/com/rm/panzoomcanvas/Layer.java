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
   * @return
   */
  public BooleanProperty visibleProperty();

  /**
   *
   * @return
   */
  public BooleanProperty selectableProperty();

  /**
   *
   * @return
   */
  public BooleanProperty hoverableProperty();

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
   * @param canvas
   */
  void redraw(FxCanvas canvas);

  /**
   *
   * @param canvas
   */
  void purge(FxCanvas canvas);
  
  /**
   *
   * @return
   */
  long getUuid();


  /**
   *
   * @param e
   */
  void onMouseClicked(LayerMouseEvent e);

  /**
   *
   * @param layerMouseEvent
   */
  void onMouseHovered(LayerMouseEvent layerMouseEvent);

}
