package com.rm.springjavafx.components;

import com.rm.springjavafx.converters.Converter;
import com.rm.datasources.DataSource;
import com.rm.springjavafx.FxmlInitializer;
import javafx.beans.property.Property;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
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
  private ApplicationContext appContext;
  
  private String id;
  private String fxml;
  private String fxmlId;
  private String dataSourceRef;
  private String valueRef;
  private Converter listItemConverter = Converter.NONE;
  private Converter selectionItemConverter = Converter.NONE;

  public void setId(String id) {
    this.id = id;
  }

  public void setSelectionItemConverter(Converter selectionItemConverter) {
    this.selectionItemConverter = selectionItemConverter;
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
   * @param listItemConverter 
   */
  public void setListItemConverter(Converter listItemConverter) {
    this.listItemConverter = listItemConverter;
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
      if (!this.fxmlInitializer.isInitialized()) {
        this.fxmlInitializer.initializeRoots(this.appContext);
      }
      result = (ComboBox) this.fxmlInitializer.getNode(this.fxml, this.fxmlId); 
    } else {
      result = new ComboBox(); 
    }
    Property valueRefProperty = (Property) this.appContext.getBean(this.valueRef); 
    result.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, change)->{
      Object newVal = this.selectionItemConverter.convert(change);
      valueRefProperty.setValue(newVal);
    });
    valueRefProperty.addListener((obs, oldVal, change)->{
      Object newVal = this.selectionItemConverter.deconvert(change);
      result.getSelectionModel().select(newVal);
    });
    Object selected = this.selectionItemConverter.deconvert(valueRefProperty.getValue());
    result.getSelectionModel().select(selected);
    
    DataSource dataSrc = (DataSource) this.appContext.getBean(this.dataSourceRef);
    dataSrc.bind(result.getItems(), Converter.NONE);
    result.setConverter(new StringConverter() {
      @Override
      public String toString(Object object) {
        return String.valueOf(listItemConverter.convert(object)); 
      }
      @Override
      public Object fromString(String string) {
        return null;
      }
    });
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
