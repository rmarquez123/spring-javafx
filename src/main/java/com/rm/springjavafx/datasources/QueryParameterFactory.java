package com.rm.springjavafx.datasources;

import com.rm.datasources.QueryParameter;
import java.lang.reflect.Field;
import javafx.beans.property.Property;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author rmarquez
 */
public class QueryParameterFactory implements FactoryBean<QueryParameter>, InitializingBean, ApplicationContextAware {
  private String name;
  private String valueRef;
  private ApplicationContext appContext;

  public void setName(String name) {
    this.name = name;
  }

  public void setValueRef(String valueRef) {
    this.valueRef = valueRef;
  }
  
  /**
   * 
   * @throws Exception 
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    if (name == null) {
      throw new NullPointerException("Name cannot be null"); 
    }
    if (this.valueRef == null) {
      throw new NullPointerException("Value reference cannot be null"); 
    }
  }
  
  /**
   * 
   * @return
   * @throws Exception 
   */
  @Override
  public QueryParameter getObject() throws Exception {
    Property<?> value;
    if (!this.valueRef.contains("#")) {
      value = (Property<?>) this.appContext.getBean(this.valueRef);
    } else {
      String[] parts = this.valueRef.split("#"); 
      Object parent = this.appContext.getBean(parts[0]);
      Field field = parent.getClass().getDeclaredField(parts[1]);
      field.setAccessible(true);
      value = (Property<?>) field.get(parent); 
    }
    QueryParameter result = new QueryParameter(this.name, value);
    return result;
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public Class<?> getObjectType() {
    return QueryParameter.class; 
  }

  @Override
  public void setApplicationContext(ApplicationContext ac) throws BeansException {
    this.appContext = ac;
  }
  
}
