package com.rm.springjavafx.properties;

import org.springframework.beans.factory.FactoryBean;

/**
 *
 * @author rmarquez
 */
public class DateRangePropertyFactory implements FactoryBean<DateRangeProperty> {
  
  /**
   * 
   * @return
   * @throws Exception 
   */
  @Override
  public DateRangeProperty getObject() throws Exception {
    DateRangeProperty result = new DateRangeProperty();
    return result;
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public Class<?> getObjectType() {
    return DateRangeProperty.class;
  }
  
}
