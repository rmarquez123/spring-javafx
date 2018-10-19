package com.rm.springjavafx.components;

import com.rm.springjavafx.SpringFxUtils;
import com.rm.testrmfxmap.javafx.FxmlInitializer;
import java.time.LocalDate;
import java.util.Date;
import javafx.beans.property.Property;
import javafx.scene.control.DatePicker;
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
public class DatePickerFactory implements FactoryBean<DatePicker>, InitializingBean, ApplicationContextAware {

  @Autowired
  private FxmlInitializer fxmlInitializer;
  private ApplicationContext appContext;
  private String id;
  private String fxml;
  private String fxmlId;
  private String valueRef;

  /**
   *
   * @param valueRef
   */
  public void setValueRef(String valueRef) {
    this.valueRef = valueRef;
  }

  /**
   *
   * @return @throws Exception
   */
  @Override
  public DatePicker getObject() throws Exception {
    DatePicker result;
    if (this.fxml != null) {
      if (!this.fxmlInitializer.isInitialized()) {
        this.fxmlInitializer.initializeRoots(this.appContext);
      }
      result = (DatePicker) this.fxmlInitializer.getNode(this.fxml, this.fxmlId);
    } else {
      result = new DatePicker();
    }
    Property<Date> valueRefProperty = (Property<Date>) SpringFxUtils
            .getValueProperty(appContext, this.valueRef); 
    result.valueProperty().addListener((obs, oldVal, change)->{
      Date newVal = new Date(change.getYear(), change.getMonthValue(), change.getDayOfMonth()); 
      valueRefProperty.setValue(newVal);
    });
    valueRefProperty.addListener((obs, oldVal, change)->{
      LocalDate localDate = LocalDate.of(change.getYear(), change.getMonth(), change.getDay());
      result.valueProperty().setValue(localDate);
    });
    
    return result;
  }

  /**
   *
   * @return
   */
  @Override
  public Class<?> getObjectType() {
    return DatePicker.class;
  }

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

  /**
   *
   * @param ac
   * @throws BeansException
   */
  @Override
  public void setApplicationContext(ApplicationContext ac) throws BeansException {
    this.appContext = ac;
  }

}
