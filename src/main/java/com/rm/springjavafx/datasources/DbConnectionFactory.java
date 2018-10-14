package com.rm.springjavafx.datasources;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 *
 * @author rmarquez
 */
public class DbConnectionFactory implements FactoryBean<DbConnection>, InitializingBean {
    
  private String dbUser;
  private String dbPassword;
  private Integer dbPort;
  private String dbUrl;
  private String dbSchema;

  public void setDbUser(String dbUser) {
    this.dbUser = dbUser;
  }

  public void setDbPassword(String dbPassword) {
    this.dbPassword = dbPassword;
  }

  public void setDbPort(Integer dbPort) {
    this.dbPort = dbPort;
  }

  public void setDbUrl(String dbUrl) {
    this.dbUrl = dbUrl;
  }

  public void setDbSchema(String dbSchema) {
    this.dbSchema = dbSchema;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    if (this.dbUrl == null) {
      throw new IllegalStateException("db user cannot be null"); 
    }
    if (this.dbPassword == null) {
      throw new IllegalStateException("db password cannot be null"); 
    }
    if (this.dbPort == null) {
      throw new IllegalStateException("db port cannot be null"); 
    }
    if (this.dbUrl == null) {
      throw new IllegalStateException("db url cannot be null"); 
    }
  }
  
  
  
  /**
   * 
   * @return
   * @throws Exception 
   */
  @Override
  public DbConnection getObject() throws Exception {
    DbConnection result = new DbConnection.Builder()
            .setUser(dbUser)
            .setPassword(dbPassword)
            .setUrl(dbUrl)
            .setPort(dbPort)
            .setSchema(dbSchema)
            .createDbConnection();
    
    return result;
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public Class<?> getObjectType() {
    return DbConnection.class;
  }

}
