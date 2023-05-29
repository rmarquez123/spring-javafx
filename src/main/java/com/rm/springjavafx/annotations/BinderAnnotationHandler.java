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
import org.springframework.beans.BeansException;
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
public class BinderAnnotationHandler implements InitializingBean {

  @Autowired
  private FxmlInitializer fxmlInitializer;

  /**
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    Map<String, Object> components = this.getComponents();
    for (Map.Entry<String, Object> object : components.entrySet()) {
      Object component = object.getValue();
      String beanname = object.getKey(); 
      if (this.isFxController(component)) {
        this.fxmlInitializer.addListener(i -> this.bindMethods(beanname, component));
      } else {
        this.bindMethods(beanname, component);
      }
    }
  }

  /**
   *
   * @param component
   * @return
   */
  private boolean isFxController(Object component) {
    Class<? extends Object> componentClass = component.getClass();
    FxController annotation = componentClass.getDeclaredAnnotation(FxController.class);
    boolean result = annotation != null;
    return result;
  }

  /**
   *
   * @param component
   * @return
   */
  private void bindMethods(String beanname, Object component) {
    Method[] methods = component.getClass().getMethods();
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
    for (int i = 0; i < dependencies.length; i++) {
      String dependency = dependencies[i];
      try {
        properties[i] = SpringFxUtils.getValueProperty(getContext(), dependency);
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
    for (int i = 0; i < dependencies.length; i++) {
      String dependency = dependencies[i];
      properties[i] = SpringFxUtils.getValueObservableList(getContext(), dependency);
    }
    RmBindings.bindActionOnAnyChange(() -> invokeMethod(method, component), properties);
    this.invokeMethod(method, component);
  }

  /**
   *
   * @param method
   * @param component
   */
  private void invokeMethod(Method method, Object component) {
    Publish binds = method.getDeclaredAnnotation(Publish.class);
    if (binds != null) {
      invokeMethod(binds, method, component);
    } else {
      try {
        method.invoke(component);
      } catch (Exception ex) {
        throw RmExceptions.create(ex,
          "Error invoking method : '%s' from component '%s;", method.getName(), component.getClass());
      }
    }
  }

  /**
   *
   * @param binds
   * @param method
   * @param component
   */
  private void invokeMethod(Publish binds, Method method, Object component) {
    try {
      if (binds.thread() == Publish.Thread.NEW // 
        || binds.thread() == Publish.Thread.NEW_THEN_JFXPLATFORM) {
        this.runOnThread(binds, method, component);
      } else {
        try {
          Object value = method.invoke(component);
          this.setValue(binds, value);
        } catch (Exception ex) {
          throw RmExceptions
            .create(ex, "Error invoking method '%s' on component '%s", //
              method.getName(), component.getClass());
        }

      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   *
   * @param binds
   * @param method
   * @param component
   * @throws BeansException
   */
  private void runOnThread(Publish binds, Method method, Object component) {
    Property<Boolean> processFlag = getProcessFlag(binds);
    if (processFlag.getValue()) {
      return;
    }
    processFlag.setValue(true);
    new Thread(() -> {
      try {
        Object value = method.invoke(component);
        if (binds.thread() == Publish.Thread.NEW_THEN_JFXPLATFORM) {
          this.setValueOnPlatformThread(binds, value);
        } else {
          this.setValue(binds, value);
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
  private void setValueOnPlatformThread(Publish binds, Object value) {
    Platform.runLater(() -> {
      this.setValue(binds, value);
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
  private void setValue(Publish binds, Object value) {
    switch (binds.type()) {
      case PROPERTIES: {
        Property p = SpringFxUtils.getValueProperty(this.getContext(), binds.value());
        p.setValue(value);
        break;
      }
      case LIST: {
        ObservableList p = SpringFxUtils.getValueObservableList(this.getContext(), binds.value());
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
