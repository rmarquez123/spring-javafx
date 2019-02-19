package com.windsim.wpls.db.linesolver.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.Test;


/**
 *
 * @author Ricardo Marquez
 */
public class MsConnectionTest {

  @Test
  public void test1() throws Exception {
    Map<String, Object> credentials = new HashMap<>();
    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("wpls_idaho_power_pu_ms", credentials);
    emf.createEntityManager();
  }

  @Test
  public void test2() throws Exception {
    String url = "jdbc:sqlserver://localhost:1433";
    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    Connection conn = DriverManager.getConnection(url, "test", "test");
    PreparedStatement statment = conn.prepareStatement("select * from conductor"); 
    ResultSet resultSet = statment.executeQuery();
    while (resultSet.next()) {
      String type = resultSet.getString("type"); 
      System.out.println("type = " + type);
    }
  }

}
