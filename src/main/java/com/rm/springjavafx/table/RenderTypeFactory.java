
package com.rm.springjavafx.table;

import com.rm.springjavafx.table.renderers.Action;
import com.rm.springjavafx.table.renderers.ButtonTypeRenderer;
import com.rm.springjavafx.table.renderers.CheckBoxTypeRenderer;
import com.rm.springjavafx.table.renderers.ToggleButtonTypeRenderer;
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
  private String secondaryLabel;

  @Required
  public void setClazz(String clazz) {
    this.clazz = clazz;
  }
  
  public void setAction(Action action) {
    this.action = action;
  }
  
  @Required
  public void setLabel(String label) {
    this.label = label;
  }
  
  public void setSecondaryLabel(String secondaryLabel) {
    this.secondaryLabel = secondaryLabel;
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
        result = new ToggleButtonTypeRenderer(this.action, this.label, this.secondaryLabel);
        break;
      case "checkbox": 
        result = new CheckBoxTypeRenderer(this.label);
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
