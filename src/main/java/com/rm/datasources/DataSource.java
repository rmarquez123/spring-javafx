package com.rm.datasources;

import com.rm.springjavafx.components.ComboBoxFactory;
import com.rm.springjavafx.converters.Converter;
import com.rm.springjavafx.table.TableViewFactory;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;

/**
 * Data source class used by various factories for creating list types of components and
 * binding data to them.
 *
 * @author rmarquez
 * @param <T>  
 * @see ComboBoxFactory
 * @see TableViewFactory
 */
public interface DataSource<T> {
  
  /**
   * 
   * @return 
   */
  public ListProperty<T> checkedValuesProperty();

  /**
   * Implementers should bind the passed items to their records using the provided
   * converter.
   *
   * @param <Output>
   * @param items
   * @param converter
   *
   */
  public <Output> void bind(ObservableList<Output> items, Converter<T, Output> converter);

  /**
   * Implementers should bind the passed items to their records using the provided
   * converter.
   *
   * @param <Output>
   * @param items
   * @param converter
   */
  public <Output> void bind(ListProperty<Output> items, Converter<T, Output> converter);

  /**
   * Return a list property.
   *
   * @return
   */
  public ListProperty<T> listProperty();

  /**
   *
   * @return
   */
  public ObservableList<T> getMultiSelectionProperty();

  /**
   *
   * @return
   */
  public ObjectProperty<T> getSingleSelectionProperty();
}
