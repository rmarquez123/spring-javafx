package com.rm.datasources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rmarquez
 */
public class DbConnection {

  private final String user;
  private final String password;
  private final String schema;
  private final String url;
  private final Integer port;
  private Connection connection;

  public DbConnection(String user, String password, String schema, String url, Integer port) {
    this.user = user;
    this.password = password;
    this.schema = schema;
    this.url = url;
    this.port = port;
  }

  public String getUser() {
    return user;
  }

  public String getPassword() {
    return password;
  }

  public String getSchema() {
    return schema;
  }

  public String getUrl() {
    return url;
  }

  public Integer getPort() {
    return port;
  }
  
  /**
   * 
   * @param sql
   * @param consumer 
   */
  public void executeQuery(String sql, Consumer<ResultSet> consumer) {
    Connection conn = this.getConnection();
    try {
      PreparedStatement statement = conn.prepareStatement(sql); 
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        consumer.accept(resultSet);
      }
    } catch(Exception ex) {
      throw new RuntimeException(ex);
    } finally {
      try {
        conn.close();
      } catch (SQLException ex) {
        Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }; 
    
  @Override
  public String toString() {
    return "DbConnection{" + "user=" + user + ", password=" + password + ", schema=" + schema + ", url=" + url + ", port=" + port + '}';
  }

  /**
   * Returns a new connection.
   *
   * @return
   */
  public Connection getConnection() {
    Connection result;
    try {
      String _url = "jdbc:postgresql://" + this.getUrl()
              + ":" + this.port
              + "/" + this.schema;
      String _username = this.user;
      String _password = this.password;
      result = DriverManager.getConnection(_url, _username, _password);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    return result;
  }

  /**
   *
   */
  public static class Builder {

    private String user;
    private String password;
    private String schema;
    private String url;
    private Integer port;

    public Builder() {
    }

    public Builder setUser(String user) {
      this.user = user;
      return this;
    }

    public Builder setPassword(String password) {
      this.password = password;
      return this;
    }

    public Builder setSchema(String schema) {
      this.schema = schema;
      return this;
    }

    public Builder setUrl(String url) {
      this.url = url;
      return this;
    }

    public Builder setPort(Integer port) {
      this.port = port;
      return this;
    }

    public DbConnection createDbConnection() {
      return new DbConnection(user, password, schema, url, port);
    }

  }

}
