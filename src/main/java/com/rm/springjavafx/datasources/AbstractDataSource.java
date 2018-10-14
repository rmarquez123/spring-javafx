package com.rm.springjavafx.datasources;

import javafx.collections.ObservableList;

/**
 *
 * @author rmarquez
 * @param <T>
 */
public abstract class AbstractDataSource<T> implements DataSource<T> {
  /**
   * 
   * @param <E>
   * @param items
   * @param converter 
   */
  @Override
  public final <E> void bind(ObservableList<E> items, Converter<E, T> converter) {
    
  }
  
}
