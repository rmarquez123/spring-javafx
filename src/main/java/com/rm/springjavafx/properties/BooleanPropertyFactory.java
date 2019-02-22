
package com.rm.springjavafx.properties;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.springframework.beans.factory.FactoryBean;

/**
 *
 * @author rmarquez
 */
public class BooleanPropertyFactory implements FactoryBean<BooleanProperty> {
  
  /**
   * 
   * @return
   * @throws Exception 
   */
  @Override
  public BooleanProperty getObject() throws Exception {
    return new SimpleBooleanProperty(); 
  }

  @Override
  public Class<?> getObjectType() {
    return BooleanProperty.class;
  }
  
}
