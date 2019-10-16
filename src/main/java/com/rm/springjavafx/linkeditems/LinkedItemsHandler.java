package com.rm.springjavafx.linkeditems;

import common.bindings.RmBindings;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

/**
 *
 * @author Ricardo Marquez
 */
public class LinkedItemsHandler {

  /**
   *
   * @param value
   */
  public void linkBean(Object reference, Object value) {
    if (!(value instanceof Property)) {
      throw new IllegalArgumentException();
    }
    if ((reference instanceof ObservableSet)) {
      SetLinker linker = new SetLinker((ObservableSet<?>) reference, (Property<?>) value);
      RmBindings.bindActionOnAnyChange(linker::linkValue, (Property) value);
      linker.linkValue();
    } else if (reference instanceof ObservableList) {
      ListLinker linker = new ListLinker((ObservableList<?>) reference, (Property<?>) value);
      RmBindings.bindActionOnAnyChange(linker::linkValue, (Property) value);
      linker.linkValue();
    } else {
      throw new IllegalArgumentException();
    }
  }

  /**
   *
   */
  static class SetLinker<T> {

    private final ObservableSet<T> reference;

    private final Property<T> property;

    SetLinker(ObservableSet<T> reference, Property<T> property) {
      this.reference = reference;
      this.property = property;
    }

    /**
     *
     */
    void linkValue() {
      T value = property.getValue();
      if (value != null) {
        this.reference.add(value);
      }
    }
  }
  /**
   *
   */
  static class ListLinker<T> {

    private final ObservableList<T> reference;

    private final Property<T> property;

    ListLinker(ObservableList<T> reference, Property<T> property) {
      this.reference = reference;
      this.property = property;
    }

    /**
     *
     */
    void linkValue() {
      T value = property.getValue();
      if (value != null && !this.reference.contains(value)) {
        System.out.println("adding value");
        this.reference.add(value);
      }
    }
  }

}
