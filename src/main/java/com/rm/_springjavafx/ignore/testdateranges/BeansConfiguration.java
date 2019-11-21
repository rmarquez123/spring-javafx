package com.rm._springjavafx.ignore.testdateranges;

import java.util.Set;
import java.util.TreeSet;
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
    Set<String> set = new TreeSet<>(String.CASE_INSENSITIVE_ORDER); 
    set.add("category 01"); 
    set.add("category 02"); 
    set.add("category 03"); 
    set.add("category 04"); 
    set.add("category 05"); 
    set.add("category 06"); 
    
    ObservableSet<String> result 
      = FXCollections.observableSet(set); 
    
    return result; 
  }
}
