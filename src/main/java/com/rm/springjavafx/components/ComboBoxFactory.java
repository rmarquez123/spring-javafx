package com.rm.springjavafx.components;

import javafx.scene.control.ComboBox;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 *
 * @author rmarquez
 */
public class ComboBoxFactory implements FactoryBean<ComboBox>, InitializingBean {
  private String fxml;
  private String fxmlId;
  private String dataSourceRef;
  private String valueRef;

  public void setFxml(String fxml) {
    this.fxml = fxml;
  }

  public void setFxmlId(String fxmlId) {
    this.fxmlId = fxmlId;
  }

  public void setDataSourceRef(String dataSourceRef) {
    this.dataSourceRef = dataSourceRef;
  }

  public void setValueRef(String valueRef) {
    this.valueRef = valueRef;
  }

  
  @Override
  public ComboBox getObject() throws Exception {
    return new ComboBox();
  }

  @Override
  public Class<?> getObjectType() {
    return ComboBox.class;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    
  }
  
  
  
}
