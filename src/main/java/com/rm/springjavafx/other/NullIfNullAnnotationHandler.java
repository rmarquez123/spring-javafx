package com.rm.springjavafx.other;

import com.rm.springjavafx.AnnotationHandler;
import com.rm.springjavafx.FxmlInitializer;
import java.lang.reflect.Method;
import java.util.Map;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Lazy(false)
public class NullIfNullAnnotationHandler implements InitializingBean, AnnotationHandler {

  @Autowired
  private FxmlInitializer fxmlInitializer;
  @Autowired
  private ApplicationContext appContext;

  /**
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    this.fxmlInitializer.addAnnotationHandler(this);
  }

  /**
   *
   */
  @Override
  public void readyFxmls() {
    Map<String, Object> configurations = appContext.getBeansWithAnnotation(Configuration.class);
    for (Object value : configurations.values()) {
      Method[] methods = ClassUtils.getUserClass(value.getClass()).getMethods();
      for (Method method : methods) {
        NullIfNull nullIfNull = method.getDeclaredAnnotation(NullIfNull.class);
        if (nullIfNull != null) {
          Bean b = method.getAnnotation(Bean.class);
          b.name();
          if (b.name().length == 0 && b.value().length == 0) {
            throw new RuntimeException("method is not a bean definition : '" + method.getName() + "'"); 
          }
          Object refBean = appContext.getBean(nullIfNull.refBean());
          
          String name = b.name().length != 0 ? b.name()[0]: b.value()[0];
          Object bean = appContext.getBean(name);
          if (refBean instanceof Property && bean instanceof Property) {
            ((Property) refBean).addListener((obs, old, change) -> {
              if (change == null) {
                if (bean instanceof ObjectProperty
                  && !((ObjectProperty) bean).isBound()) {
                  ((Property) bean).setValue(null);
                }
              }
            });
          }
        }
      }
    }
  }

  /**
   *
   */
  @Override
  public void setNodes() {
  }

}
