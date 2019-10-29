package com.rm._springjavafx.ignore.textfields;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Ricardo Marquez
 */
@Configuration
public class TextFieldsConfiguration {
  
  
  @Bean(name = "numberProperty")
  public Property<Double> numberProperty() {
    SimpleObjectProperty<Double> result = new SimpleObjectProperty<>(2.22);
    return result;
  }
  
}
