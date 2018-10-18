package com.rm.datasources;

import com.rm.springjavafx.converters.Converter;
import javafx.beans.property.ListProperty;
import javafx.collections.ObservableList;

/**
 *
 * @author rmarquez
 * @param <T>
 */
public interface DataSource<T> {
  
  /**
   * 
   * @param <E>
   * @param items 
   * @param converter 
   */
  public <E> void bind(ObservableList<E> items, Converter<T, E> converter);
  
  /**
   * 
   * @param <E>
   * @param items 
   * @param converter 
   */
  public <E> void bind(ListProperty<E> items, Converter<T, E> converter);
  
  /**
   * 
   * @return  
   */
  public ListProperty<T> listProperty();
}
