package com.rm.springjavafx.link;

import com.rm.springjavafx.AnnotationHandler;
import com.rm.springjavafx.FxmlInitializer;
import common.bindings.RmBindings;
import java.util.Map;
import javafx.beans.property.Property;
import javafx.collections.ObservableSet;
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
public class BeanSetItemLinkProcessor implements InitializingBean, AnnotationHandler {

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
    Map<String, Object> beans = this.appContext.getBeansWithAnnotation(BeanSetItemLink.class);
    beans.entrySet().forEach((Map.Entry<String, Object> e) -> {
      String key = e.getKey();
      BeanSetItemLink conf = this.appContext.findAnnotationOnBean(key, BeanSetItemLink.class);
      if (conf == null) {
        throw new RuntimeException();
      }
      Object refBean = this.appContext.getBean(conf.setbean());
      Property<Object> value = (Property<Object>) e.getValue();
      if (refBean instanceof ObservableSet) {
        ObservableSet refBeanProperty = (ObservableSet) refBean;
        RmBindings.bindActionOnAnyChange(() -> {
          this.setValue(refBeanProperty, value);
        }, refBeanProperty);
        this.setValue(refBeanProperty, value);
      }
    });
  }
  
  private void setValue(ObservableSet refBeanProperty, Property<Object> value) {
    if (!refBeanProperty.isEmpty()) {
      if (value.getValue() == null || !refBeanProperty.contains(value.getValue())) {
        value.setValue(refBeanProperty.iterator().next());
      }
    } else {
      value.setValue(null);
    }
  }
}
