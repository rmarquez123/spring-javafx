package com.rm.springjavafx.annotations;

import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.SpringFxUtils;
import common.RmExceptions;
import common.bindings.RmBindings;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Lazy(false)
@DependsOn("injectHandler")
@PostInject
public class PubSubAnnotationHandler {

  @Autowired
  private FxmlInitializer fxmlInitializer;

  /**
   *
   * @throws Exception
   */
  @PostInjectHandler
  public void handlePubSubAnnotations() throws Exception {
    Map<String, Object> components = this.getComponents();
    for (Map.Entry<String, Object> object : components.entrySet()) {
      Object component = object.getValue();
      String beanname = object.getKey();
      Scope scope = SpringFxUtils.getAnnotation(component, Scope.class);
      component.getClass().getAnnotation(Scope.class);
      if (scope != null && scope.value().equals("prototype")) {
        continue;
      }
      this.fxmlInitializer.addListener(i -> this.bindMethodsWithSubscribeAnnotation(beanname, component));
    }
  }

  /**
   * This method will bind listening methods that are annotated with
   * {@linkplain  Subscribe}.
   *
   * @param component
   * @return
   */
  public void bindMethodsWithSubscribeAnnotation(String beanname, Object component) {

    Class<?> originalClass = AopProxyUtils.ultimateTargetClass(component);
    Method[] methods = originalClass.getMethods();
    for (Method method : methods) {
      Subscribe subscribeAttrs = method.getAnnotation(Subscribe.class);
      if (subscribeAttrs == null) {
        continue;
      }
      String[] dependencies = subscribeAttrs.dependencies();
      Subscribe.Type bindtype = subscribeAttrs.bindtype();
      try {
        if (bindtype == Subscribe.Type.PROPERTIES) {
          this.bindMethodInvocationWithProperties(method, component, dependencies);
        } else if (bindtype == Subscribe.Type.LIST) {
          this.bindMethodInvocationWithList(method, component, dependencies);
        }
      } catch (Exception ex) {
        throw new RuntimeException( //
          String.format("An error occured on binding method '%s' " //
            + "for bean name (%s), object '%s' " //
            + "with method subscribe dependencies '%s'", //
            method, beanname, component, Arrays.toString(dependencies)), ex);
      }
    }
  }

  /**
   *
   * @return @throws BeansException
   */
  private Map<String, Object> getComponents() throws BeansException {
    ApplicationContext context = getContext();
    Map<String, Object> result = context.getBeansWithAnnotation(Component.class);
    return result;
  }

  /**
   *
   * @param method
   * @param component
   * @param dependencies
   */
  private void bindMethodInvocationWithProperties(
    Method method, // 
    Object component, String[] dependencies) {
    Property[] properties = new Property[dependencies.length];
    PropertyAccessor accessor = new PropertyAccessor(getContext(), component);
    for (int i = 0; i < dependencies.length; i++) {
      String dependency = dependencies[i];
      try {
        properties[i] = accessor.getValueProperty(dependency);
      } catch (Exception ex) {
        RmExceptions.throwException(ex,
          "Error on getting dependency '%s' for component '%s'", dependency, component);
      }
    }
    RmBindings.bindActionOnAnyChange(() -> invokeMethod(method, component), properties);
    invokeMethod(method, component);
  }

  /**
   *
   * @param method
   * @param component
   * @param dependencies
   */
  private void bindMethodInvocationWithList(Method method, Object component, String[] dependencies) {
    ObservableList[] properties = new ObservableList[dependencies.length];
    PropertyAccessor accessor = new PropertyAccessor(this.getContext(), component);

    for (int i = 0; i < dependencies.length; i++) {
      String dependency = dependencies[i];
      properties[i] = accessor.getOvervableList(dependency);

    }
    RmBindings.bindActionOnAnyChange(() -> invokeMethod(method, component), properties);
    this.invokeMethod(method, component);
  }

  /**
   *
   * @param method
   * @param bean
   */
  private void invokeMethod(Method method, Object bean) {
    Publish publishdefinition = method.getDeclaredAnnotation(Publish.class);
    if (publishdefinition != null) {
      invokeMethod(bean, method, publishdefinition);
    } else {
      try {
        method.invoke(bean);
      } catch (Exception ex) {
        throw RmExceptions.create(ex,
          "Error invoking method : '%s' from component '%s;", method.getName(), bean.getClass());
      }
    }
  }

  /**
   *
   * @param publishdefintion
   * @param method
   * @param bean
   */
  private void invokeMethod(Object bean, Method method, Publish publishdefintion) {
    try {
      if (publishdefintion.thread() == Publish.Thread.NEW // 
        || publishdefintion.thread() == Publish.Thread.NEW_THEN_JFXPLATFORM) {
        this.runOnThread(bean, method, publishdefintion);
      } else {
        try {
          Object value = method.invoke(bean);
          this.setValue(bean, publishdefintion, value);
        } catch (Exception ex) {
          throw RmExceptions
            .create(ex, "Error invoking method '%s' on component '%s", //
              method.getName(), bean.getClass());
        }

      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   *
   * @param publishdefintion
   * @param method
   * @param bean
   * @throws BeansException
   */
  private void runOnThread(Object bean, Method method, Publish publishdefintion) {
    Property<Boolean> processFlag = getProcessFlag(publishdefintion);
    if (processFlag.getValue()) {
      return;
    }
    processFlag.setValue(true);
    new Thread(() -> {
      try {
        Object value = method.invoke(bean);
        if (publishdefintion.thread() == Publish.Thread.NEW_THEN_JFXPLATFORM) {
          this.setValueOnPlatformThread(bean, publishdefintion, value);
        } else {
          this.setValue(bean, publishdefintion, value);
        }
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      } finally {
        processFlag.setValue(false);
      }
    }).start();
  }

  /**
   *
   * @param binds
   * @param value
   */
  private void setValueOnPlatformThread(Object bean, Publish binds, Object value) {
    Platform.runLater(() -> {
      this.setValue(bean, binds, value);
    });
  }

  /**
   *
   * @param binds
   * @return
   * @throws BeansException
   */
  private Property<Boolean> getProcessFlag(Publish binds) {
    Property<Boolean> processFlag;
    if (!binds.processflag().isEmpty()) {
      processFlag = (Property<Boolean>) this.getContext().getBean(binds.processflag());
    } else {
      processFlag = new SimpleObjectProperty<>(false);
    }
    return processFlag;
  }

  /**
   *
   * @param binds
   * @param value
   */
  private void setValue(Object bean, Publish binds, Object value) {
    PropertyAccessor accessor = new PropertyAccessor(this.getContext(), bean);
    switch (binds.type()) {
      case PROPERTIES: {
        Property p = accessor.getValueProperty(binds.value());
        p.setValue(value);
        break;
      }
      case LIST: {
        ObservableList p = accessor.getOvervableList(binds.value());
        p.clear();
        p.setAll((Collection) value);
        break;
      }
      default:
        throw new UnsupportedOperationException();
    }
  }

  /**
   *
   * @return
   */
  private ApplicationContext getContext() {
    ApplicationContext result = this.fxmlInitializer.getContext();
    return result;
  }

}
