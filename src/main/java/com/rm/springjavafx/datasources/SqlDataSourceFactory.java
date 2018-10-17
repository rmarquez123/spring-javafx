package com.rm.springjavafx.datasources;

import com.rm.datasources.QueryParameters;
import com.rm.datasources.DbConnection;
import com.rm.datasources.SqlDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author rmarquez
 */
public class SqlDataSourceFactory implements FactoryBean<SqlDataSource>, InitializingBean, ApplicationContextAware {
    
  private QueryParameters queryParams; 
  private String dbConnectionRef;
  private String queryFile; 
  private ApplicationContext appContext;
  
  /**
   * 
   * @param queryParams 
   */
  public void setQueryParams(QueryParameters queryParams) {
    this.queryParams = queryParams;
  }

  public void setDbConnectionRef(String dbConnectionRef) {
    this.dbConnectionRef = dbConnectionRef;
  }

  public void setQueryFile(String queryFile) {
    this.queryFile = queryFile;
  }
  

  @Override
  public void afterPropertiesSet() throws Exception {
    if (this.dbConnectionRef == null) {
      throw new NullPointerException("db connection cannot be null"); 
    }
    if (this.queryFile == null) {
      throw new NullPointerException("query file cannot be null"); 
    }
  }
  
  
  /**
   * 
   * @return
   * @throws Exception 
   */
  @Override
  public SqlDataSource getObject() throws Exception {
    DbConnection connection = (DbConnection) this.appContext.getBean(this.dbConnectionRef);
    String query = this.getQueryString();
    SqlDataSource result = new SqlDataSource(connection, query, this.queryParams); 
    return result; 
  }

  private String getQueryString() throws IOException {
    InputStream r = this.getClass().getClassLoader().getResourceAsStream(this.queryFile);
    if (r == null) {
      throw new IllegalStateException("File does not exist : '" + this.queryFile + "'"); 
    }
    List<String> lines = IOUtils.readLines(r, Charset.forName("UTF-8"));
    String query = String.join("\n", lines);
    return query;
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public Class<?> getObjectType() {
    return SqlDataSource.class;
  }

  @Override
  public void setApplicationContext(ApplicationContext ac) throws BeansException {
    this.appContext = ac;
  }
  
}
