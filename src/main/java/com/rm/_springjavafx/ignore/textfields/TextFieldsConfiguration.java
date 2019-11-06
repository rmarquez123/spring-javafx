package com.rm._springjavafx.ignore.textfields;

import common.bindings.RmBindings;
import java.time.ZoneId;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Ricardo Marquez
 */
@Configuration
public class TextFieldsConfiguration {
  
  @Bean(name="zoneIdProperty")
  public Property<ZoneId> zoneIdProperty() {
    return new SimpleObjectProperty<>(ZoneId.of("US/Pacific")); 
  }
  
  /**
   * 
   * @param records
   * @return 
   */
  @Bean(name = "selected.record")
  public Property<Record> selectedRecord( //
    @Qualifier("records") ObservableSet<Record> records) {
    SimpleObjectProperty<Record> result = new SimpleObjectProperty<>();
    RmBindings.bindToFirstValueInSet(records, result);
    return result;
  }
    
  /**
   * 
   * @return 
   */
  @Bean(name = "records")
  public ObservableSet<Record> records() {
    ObservableSet<Record> result = FXCollections.observableSet();
    result.add(new Record(0, 2.222, "test1"));
    result.add(new Record(1, 2.222, "test3"));
    return result;
  }
    
  /**
   * 
   * @param selectedProperty
   * @param records
   * @return 
   */
  @Bean(name="mutable.selected.record")
  public Property<RecordObservable> mutableselectedrecord(
    @Qualifier("selected.record") Property<Record> selectedProperty,
    @Qualifier("mutable.records") ObservableSet<RecordObservable> records) {
 
    Property<RecordObservable> result = new SimpleObjectProperty<>();
    RmBindings.bindObject(result, records, selectedProperty, (e, v)->e.toRecord().equals(v));
    return result;
  }
  
  /**
   * 
   * @return 
   */
  @Bean(name = "mutable.records")
  public ObservableSet<RecordObservable> mutablerecords( //
    ObservableSet<Record> records) {
    ObservableSet<RecordObservable> result = FXCollections.observableSet();
    RmBindings.bindCollections(result, records, (r)-> RecordObservable.fromRecord(r));
    return result;
  }

}
