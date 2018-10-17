package com.rm.datasources;

import com.rm.springjavafx.datasources.AbstractDataSource;
import java.util.List;

/**
 *
 * @author rmarquez
 */
public class SqlDataSource extends AbstractDataSource<RecordValue> {

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
    return query;
  }

  public DbConnection getConnection() {
    return connection;
  }

  /**
   *
   */
  private void updateResultSet() {
    String queryString = SqlUtils.createQueryString(this.query, this.queryParams);
    List<RecordValue> resultSet = SqlUtils.executeListQuery(this.connection, queryString);
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
