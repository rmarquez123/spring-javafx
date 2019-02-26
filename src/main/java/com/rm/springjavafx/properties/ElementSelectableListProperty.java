package com.rm.springjavafx.properties;

import com.rm.springjavafx.converters.Converter;
import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.SingleSelectionModel;

/**
 *
 * @author rmarquez
 */
public class ElementSelectableListProperty<E> {

  private final ListProperty<E> listProperty = new SimpleListProperty<>();
  private final SingleSelectionModel<E> selection = new SingleSelectionModel<E>() {
    @Override
    protected E getModelItem(int index) {
      return listProperty.getValue().get(index);
    }

    @Override
    protected int getItemCount() {
      return listProperty.getValue().size();
    }
  };

  public ListProperty<E> getListProperty() {
    return listProperty;
  }

  public SingleSelectionModel<E> getSelection() {
    return selection;
  }

  /**
   *
   */
  public ElementSelectableListProperty() {
    this.listProperty.addListener((observable, oldValue, newValue) -> {
      if (newValue.size() > 0 && !newValue.contains(this.selection.getSelectedItem())) {
        selection.select(newValue.get(0));
      } else {
        selection.select(null);
      }
    });
  }

  /**
   *
   * @param listProperty
   */
  public void setListItems(List<E> listProperty) {
    this.listProperty.setValue(FXCollections.observableList(listProperty));
  }

  /**
   *
   * @param <E1>
   * @param <E2>
   * @param items1
   * @param items2
   * @param converter
   */
  public static <E1, E2> void bind(SingleSelectionModel<E1> items1, SingleSelectionModel<E2> items2, Converter<E1, E2> converter) {
    items1.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      E2 result = converter.convert(newValue);
      items2.select(result);
    });
    items2.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      E1 result = converter.deconvert(newValue);
      items1.select(result);
    });
  }

}
