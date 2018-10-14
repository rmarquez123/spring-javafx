package com.rm.springjavafx.datasources;

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
  public <E> void bind(ObservableList<E> items, Converter<E, T> converter);
}
