package com.rm._springjavafx.ignore.testcategories;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Ricardo Marquez
 */
@Configuration
public class BeansConfiguration {
  
  @Bean("categories")
  public ObservableSet<String> categories() {
    ObservableSet<String> result = FXCollections.observableSet(); 
    result.add("category 01"); 
    result.add("category 02"); 
    result.add("category 03"); 
    result.add("category 04"); 
    result.add("category 05"); 
    result.add("category 06"); 
    return result; 
  }
}
