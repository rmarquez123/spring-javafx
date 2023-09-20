package com.rm.springjavafx.annotations;

import com.rm.springjavafx.SpringFxUtils;
import java.util.stream.Stream;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author Ricardo Marquez
 */
public final class PropertyAccessor {

  private final ApplicationContext appContext;
  private final Object bean;

  /**
   *
   * @param appContext
   * @param bean
   */
  public PropertyAccessor(ApplicationContext appContext, Object bean) {
    this.appContext = appContext;
    this.bean = bean;
  }

  /**
   *
   * @param dependency
   * @return
   */
  Property<?> getValueProperty(String dependency) {
    String actualdependency = getActualDependency(dependency);
    Property<?> result = SpringFxUtils.getValueProperty(this.appContext, actualdependency);
    if (result == null) {
      throw new RuntimeException("dependency cannot be null: " + actualdependency);
    }
    return result;
  }

  /**
   *
   * @param dependency
   * @return
   */
  ObservableList getOvervableList(String dependency) {
    String actualdependency = this.getActualDependency(dependency);
    ObservableList result = SpringFxUtils.getValueObservableList(this.appContext, actualdependency);
    return result;
  }
  
  /**
   * 
   * @param dependency
   * @return 
   */
  public Object getObject(String dependency) {
    String actual = getActualDependency(dependency);
    Object result = this.appContext.getBean(actual);
    return result;
  }

  /**
   *
   * @param dependency
   * @return
   */
  private String getActualDependency(String dependency) {
    String actualdependency;
    if (this.bean instanceof ValueMapAccessor) {
      String fromValueMap = ((ValueMapAccessor) this.bean).getValue(dependency);
      actualdependency = fromValueMap != null ? fromValueMap : dependency;
    } else {
      ValueMap valuemap = bean.getClass().getDeclaredAnnotation(ValueMap.class);
      if (valuemap != null) {
        actualdependency = Stream.of(valuemap.values()) //
          .filter((s) -> s.split(",")[0].equals(dependency)) //
          .map(s -> s.split(",")[1])
          .findFirst()
          .orElse(dependency);
      } else {
        actualdependency = dependency;
      }
    }
    return actualdependency;
  }

}
