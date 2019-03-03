package com.rm.springjavafx.table.renderers;

import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.ToggleButton;
import javafx.util.Callback;

public class ActionToggleButtonTableCell<S> extends TableCell<S, Object> {

  private final ToggleButton actionButton;
  private final String label;
  private final String secondaryLabel;
  
  /**
   * 
   * @param label
   * @param secondaryLabel
   * @param function 
   */
  public ActionToggleButtonTableCell(String label, String secondaryLabel, Handler<S> function) {
    this.getStyleClass().add("action-toggle-button-table-cell");
    this.actionButton = new ToggleButton(label);
    this.label = label;
    this.secondaryLabel = secondaryLabel;
    this.actionButton.setOnAction((ActionEvent e) -> {
      S currentItem = getCurrentItem();
      function.handle(this.actionButton.isSelected(), currentItem);
    });
    this.actionButton.setMaxWidth(Double.MAX_VALUE);
    this.actionButton.setMaxHeight(Double.MAX_VALUE);
  }

  /**
   *
   * @return
   */
  public BooleanProperty toggledOnProperty() {
    return this.actionButton.selectedProperty();
  }

  /**
   *
   * @return
   */
  public S getCurrentItem() {
    return (S) getTableView().getItems().get(getIndex());
  }

  /**
   *
   * @param <S>
   * @param label
   * @param function
   * @return
   */
  public static <S> Callback<TableColumn<S, Object>, TableCell<S, Object>>
    forTableColumn(String label, String secondaryLabel, Handler<S> function) {
    Callback<TableColumn<S, Object>, TableCell<S, Object>> callBack = param -> {

      ActionToggleButtonTableCell<S> tableCell 
        = new ActionToggleButtonTableCell<>(label, secondaryLabel, function);
      return tableCell;
    };
    return callBack;
  }

  /**
   *
   * @param item
   * @param empty
   */
  @Override
  public void updateItem(Object item, boolean empty) {
    super.updateItem(item, empty);
    if (empty) {
      setGraphic(null);
    } else {
      if (item instanceof Boolean) {
        actionButton.setSelected((Boolean) item);
        if (this.secondaryLabel != null && (Boolean) item == false) {
          actionButton.setText(this.secondaryLabel);
        } else {
          actionButton.setText(this.label);
        }
      }
      setGraphic(actionButton);
    }
  }

  public static interface Handler<S> {

    /**
     *
     * @param selected
     * @param item
     */
    void handle(boolean selected, S item);
  }

}
