package com.rm.datasources;

import com.rm.springjavafx.datasources.AbstractDataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rmarquez
 */
public class SqlDataSource extends AbstractDataSource<RecordValue> {

  private final RecordValueQuery query;
  private final QueryParameters queryParams;
  private final DbConnection connection;

  /**
   *
   * @param connection
   * @param query
   * @param queryParams
   */
  public SqlDataSource(DbConnection connection, RecordValueQuery query, QueryParameters queryParams ) {
    this.connection = connection;
    this.query = query;
    if (queryParams != null) {
      this.queryParams = queryParams; 
    } else {
      this.queryParams = new QueryParameters(new ArrayList<>());
    }
    this.queryParams.addListener((observable, oldValue, newValue) -> {
      this.updateResultSet();
    });
    this.updateResultSet();
  }

  /**
   *
   * @return
   */
  public QueryParameters getQueryParams() {
    return this.queryParams;
  }
  
  public String getQuery() {
    return query.getQuery();
  }

  public DbConnection getConnection() {
    return connection;
  }
  

  /**
   *
   */
  private void updateResultSet() {
    List<RecordValue> resultSet; 
    try {
      
      resultSet = SqlUtils.executeListQuery(
              this.connection, this.query.getQuery(), 
              this.queryParams, this.query.getIdField());
      
    } catch (SQLException ex) {
      throw new RuntimeException(ex); 
    }
    super.setRecords(resultSet);
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
