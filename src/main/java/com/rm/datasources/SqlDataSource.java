package com.rm.datasources;

import com.rm.springjavafx.datasources.AbstractDataSource;

/**
 *
 * @author rmarquez
 */
public class SqlDataSource extends AbstractDataSource {

  private final String query;
  private final QueryParameters queryParams;
  private final DbConnection connection;
  
  
  /**
   * 
   * @param connection
   * @param query
   * @param queryParams 
   */
  public SqlDataSource(DbConnection connection, String query, QueryParameters queryParams) {
    this.connection = connection;
    this.query = query;
    this.queryParams = queryParams;
  }
  
  /**
   * 
   * @return 
   */
  public QueryParameters getQueryParams() {
    return this.queryParams;
  }

  public String getQuery() {
    return query;
  }

  public DbConnection getConnection() {
    return connection;
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public String toString() {
    return "SqlDataSource{" + "query=" + query
            + ", queryParams=" + queryParams 
            + ", connection=" + connection + '}';
  }
  
}
