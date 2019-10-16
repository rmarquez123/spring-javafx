package com.rm.springjavafx.annotations;

import com.rm.springjavafx.AnnotationHandler;
import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.linkeditems.LinkedItem;
import com.rm.springjavafx.linkeditems.LinkedItemsHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.BeansException;
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
public class LinkedItemAnnotationHandler implements InitializingBean, AnnotationHandler {

  @Autowired
  private FxmlInitializer fxmlInitializer;
  @Autowired
  private ApplicationContext appContext;

  private final LinkedItemsHandler handler = new LinkedItemsHandler();

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

  }

  /**
   *
   */
  @Override
  public void setNodes() {
    Map<String, LinkedItemBean> beans = getLinkedItemBeans();
    beans.entrySet().stream().forEach(this::linkBean);
  }

  /**
   *
   * @return @throws BeansException
   */
  private Map<String, LinkedItemBean> getLinkedItemBeans() {
    Map<String, LinkedItemBean> result = this.appContext.getBeansWithAnnotation(LinkedItem.class).entrySet()
      .stream()
      .collect(Collectors.toMap((e)->e.getKey(), 
        (e)->new LinkedItemBean(e.getValue(), 
          e.getValue().getClass().getDeclaredAnnotation(LinkedItem.class))))
      ;
    
    Map<String, Object> configurations = appContext.getBeansWithAnnotation(Configuration.class);
    for (Object configuration : configurations.values()) {
      extractLinkedItemsFromConfiguration(configuration, result);
    }
    return result;
  }
  
  
  
  /**
   * 
   * @param configuration
   * @param result 
   */
  private void extractLinkedItemsFromConfiguration(Object configuration, Map<String, LinkedItemBean> result) {
    Method[] methods = ClassUtils.getUserClass(configuration.getClass()).getMethods();
    for (Method method : methods) {
      if (isMethodALinkedItemBean(method)) {
        String name = getBeanName(method);
        Object bean = this.appContext.getBean(name);
        LinkedItem ann = method.getDeclaredAnnotation(LinkedItem.class);
        result.put(name, new LinkedItemBean(bean, ann));
      }
    }
  }
  

  /**
   *
   * @param method
   * @return
   */
  private String getBeanName(Method method) {
    String name;
    Bean beanAnn = method.getDeclaredAnnotation(Bean.class);
    if (beanAnn.value().length != 0) {
      name = beanAnn.value()[0];
    } else {
      name = beanAnn.name()[0];
    }
    return name;
  }

  /**
   *
   * @param method
   * @return
   */
  private boolean isMethodALinkedItemBean(Method method) {
    LinkedItem linkedItemAnnotation = method.getDeclaredAnnotation(LinkedItem.class);
    Bean beanAnnotation = method.getDeclaredAnnotation(Bean.class);
    boolean isMethodALinkedItemBean = linkedItemAnnotation != null && beanAnnotation != null;
    return isMethodALinkedItemBean;
  }

  /**
   *
   * @param beanEntry
   */
  private void linkBean(Entry<String, LinkedItemBean> beanEntry) {
    try {
      Object bean = beanEntry.getValue().bean;
      LinkedItem annotation = beanEntry.getValue().annotation;
      String refName = annotation.collectionsRef();
      Object reference = this.appContext.getBean(refName);
      this.handler.linkBean(reference, bean);
    } catch (Exception ex) {
      throw new RuntimeException(
        String.format("An error occurred linking bean to collection: '%s'", beanEntry),
        ex);
    }
  }
  
  
  static class LinkedItemBean {

    private final Object bean;
    private final LinkedItem annotation;

    public LinkedItemBean(Object bean, LinkedItem annotation) {
      Objects.requireNonNull(bean);
      Objects.requireNonNull(annotation);
      this.bean = bean;
      this.annotation = annotation;
    }
  }
  
  
  
  public static class MapAggregator<K,V> {
    
    
    public MapAggregator(Map<K, V> result) {
      
    }
    
    
    
  }
}
