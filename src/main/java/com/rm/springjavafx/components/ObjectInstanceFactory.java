package com.rm.springjavafx.components;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author rmarquez
 */
public class ObjectInstanceFactory implements FactoryBean<Object>, InitializingBean, ApplicationContextAware {
  
  private String className;
  private List<String> constructorArgs = new ArrayList<>();
  private HashMap<String, Object> properties = new HashMap<>();
  private ApplicationContext appContext;
  
  public ObjectInstanceFactory() {
  }
  
  
  public void setClassName(String className) {
    this.className = className;
  }
  
  public void setConstructorArgs(List<String> constructorArgs) {
    this.constructorArgs = constructorArgs;
  }

  public void setProperties(HashMap<String, Object> properties) {
    this.properties = properties;
  }
  
  /**
   * 
   * @return
   * @throws Exception 
   */
  @Override
  public Object getObject() throws Exception {
    Class<?> clazz = Class.forName(this.className);
    Constructor<?> constructor = clazz.getConstructor();
    constructor.setAccessible(true);
    Object result = constructor.newInstance(this.constructorArgs.toArray(new Object[]{}));
    for (Map.Entry<String, Object> entry : this.properties.entrySet()) {
      Field field = clazz.getDeclaredField(entry.getKey()); 
      field.setAccessible(true);
      field.set(result, entry.getValue());
    }
    if (InitializingBean.class.isAssignableFrom(clazz)) {
      ((InitializingBean) result).afterPropertiesSet();
    }
    return result;
  }

  @Override
  public Class<?> getObjectType() {
    return Object.class;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    if (this.className == null || this.className.isEmpty()) {
      throw new IllegalStateException("Class is not defined"); 
    }
  }
  
  @Override
  public void setApplicationContext(ApplicationContext ac) throws BeansException {
    this.appContext = ac;
  }
  
}
