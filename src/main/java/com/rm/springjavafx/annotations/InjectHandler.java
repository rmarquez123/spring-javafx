package com.rm.springjavafx.annotations;

import com.rm.springjavafx.FxmlInitializerListener;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.stream.Stream;
import javafx.beans.property.Property;
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
public class InjectHandler implements FxmlInitializerListener{
  @Autowired
  private ApplicationContext applicationContext;
  
  /**
   * 
   */
  @Override
  public void onInitialized() {
    Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(Component.class); 
    for (Map.Entry<String, Object> entry : beans.entrySet()) {
      Object bean = entry.getValue();
      Field[] fields = bean.getClass().getDeclaredFields();
      Stream.of(fields) //
        .filter(field -> field.isAnnotationPresent(Inject.class)) //
        .forEach(field->this.processField(field, bean));
    }
  }
  
  /**
   * 
   * @param field
   * @param bean
   * @throws RuntimeException 
   */
  private void processField(Field field, Object bean) throws RuntimeException {
    Property<?> property = this.getProperty(field, bean);
    try {
      field.setAccessible(true);
      field.set(bean, property);
    } catch (IllegalArgumentException | IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  /**
   * 
   * @param field
   * @param bean
   * @return 
   */
  private Property<?> getProperty(Field field, Object bean) {
    Inject inject = field.getAnnotation(Inject.class);
    String dependency = inject.value();
    PropertyAccessor accessor = new PropertyAccessor(applicationContext, bean);
    Property<?> property = accessor.getValueProperty(dependency);
    return property;
  }
}
