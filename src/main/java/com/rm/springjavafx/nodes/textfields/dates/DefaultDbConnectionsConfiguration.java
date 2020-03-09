package com.rm.springjavafx.nodes.textfields.dates;

import common.db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Ricardo Marquez
 */
@Configuration
public class DefaultDbConnectionsConfiguration {

  /**
   * 
   * @return 
   */
  @Bean(name = "defaultDbConnections")
  public ObservableSet<DbConnection> defaultDbConnections() {
    ObservableSet<DbConnection> result = FXCollections.observableSet();
    return result;

  }
}
