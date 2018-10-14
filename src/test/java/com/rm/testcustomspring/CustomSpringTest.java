package com.rm.testcustomspring;

import com.rm.springjavafx.datasources.DbConnection;
import com.rm.springjavafx.datasources.SqlDataSource;
import com.rm.springjavafx.properties.DateRangeProperty;
import javafx.beans.property.IntegerProperty;
import javafx.scene.control.TableView;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author rmarquez
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/custom.xml"})
public class CustomSpringTest {
  
  @Autowired
  @Qualifier("test-tableview")
  private TableView tableView;
  
  @Autowired
  @Qualifier("dateRangeObs")
  private DateRangeProperty dateRangeProperty;
  
  @Autowired
  @Qualifier("sensorDefObs")
  private IntegerProperty sensorDefObs;
  
  @Autowired
  @Qualifier("stnIdObs")
  private IntegerProperty stnIdObs;
  
  @Autowired
  @Qualifier("db-connection")
  private DbConnection dbConnection;
  
  @Autowired
  @Qualifier("seriesDataSource")
  private SqlDataSource seriesDataSource;
  
  
  
  @BeforeClass
  public static void setUp() throws InterruptedException {
    JavaFxTest.initToolkit(); 
  }
  
  /**
   * 
   */
  @Test
  public void test() {
    Assert.assertNotNull("date range property not null", this.sensorDefObs);
    Assert.assertNotNull("date range property not null", this.dateRangeProperty);
    Assert.assertNotNull("dbConnection property not null", this.dbConnection);
    Assert.assertEquals("check url:", "mydb.com", this.dbConnection.getUrl());
    Assert.assertEquals("check user:", "postgres", this.dbConnection.getUser());
    Assert.assertEquals("check password:", "postgres", this.dbConnection.getPassword());
    Assert.assertEquals("check port:", new Integer(5432), this.dbConnection.getPort());
    Assert.assertEquals("check schema:", "emissions", this.dbConnection.getSchema());
    Assert.assertNotNull("seriesDataSource is not null", this.seriesDataSource);
    Assert.assertEquals("check size of query params:", 4, this.seriesDataSource.getQueryParams().size());
    Assert.assertEquals("check size of query params:", "", this.seriesDataSource.getQuery());
    Assert.assertEquals("check size of query params:", this.dbConnection, this.seriesDataSource.getConnection());
    Assert.assertEquals("check size of query params:", this.stnIdObs, this.seriesDataSource.getQueryParams().get("stnId").getValue());
    Assert.assertNotNull("tableview is not null", this.tableView);
  }
}
