package com.rm.springjavafx.form;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Ricardo Marquez
 */
public class FormGroup {
  
  private final ObservableList<FormItem> formItems = FXCollections.observableArrayList();
  private final StringProperty textProperty = new SimpleStringProperty();
  /**
   * 
   * @return 
   */
  public ObservableList<FormItem> getItems() {
    return this.formItems;
  } 

  /**
   * 
   * @return 
   */
  public StringProperty textProperty() {
    return textProperty;
  }
  
}
