package com.rm.springjavafx.components;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

/**
 *
 * @author Ricardo Marquez
 * @param <T>
 */
public class ChildNodeWrapper<T extends Node> {
  
  private final Property<T> nodeProperty = new SimpleObjectProperty<>();

  public void setNode(T node) {
    this.nodeProperty.setValue(node);
  }

  public T getNode() {
    return nodeProperty.getValue();
  }
  
  /**
   * 
   * @return 
   */
  public Property<T> nodeProperty() {
    return this.nodeProperty;
  }
  
}
