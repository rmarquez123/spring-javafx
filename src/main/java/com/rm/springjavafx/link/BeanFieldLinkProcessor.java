package com.rm.springjavafx.link;

import com.rm.springjavafx.AnnotationHandler;
import com.rm.springjavafx.FxmlInitializer;
import common.bindings.RmBindings;
import java.lang.reflect.Field;
import java.util.Map;
import javafx.beans.property.Property;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Lazy(false)
public class BeanFieldLinkProcessor implements InitializingBean, AnnotationHandler {

  @Autowired
  private FxmlInitializer fxmlInitializer;

  @Autowired
  private ApplicationContext appContext;

  @Override
  public void afterPropertiesSet() throws Exception {
    this.fxmlInitializer.addAnnotationHandler(this);
  }

  @Override
  public void readyFxmls() {
  }

  @Override
  public void setNodes() {
    Map<String, Object> beans = this.appContext.getBeansWithAnnotation(BeanFieldLink.class);
    beans.entrySet().forEach((Map.Entry<String, Object> e) -> {
      String key = e.getKey();
      BeanFieldLink conf = this.appContext.findAnnotationOnBean(key, BeanFieldLink.class);
      if (conf == null) {
        throw new RuntimeException();
      }
      Object refBean = this.appContext.getBean(conf.bean());
      Property<Object> value = (Property<Object>) e.getValue();
      if (refBean instanceof Property) {
        Property refBeanProperty = (Property) refBean;
        RmBindings.bindActionOnAnyChange(() -> {
          this.updateValue(refBeanProperty, conf, value);
        }, refBeanProperty);
        this.updateValue(refBeanProperty, conf, value);
      }
    });
  }

  /**
   *
   * @param refBeanProperty
   * @param conf
   * @param value
   */
  private void updateValue(Property refBeanProperty, BeanFieldLink conf, Property<Object> value) {
    if (refBeanProperty.getValue() != null) {
      String[] fields = conf.field();
      Object newvalue = refBeanProperty.getValue();
      for (String fieldname : fields) {
        try {
          Field field = newvalue.getClass().getDeclaredField(fieldname);
          field.setAccessible(true);
          newvalue = field.get(newvalue);
        } catch (Exception ex) {
          throw new RuntimeException(ex);
        }
      }
      value.setValue(newvalue);
    }
  }
}
