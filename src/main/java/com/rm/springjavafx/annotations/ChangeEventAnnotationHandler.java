package com.rm.springjavafx.annotations;

import com.rm.springjavafx.AnnotationHandler;
import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.events.ChangeEventListener;
import com.rm.springjavafx.events.OnChangeEvent;
import java.lang.reflect.Method;
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
public class ChangeEventAnnotationHandler implements AnnotationHandler, InitializingBean{
  @Autowired
  private FxmlInitializer fxmlInitializer;
  
  @Autowired
  private ApplicationContext appContext;
  
  @Override
  public void afterPropertiesSet() throws Exception {
    this.fxmlInitializer.addAnnotationHandler(this);
  }
  
  /**
   * 
   */
  @Override
  public void readyFxmls() {
  }
  
  /**
   * 
   */
  @Override
  public void setNodes() {
    Map<String, Object> beans = this.appContext.getBeansWithAnnotation(ChangeEventListener.class);
    for (Map.Entry<String, Object> entry : beans.entrySet()) {
      Object bean = entry.getValue(); 
      Method[] methods = bean.getClass().getDeclaredMethods();
      for (Method method : methods) {
        OnChangeEvent onChangeEventConf;
        if ((onChangeEventConf = method.getAnnotation(OnChangeEvent.class)) !=null ) {
          String propertyBeanId = onChangeEventConf.bean();
          Property<? extends Object> propertyBean = (Property<? extends Object>) this.appContext.getBean(propertyBeanId);
          propertyBean.addListener((obs, old, change)->{
            try {
              method.invoke(bean, obs, old, change);
            } catch (Exception ex) {
              throw new RuntimeException(ex); 
            }
          });
          try {
            method.invoke(bean, propertyBean, null, propertyBean.getValue());
          } catch (Exception ex) {
            throw new RuntimeException(ex); 
          }
        }
      }
        
    }
  }
  
}
