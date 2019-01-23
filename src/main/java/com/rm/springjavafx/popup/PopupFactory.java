package com.rm.springjavafx.popup;

import com.rm.springjavafx.FxmlInitializer;
import javafx.scene.Node;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author rmarquez
 */
public class PopupFactory implements FactoryBean<Popup>, InitializingBean  {
  
  @Autowired
  private FxmlInitializer initializer;
  
  private String contentFxml;
        
  public PopupFactory() {
    
  }
  /**
   * 
   * @param initializer 
   */
  public void setFxmlInitializer(FxmlInitializer initializer) {
    this.initializer = initializer;
  }
  
  @Required
  public void setContentFxml(String contentFxml) {
    this.contentFxml = contentFxml;
  }
  
  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public Popup getObject() throws Exception {
    Node content = this.initializer.getRoot(this.contentFxml);
    Object controller = this.initializer.getController(contentFxml);
    Popup result = new Popup(content, (PopupContent) controller);
    return result;
  }
  
  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public Class<?> getObjectType() {
    return Popup.class;
  }
  
  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: nothing yet</p>
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    
  }
  
  
  
}
