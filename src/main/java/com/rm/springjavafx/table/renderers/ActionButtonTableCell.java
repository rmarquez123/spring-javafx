package com.rm.springjavafx.table.renderers;

import java.util.function.Function;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ActionButtonTableCell<S> extends TableCell<S, Object> {

  private final Button actionButton;

  public ActionButtonTableCell(String label, Function< S, S> function) {
    this.getStyleClass().add("action-button-table-cell");

    this.actionButton = new Button(label);
    this.actionButton.setOnAction((ActionEvent e) -> {
      function.apply(getCurrentItem());
    });
    this.actionButton.setMaxWidth(Double.MAX_VALUE);
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
  forTableColumn(String label, Function< S, S> function) {
    Callback<TableColumn<S, Object>, TableCell<S, Object>> callBack = param -> {
      ActionButtonTableCell<S> tableCell = new ActionButtonTableCell<>(label, function);
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
      setGraphic(actionButton);
    }
  }
}
