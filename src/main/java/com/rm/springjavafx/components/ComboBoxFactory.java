package com.rm.springjavafx.components;

import com.rm.testrmfxmap.javafx.FxmlInitializer;
import javafx.scene.control.ComboBox;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 *
 * @author rmarquez
 */

public class ComboBoxFactory implements FactoryBean<ComboBox>, InitializingBean, ApplicationContextAware {
  @Autowired
  FxmlInitializer fxmlInitializer;
  private String id;
  private String fxml;
  private String fxmlId;
  private String dataSourceRef;
  private String valueRef;
  private ApplicationContext appContext;

  public void setId(String id) {
    this.id = id;
  }

  
  public void setFxml(String fxml) {
    this.fxml = fxml;
  }

  public void setFxmlId(String fxmlId) {
    this.fxmlId = fxmlId;
  }
  
  /**
   * 
   * @param dataSourceRef 
   */
  public void setDataSourceRef(String dataSourceRef) {
    this.dataSourceRef = dataSourceRef;
  }
  
  /**
   * 
   * @param valueRef 
   */
  public void setValueRef(String valueRef) {
    this.valueRef = valueRef;
  }

  /**
   * 
   * @return
   * @throws Exception 
   */
  @Override
  public ComboBox getObject() throws Exception {
    ComboBox result; 
    if (this.fxml != null) {
      result = new ComboBox();
//      result = (ComboBox) this.fxmlInitializer.getNode(this.fxml, this.fxmlId); 
    } else {
      result = new ComboBox(); 
    }
    return result;
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public Class<?> getObjectType() {
    return ComboBox.class;
  }
  
  /**
   * 
   * @throws Exception 
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    if (this.fxml != null && this.fxmlId == null) {
      throw new IllegalStateException("fxmlId cannot be null if fxml is not null"); 
    }
    if (this.fxml != null && this.fxmlInitializer == null) {
      throw new IllegalStateException("fxml initializer cannot be null if fxml is not null"); 
    }
    this.appContext.getBean(this.id); 
  }

  @Override
  public void setApplicationContext(ApplicationContext ac) throws BeansException {
    this.appContext = ac;
  }
  
  
}
