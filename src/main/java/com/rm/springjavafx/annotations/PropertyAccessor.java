package com.rm.springjavafx.annotations;

import com.rm.springjavafx.SpringFxUtils;
import java.util.stream.Stream;
import javafx.beans.property.Property;
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
    ValueMap valueMap = bean.getClass().getDeclaredAnnotation(ValueMap.class);
    String valuePair = //
    //
    Stream.of(valueMap.values()) //
    .filter((s) -> s.split(",")[0].equals(dependency)) //
    .findFirst().orElse(null);
    String otherBean = valuePair == null ? dependency : valuePair.split(",")[1];
    Property<?> result = SpringFxUtils.getValueProperty(this.appContext, otherBean);
    return result;
  }
  
}
