package com.rm.springjavafx.form;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.StringConverter;

/**
 *
 * @author Ricardo Marquez
 * @param <T> The value type
 */
public final class FormItem<T> {

  private final StringProperty labelProperty;
  private final ObjectProperty valueProperty;
  private final BooleanProperty editableProperty = new SimpleBooleanProperty(true);
  private final StringConverter<T> stringConverter;
  
  
  /**
   *
   * @param labelProperty
   * @param valueProperty
   * @param stringConverter
   */
  public FormItem(StringProperty labelProperty, ObjectProperty valueProperty, StringConverter<T> stringConverter) {
    this.labelProperty = labelProperty;
    this.valueProperty = valueProperty;
    this.stringConverter = stringConverter;
  }

  /**
   *
   * @param label
   * @param valueProperty
   * @param stringConverter
   */
  public FormItem(String label, ObjectProperty valueProperty, StringConverter<T> stringConverter) {
    this(new SimpleStringProperty(label), valueProperty, stringConverter);
  }

  /**
   *
   * @param stringConverter
   */
  public FormItem(StringConverter<T> stringConverter) {
    this("", new SimpleObjectProperty<Object>(), stringConverter);
  }

  /**
   *
   * @return
   */
  public StringProperty labelProperty() {
    return labelProperty;
  }

  /**
   *
   * @return
   */
  public ObjectProperty valueProperty() {
    return valueProperty;
  }

  /**
   *
   * @return
   */
  public BooleanProperty editableProperty() {
    return editableProperty;
  }

  /**
   * 
   * @return 
   */
  public StringConverter<T> stringConverter() {
    return stringConverter;
  }

}
