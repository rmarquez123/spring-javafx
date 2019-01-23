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
 * Implement {@linkplain DataSource} by internally holdings records using
 * {@linkplain ListProperty} -- which means the list property event handlers can
 * be used instead of using binding methods.
 * <p>
 * Subtypes can also use set records which will add all records to the list
 * property thus changing it and triggering a change event.
 * </p>
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
    this.records.addListener((obs, old, change) -> {
      this.setValues(items, change, converter);
    });
    this.setValues(items, records.getValue(), converter);
  }

  /**
   *
   * @param <E>
   * @param items
   * @param converter
   */
  @Override
  public final <E> void bind(ListProperty<E> items, Converter<T, E> converter) {
    this.records.addListener((obs, old, change) -> {
      items.clear();
      List<E> mapped = change.stream().map((c) -> {
        return converter.convert(c);
      }).collect(Collectors.toList());
      items.setValue(FXCollections.observableArrayList(mapped));
    });
  }

  /**
   *
   * @return
   */
  @Override
  public ListProperty<T> listProperty() {
    return this.records;
  }

  /**
   *
   * @param records
   */
  protected void setRecords(List<T> records) {
    this.records.setValue(FXCollections.observableArrayList(records));
  }

  /**
   *
   * @param <E>
   * @param items
   * @param change
   * @param converter
   */
  private <E> void setValues(ObservableList<E> items, ObservableList<T> change, Converter<T, E> converter) {
    if (items == null) {
      throw new NullPointerException("Items cannot be null.");
    }
    if (change == null) {
      throw new NullPointerException("Change list cannot be null.");
    }
    if (converter == null) {
      throw new NullPointerException("converter cannot be null.");
    }
    items.clear();
    List<E> mapped = change.stream().map((c) -> {
      return converter.convert(c);
    }).collect(Collectors.toList());
    items.addAll(mapped);
  }

}
