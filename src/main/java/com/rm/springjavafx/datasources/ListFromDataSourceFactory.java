package com.rm.springjavafx.datasources;

import com.rm.datasources.DataSource;
import javafx.beans.property.ListProperty;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author rmarquez
 */
public class ListFromDataSourceFactory implements FactoryBean<ListProperty>, InitializingBean, ApplicationContextAware  {

  private String dataSource;
  private ApplicationContext appContext;
  
  /**
   * 
   */
  public ListFromDataSourceFactory() {
  }
  
  public void setDataSource(String dataSource) {
    this.dataSource = dataSource;
  }
    
  /**
   * 
   * @return
   * @throws Exception 
   */
  @Override
  public ListProperty getObject() throws Exception {
    DataSource dataSourceObj = (DataSource) this.appContext.getBean(this.dataSource);
    ListProperty result = dataSourceObj.listProperty(); 
    return result; 
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public Class<?> getObjectType() {
    return ListProperty.class;
  }
    
  /**
   * 
   * @throws Exception 
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    if (this.dataSource == null) {
      throw new RuntimeException();
    }
  }

  @Override
  public void setApplicationContext(ApplicationContext ac) throws BeansException {
    this.appContext = ac;
  }
  
}
