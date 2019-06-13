package com.rm.springjavafx.form;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import com.sun.javafx.scene.control.skin.TableViewSkinBase;
import java.util.List;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

/**
 *
 * @author Ricardo Marquez
 */
public class Form extends VBox {

  private final TableView<FormItem> tableView;
  private final StringProperty labelProperty = new SimpleStringProperty("");
  private final ObservableList<FormGroup> formGroups = FXCollections.observableArrayList();

  /**
   *
   */
  public Form() {

    this.tableView = new TableView<>();
    this.tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    tableView.skinProperty().addListener((a, b, newSkin) -> {
      TableHeaderRow header = ((TableViewSkinBase) newSkin).getTableHeaderRow();
      header.setMinHeight(0);
      header.setPrefHeight(0);
      header.setMaxHeight(0);
      header.setVisible(false);
    });
    Callback<TableView<FormItem>, TableRow<FormItem>> rowFactory = tableView.rowFactoryProperty().getValue();
    tableView.rowFactoryProperty().setValue((param) -> {
      TableRow<FormItem> original = new TableRow<FormItem>() {
        @Override
        protected void updateItem(FormItem item, boolean empty) {
          super.updateItem(item, empty);
          if (item != null) {
            if (item.stringConverter() == null) {
              super.getStyleClass().add("group-row");
              super.getChildren().addListener((ListChangeListener.Change<? extends Node> c) -> {
                if (c.next()) {
                  if (c.wasAdded()) {
                    if (c.getAddedSize() == 2) {
                      TableCell a = (TableCell) c.getAddedSubList().get(0);
                      a.setStyle("-fx-font-weight: bold");
                      a.textProperty().bind(item.labelProperty());
                    }
                  }
                }
              });
            }
          }
        }
      };

      return original;
    });

    this.tableView.setFixedCellSize(25);
    this.tableView.setEditable(true);
    this.tableView.prefHeightProperty().bind(tableView.fixedCellSizeProperty().multiply(Bindings.size(tableView.getItems()).add(.10)));
    this.tableView.minHeightProperty().bind(tableView.prefHeightProperty());
    this.tableView.maxHeightProperty().bind(tableView.prefHeightProperty());

    this.tableView.getColumns().clear();
    TableColumn<FormItem, String> fieldNameCol = new TableColumn<>();
    fieldNameCol.setCellValueFactory((param) -> param.getValue().labelProperty());
    this.tableView.getColumns().add(fieldNameCol);

    TableColumn<FormItem, String> valueCol = new TableColumn<>();
    valueCol.setCellValueFactory((param) -> {
      FormItem formItem = param.getValue();
      StringConverter stringConverter = formItem.stringConverter();
      SimpleStringProperty wrapper;
      if (stringConverter != null) {
        wrapper = new SimpleStringProperty(stringConverter.toString(formItem.valueProperty().getValue()));
        wrapper.bindBidirectional(formItem.valueProperty(), stringConverter);
      } else {
        wrapper = new SimpleStringProperty();
      }
      return wrapper;
    });

    valueCol.setCellFactory((parm) -> cell());
    this.tableView.getColumns().add(valueCol);

    Label label = new Label();
    label.setStyle("-fx-font-weight:bold");
    label.managedProperty().bind(Bindings.createBooleanBinding(() -> {
      return this.labelProperty.getValue() != null && !this.labelProperty.getValue().isEmpty();
    }, this.labelProperty));
    label.visibleProperty().bind(Bindings.createBooleanBinding(() -> {
      return this.labelProperty.getValue() != null && !this.labelProperty.getValue().isEmpty();
    }, this.labelProperty));

    label.textProperty().bind(this.labelProperty);
    super.getChildren().add(label);
    super.getChildren().add(this.tableView);
    VBox.setVgrow(this.tableView, Priority.ALWAYS);
    this.formGroups.addListener(this::onFormGroupsChanged);
    this.tableView.getItems().addListener((Change<? extends FormItem> c) -> {
      c.next();
      removeScrollBar();
    });
    
    this.tableView.getChildrenUnmodifiable().addListener((Change<? extends Node> c) -> {
      Platform.runLater(this::removeScrollBar);
    });

    this.setSpacing(5);
  }

  /**
   *
   * @return
   */
  public StringProperty labelProperty() {
    return labelProperty;
  }

  /**
   *
   * @return
   */
  private TextFieldTableCell<FormItem, String> cell() {
    ObservableList<FormItem> is = tableView.getItems();
    TableView<FormItem> t = tableView;
    TextFieldTableCell<FormItem, String> result = new TextFieldTableCell<FormItem, String>() {
      @Override
      public void updateSelected(boolean selected) {
      }

      @Override
      public void startEdit() {
        FormItem formItem = is.get(this.getIndex());
        if (formItem.editableProperty().getValue()) {
          super.startEdit();
        }
      }

      @Override
      public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        Platform.runLater(() -> t.requestFocus());
      }
    };
    result.setConverter(new DefaultStringConverter());
    return result;
  }

  /**
   *
   */
  private void removeScrollBar() {
    ScrollBar scrollbar = findScrollBar(this.tableView, Orientation.VERTICAL);
    if (scrollbar != null) {
      scrollbar.setVisible(false);
      scrollbar.setManaged(false);
    }

  }

  /**
   *
   * @param c
   */
  private void onFormGroupsChanged(ListChangeListener.Change<? extends FormGroup> c) {
    if (c.next()) {
      if (c.wasAdded()) {
        List<? extends FormGroup> added = c.getAddedSubList();
        for (FormGroup formGroup : added) {
          ObservableList<FormItem> items = formGroup.getItems();
          FormItem gitem = new FormItem(formGroup.textProperty(), null, null);
          gitem.editableProperty().set(false);
          this.tableView.getItems().add(gitem);
          for (FormItem item : items) {
            this.tableView.getItems().add(item);
          }
        }
      } else if (c.wasRemoved()) {
        List<? extends FormGroup> rem = c.getRemoved();
        for (FormGroup formGroup : rem) {

          for (FormItem item : formGroup.getItems()) {
            this.tableView.getItems().remove(item);
          }
        }
      }
    }
  }

  /**
   *
   * @return
   */
  public ObservableList<FormGroup> getFormGroups() {
    return this.formGroups;
  }

  /**
   * Find the horizontal scrollbar of the given table.
   *
   * @param table
   * @return
   */
  private ScrollBar findScrollBar(TableView<?> table, Orientation orientation) {

    // this would be the preferred solution, but it doesn't work. it always gives back the vertical scrollbar
    //      return (ScrollBar) table.lookup(".scroll-bar:horizontal");
    //      
    // => we have to search all scrollbars and return the one with the proper orientation
    Set<Node> set = table.lookupAll(".scroll-bar");
    for (Node node : set) {
      ScrollBar bar = (ScrollBar) node;
      if (bar.getOrientation() == orientation) {
        return bar;
      }
    }

    return null;

  }
}
