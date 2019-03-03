
package com.rm.springjavafx.table.renderers;

import com.rm.springjavafx.table.RenderType;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author Ricardo Marquez
 */
public class RenderTypeFactory implements FactoryBean<RenderType> {
  
  private String clazz; 
  private Action action;
  private String label;

  @Required
  public void setClazz(String clazz) {
    this.clazz = clazz;
  }
  
  @Required
  public void setAction(Action action) {
    this.action = action;
  }
  
  @Required
  public void setLabel(String label) {
    this.label = label;
  }
  
  
  /**
   * 
   * @return
   * @throws Exception 
   */
  @Override
  public RenderType getObject() throws Exception {
    RenderType result;
    switch (this.clazz) {
      case "button":
        result = new ButtonTypeRenderer(this.action, this.label);
        break;
      case "togglebutton":
        result = new ToggleButtonTypeRenderer(this.action, this.label);
        break;
      default:
        throw new RuntimeException("Invalid render type class : '" + this.clazz + "'");
    }
    return result;
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public Class<?> getObjectType() {
    return RenderType.class;
  }
  
}
