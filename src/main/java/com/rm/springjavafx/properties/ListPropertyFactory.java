package com.rm.springjavafx.properties;

import java.util.List;
import org.springframework.beans.factory.FactoryBean;

/**
 *
 * @author rmarquez
 */
public class ListPropertyFactory implements FactoryBean<ElementSelectableListProperty<?>>{
  private List<?> items;
  
  /**
   * 
   * @param items 
   */
  public void setItems(List<?> items) {
    this.items = items;
  }

  /**
   * 
   * @return
   * @throws Exception 
   */
  @Override
  public ElementSelectableListProperty<?> getObject() throws Exception {
    ElementSelectableListProperty result = new ElementSelectableListProperty(); 
    result.setListItems(this.items);
    return result;
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public Class<?> getObjectType() {
    return ElementSelectableListProperty.class;
  }
  
  
  
}
