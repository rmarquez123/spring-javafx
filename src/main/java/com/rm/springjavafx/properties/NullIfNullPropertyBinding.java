package com.rm.springjavafx.properties;

import javafx.beans.property.ObjectProperty;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author Ricardo Marquez
 */
public class NullIfNullPropertyBinding implements InitializingBean{
  
  private ObjectProperty<?> referenceProperty;
  
  
  private ObjectProperty<?> property;
  
  /**
   * 
   * @param referenceProperty 
   */
  @Required
  public void setReferenceProperty(ObjectProperty<?> referenceProperty) {
    this.referenceProperty = referenceProperty;
  }
  
  /**
   * 
   * @param property 
   */
  @Required
  public void setProperty(ObjectProperty<?> property) {
    this.property = property;
  }
  
    
  /**
   * 
   * @throws Exception 
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    this.referenceProperty.addListener((obs, old, change)->{
      if (change == null) {
        this.property.setValue(null);
      }
    });
  }
}
