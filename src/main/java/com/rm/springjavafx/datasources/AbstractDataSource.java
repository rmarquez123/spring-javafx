package com.rm.springjavafx.datasources;

import com.rm.springjavafx.converters.Converter;
import com.rm.datasources.DataSource;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author rmarquez
 * @param <T>
 */
public abstract class AbstractDataSource<T> implements DataSource<T> {
  
  protected ListProperty<T> records = new SimpleListProperty<>();
  
  /**
   * 
   * @param <E>
   * @param items
   * @param converter 
   */
  @Override
  public final <E> void bind(ObservableList<E> items, Converter<T, E> converter) {
    this.records.addListener((obs, old, change)->{
      items.clear();
      List<E> mapped = change.stream().map((c)->{
        return converter.convert(c);
      }).collect(Collectors.toList()); 
      items.addAll(mapped);
    });
  }
  
  /**
   * 
   * @param <E>
   * @param items
   * @param converter 
   */
  @Override
  public final <E> void bind(ListProperty<E> items, Converter<T, E> converter) {
    this.records.addListener((obs, old, change)->{
      items.clear();
      List<E> mapped = change.stream().map((c)->{
        return converter.convert(c);
      }).collect(Collectors.toList()); 
      items.setValue(FXCollections.observableArrayList(mapped));
    });
  }
  
  /**
   * 
   * @param records 
   */
  protected void setRecords(List<T> records) {
    this.records.setValue(FXCollections.observableArrayList(records));
  }
  
}
