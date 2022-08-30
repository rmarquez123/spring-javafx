package com.rm.springjavafx.form;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import common.bindings.RmBindings;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

/**
 *
 * @author Ricardo Marquez
 */
public class Form extends VBox {

  private final TableView<FormItem> tableView;
  private final StringProperty labelProperty = new SimpleStringProperty("");
  private final ObservableList<FormGroup> formGroups = FXCollections.observableArrayList();
  private final Map<Object, ListChangeListenerImpl> itemsListener = new HashMap<>();

  /**
   *
   */
  public Form() {

    this.tableView = new TableView<>();
    this.tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    tableView.skinProperty().addListener((a, b, newSkin) -> {
      TableHeaderRow header = (TableHeaderRow) tableView.lookup("TableHeaderRow");
      header.setMinHeight(0);
      header.setPrefHeight(0);
      header.setMaxHeight(0);
      header.setVisible(false);
    });

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
                      RmBindings.bind1To2(a.textProperty(), item.labelProperty());
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
    fieldNameCol.setCellValueFactory((param) -> {
      StringProperty cellLabelProperty = param.getValue().labelProperty();
      return cellLabelProperty;
    });
    this.tableView.getColumns().add(fieldNameCol);

    TableColumn<FormItem, String> valueCol = new TableColumn<>();
    valueCol.setCellValueFactory((param) -> {
      FormItem formItem = param.getValue();
      StringConverter stringConverter = formItem.stringConverter();
      SimpleStringProperty wrapper;
      if (stringConverter != null) {
        Object formValue = formItem.valueProperty().getValue();

        String stringValue;
        try {
          stringValue = stringConverter.toString(formValue);
        } catch (Exception ex) {
          throw new RuntimeException(
            String.format("Could not convert '%s' value to string",
              formItem.labelProperty().get()));
        }
        wrapper = new SimpleStringProperty(stringValue);
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
      this.removeScrollBar();
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
  private TableCell<FormItem, String> cell() {
    ObservableList<FormItem> is = tableView.getItems();
    TableView<FormItem> t = tableView;
    TableCell<FormItem, String> result = new CustomTableCell(is, t);
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
          int parentIndex = this.tableView.getItems().size();
          if (!this.itemsListener.containsKey(gitem)) {
            ListChangeListenerImpl listener = new ListChangeListenerImpl(this.tableView, parentIndex);
            this.itemsListener.put(gitem, listener);
          }
          ListChangeListenerImpl listener = this.itemsListener.get(gitem);
          listener.addAll(items);
          formGroup.getItems().addListener(listener);
        }
      } else if (c.wasRemoved()) {
        List<? extends FormGroup> rem = c.getRemoved();
        for (FormGroup formGroup : rem) {
          ListChangeListenerImpl listener = this.itemsListener.get(formGroup);
          formGroup.getItems().removeListener(listener);
          listener.removeAll(formGroup.getItems());
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

    // this would be the preferred solution, but it doesn't work. 
    // it always gives back the vertical scrollbar
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

  private static class CustomTableCell extends TableCell<FormItem, String> {

    private final ObservableList<FormItem> is;

    private final TableView<FormItem> tableView;
    private final TextField textField;

    public CustomTableCell(ObservableList<FormItem> formItems, TableView<FormItem> t) {
      this.is = formItems;
      this.tableView = t;
      this.textField = new TextField();
      this.textField.setManaged(false);
      this.textField.setVisible(false);

      this.textField.setOnKeyPressed((evt) -> {
        if (evt.getCode() == KeyCode.ENTER || evt.getCode() == KeyCode.TAB) {
          Platform.runLater(() -> {
            this.commitEdit(this.textField.getText());
          });
        }
      });
      this.textField.focusedProperty().addListener((obs, old, change) -> {
        if (change == null) {
          this.cancelEdit();
        }
      });
    }

    @Override
    public void updateItem(String item, boolean empty) {
      super.updateItem(item, empty);
      if (item != null) {
        int index = this.getIndex();
        FormItem formItem = is.get(index);
        if (!this.isEditing()) {
          this.textField.setText(item);
        } else {
          this.textField.setText("");
        }
        super.setText(item);
        Button button = (Button) formItem.buttonProperty().getValue();
        if (button != null) {
          Pane glue = new Pane();
          HBox hbox = new HBox();
          Label label = new Label();
          hbox.getChildren().addAll(label, glue, button);
          HBox.setHgrow(glue, Priority.ALWAYS);
          HBox.setHgrow(label, Priority.ALWAYS);
          hbox.setMaxWidth(Double.MAX_VALUE);
          label.setMinWidth(USE_PREF_SIZE);
          label.setPrefWidth(USE_COMPUTED_SIZE);
          super.setGraphic(hbox);
          super.textProperty().addListener((obs, old, change) -> {
            label.setText(change);
          });
          label.setPadding(new Insets(0, 0, 0, 4));
          super.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
          label.setText(this.getText());
        } else {

          HBox hbox = new HBox();
          hbox.getChildren().addAll(textField);
          HBox.setHgrow(this.textField, Priority.ALWAYS);
          super.setGraphic(hbox);
        }
      }
    }

    @Override
    public void startEdit() {
      FormItem formItem = is.get(this.getIndex());
      if (formItem.editableProperty().getValue()) {
        super.startEdit();

        super.setText("");
        this.textField.setManaged(true);
        this.textField.setVisible(true);
        this.textField.requestFocus();
        Platform.runLater(() -> {
          String text = this.textField.getText();
          if (text != null) {
            int length = text.length();
            this.textField.positionCaret(length);
          }
        });

      }
    }

    @Override
    public void commitEdit(String newValue) {
      this.setText(newValue);
      this.textField.setManaged(false);
      this.textField.setVisible(false);
      super.commitEdit(newValue);
      Platform.runLater(() -> {
        this.tableView.requestFocus();
      });
    }

    @Override
    public void cancelEdit() {
      this.textField.setManaged(false);
      this.textField.setVisible(false);
      super.cancelEdit();
      FormItem formItem = is.get(this.getIndex());
      String text = formItem.stringConverter().toString(formItem.valueProperty().get());
      this.setText(text);
      Platform.runLater(() -> {
        tableView.requestFocus();
      });
    }

  }

  private static class ListChangeListenerImpl implements ListChangeListener<FormItem> {

    private final TableView tableView;

    private final int parentIndex;

    /**
     *
     * @param tableView
     * @param parentIndex
     */
    public ListChangeListenerImpl(TableView tableView, int parentIndex) {
      this.tableView = tableView;
      this.parentIndex = parentIndex;
    }

    /**
     *
     * @param c
     */
    @Override
    public void onChanged(Change<? extends FormItem> c) {
      if (c.next()) {
        if (c.wasAdded()) {
          List<? extends FormItem> items = c.getAddedSubList();
          this.addAll(items);
        } else if (c.wasRemoved()) {
          List<? extends FormItem> items = c.getRemoved();
          this.removeAll(items);
        }
      }
    }

    void removeAll(List<? extends FormItem> items) {
      this.tableView.getItems().removeAll(items);
    }

    void addAll(List<? extends FormItem> items) {
      this.tableView.getItems().addAll(parentIndex, items);
    }
  }
}
