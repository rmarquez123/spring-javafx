package com.rm.panzoomcanvas;

import javafx.beans.property.BooleanProperty;

/**
 *
 * @author rmarquez
 */
public interface Layer {
  
  public void redraw(FxCanvas canvas);

  public void purge(FxCanvas canvas);

  public String getName();

  public BooleanProperty getVisible();

  public long getUuid();

  public Content getContent();

}
