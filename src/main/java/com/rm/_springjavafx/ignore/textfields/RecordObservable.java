package com.rm._springjavafx.ignore.textfields;

import java.time.ZonedDateTime;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author Ricardo Marquez
 */
public class RecordObservable {
  private final Property<Number> numberProperty = new SimpleObjectProperty<>();
  private final Property<String> stringProperty = new SimpleObjectProperty<>();
  private final Property<String> fileProperty = new SimpleObjectProperty<>();
  private final Property<ZonedDateTime> dateTimeProperty = new SimpleObjectProperty<>();
  private final Property<Boolean> selectProperty = new SimpleObjectProperty<>();
  private final int id;
  
  private RecordObservable(int id) {
    this.id = id;
  }
  
  public final Property<Number> numberProperty() {
    return this.numberProperty;
  }
  
  public final Property<String> stringProperty() {
    return this.stringProperty;
  }
  
  public final Property<String> fileProperty() {
    return this.fileProperty;
  }
  
  public final Property<ZonedDateTime> dateTimeProperty() {
    return this.dateTimeProperty;
  }
  
  public final Property<Boolean> selectProperty() {
    return this.selectProperty;
  }
  
  /**
   * 
   * @param record 
   */
  public static RecordObservable fromRecord(Record record) {
    RecordObservable instance = new RecordObservable(record.id());
    instance.numberProperty().setValue(record.number());
    instance.stringProperty().setValue(record.string());
    return instance;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 19 * hash + this.id;
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final RecordObservable other = (RecordObservable) obj;
    if (this.id != other.id) {
      return false;
    }
    return true;
  }
  
  
  
  /**
   * 
   * @return 
   */
  public Record toRecord() {
    Record result = new Record(this.id, this.numberProperty.getValue().doubleValue(), this.stringProperty.getValue());
    return result;
  }
}
