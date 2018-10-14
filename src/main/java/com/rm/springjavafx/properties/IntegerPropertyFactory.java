package com.rm.springjavafx.properties;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.springframework.beans.factory.FactoryBean;

/**
 *
 * @author rmarquez
 */
public class IntegerPropertyFactory implements FactoryBean<IntegerProperty> {
  
  
  /**
   * 
   * @return
   * @throws Exception 
   */
  @Override
  public IntegerProperty getObject() throws Exception {
    return new SimpleIntegerProperty();
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public Class<?> getObjectType() {
    return SimpleIntegerProperty.class;
  }
}
