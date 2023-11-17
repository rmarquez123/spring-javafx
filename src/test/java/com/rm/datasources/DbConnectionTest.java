package com.rm.datasources;

import common.db.DbConnection;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Ricardo Marquez
 */
public class DbConnectionTest {
  
  public DbConnectionTest() {
  }

  @Test
  public void testSomeMethod() {
    String connUrl = "jdbc:postgresql://localhost:5434/idaho_power"; 
    DbConnection connection = DbConnection.create(connUrl, "postgres", "pw"); 
    Assert.assertEquals("", "localhost", connection.getConnPool().getUrl());
    Assert.assertEquals("", Integer.valueOf(5434), connection.getConnPool().getPort());
    Assert.assertEquals("", "idaho_power", connection.getConnPool().getDatabaseName());
    Assert.assertEquals("", "postgres", connection.getConnPool().getUser());
    Assert.assertEquals("", "pw", connection.getConnPool().getPassword());
  }
  
}
