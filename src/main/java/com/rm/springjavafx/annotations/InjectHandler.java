package com.rm.springjavafx.annotations;

import com.rm.springjavafx.FxmlInitializerListener;
import com.rm.springjavafx.SpringFxUtils;
import common.RmExceptions;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.stream.Stream;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

/**
 *
 * @author Ricardo Marquez
 */
@Component("injectHandler")
@Lazy(false)
public class InjectHandler implements FxmlInitializerListener {

  @Autowired
  private ApplicationContext applicationContext;

  /**
   *
   */
  @Override
  public void onInitialized() {
    Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(Injectable.class);
    for (Map.Entry<String, Object> entry : beans.entrySet()) {
      Object bean = entry.getValue();
      Scope scope = SpringFxUtils.getAnnotation(bean, Scope.class);
      if (scope != null && scope.value().equals("prototype")) {
        continue;
      }
      this.processBeanFields(bean);
    }
    this.invokePostInjectBeans();
  }

  /**
   * Will scan bean for field annotations with {@linkplain Inject} annotation for each
   * will set the bean value to the field. Note that the scanning requires the fields to
   * be public fields.
   *
   * @param bean
   * @see #processBeanMethods(java.lang.Object) for similar but with methods. 
   * @throws SecurityException
   */
  public void processBeanFields(Object bean) {
    Field[] fields = bean.getClass().getFields();
    Stream.of(fields) //
      .filter(field -> field.isAnnotationPresent(Inject.class)) //
      .forEach(field -> this.processField(field, bean));
  }

  /**
   *
   * @throws BeansException
   * @throws RuntimeException
   */
  private void invokePostInjectBeans() {
    Map<String, Object> postinjectbeans = this.getPostInjectBeans();
    for (Map.Entry<String, Object> entry : postinjectbeans.entrySet()) {
      Object bean = entry.getValue();
      Object name = entry.getKey();
      Scope scope = SpringFxUtils.getAnnotation(bean, Scope.class);
      if (scope != null && scope.value().equals("prototype")) {
        continue;
      }
      this.processBeanMethods(bean);
    }
  }

  /**
   *
   * @param bean
   * @throws SecurityException
   */
  public void processBeanMethods(Object bean) throws SecurityException {
    Method[] methods = ReflectionUtils.getUniqueDeclaredMethods(bean.getClass());
    for (Method method : methods) {
      if (method.isAnnotationPresent(PostInjectHandler.class)) {
        this.invokePostInjectHandler(method, bean);
      }
    }
  }

  /**
   *
   * @return @throws BeansException
   */
  private Map<String, Object> getPostInjectBeans() throws BeansException {
    return this.applicationContext //
      .getBeansWithAnnotation(PostInject.class);
  }

  /**
   *
   * @param method
   * @param bean
   */
  private void invokePostInjectHandler(Method method, Object bean) {
    try {
      method.invoke(bean);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   *
   * @param field
   * @param bean
   * @throws RuntimeException
   */
  private void processField(Field field, Object bean) {
    try {
      Object property = this.getProperty(field, bean);
      field.setAccessible(true);
      field.set(bean, property);
    } catch (Exception ex) {
      throw RmExceptions.create(ex, "Error on setting field '%s' for bean '%s'", field, bean);
    }
  }

  /**
   *
   * @param field
   * @param bean
   * @return
   */
  private Object getProperty(Field field, Object bean) {
    Inject inject = field.getAnnotation(Inject.class);
    String dependency = inject.value();
    PropertyAccessor accessor = new PropertyAccessor(applicationContext, bean);
    Object property;
    if (Property.class.isAssignableFrom(field.getType())) {
      property = accessor.getValueProperty(dependency);
    } else if (ObservableList.class.isAssignableFrom(field.getType())) {
      property = accessor.getOvervableList(dependency);
    } else {
      property = accessor.getObject(dependency);
    }
    return property;
  }
}
