package com.rm.springjavafx.datasources;

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

  @Override
  public String toString() {
    return "DbConnection{" + "user=" + user + ", password=" + password + ", schema=" + schema + ", url=" + url + ", port=" + port + '}';
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
