package com.rm.springjavafx.datasources;

import com.rm.datasources.QueryParameter;
import com.rm.springjavafx.SpringFxUtils;
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
    Property<?> valueProperty = SpringFxUtils.getValueProperty(appContext, this.valueRef); 
    QueryParameter result = new QueryParameter(this.name, valueProperty);
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
