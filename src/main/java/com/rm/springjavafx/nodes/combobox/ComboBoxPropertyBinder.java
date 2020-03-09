package com.rm.springjavafx.nodes.combobox;

import javafx.beans.property.Property;
import javafx.scene.control.ComboBox;

/**
 *
 * @author Ricardo Marquez
 */
public class ComboBoxPropertyBinder {

  private final ComboBox comboBox;
  private final String[] beanId;
  private final Object parentBean;

  public ComboBoxPropertyBinder( //
    ComboBox comboBox, String[] beanId, Object parentBean) {
    this.comboBox = comboBox;
    this.beanId = beanId;
    this.parentBean = parentBean;
  }

  void bind() {
    if (beanId.length == 1) {
      this.doSingleLayerBinding();
    } else if (parentBean != null) {
      this.doLayerBinding();
    }
  }

  /**
   *
   * @param binder
   * @throws RuntimeException
   */
  private void doLayerBinding() {
    throw new UnsupportedOperationException();
  }

  /**
   *
   */
  private void doSingleLayerBinding() {
    Property property = (Property) parentBean;
    property.addListener((obs, old, change)->{
      this.comboBox.getSelectionModel().select(change);
    });
    this.comboBox.getSelectionModel().selectedItemProperty().addListener((obs, old, change)->{
      property.setValue(change);
    });
    this.comboBox.getSelectionModel().select(property.getValue());
    
  }
}
