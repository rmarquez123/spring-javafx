package com.rm.wizard;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 *
 * @author Ricardo Marquez
 */
public abstract class AbstractSettingPanel {
  
  /**
   * 
   */
  private final BooleanProperty booleanProperty = new SimpleBooleanProperty(true);
  
  /**
   * 
   * @return 
   */
  public final BooleanProperty nextReadyProperty(){
    return this.booleanProperty;
  };
  
  

  /**
   * 
   */
  public abstract void submit();
}
