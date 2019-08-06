package com.rm.springjavafx.table.renderers;

import com.rm.springjavafx.table.RenderType;
import java.util.Objects;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;

/**
 *
 * @author Ricardo Marquez
 */
public class CheckBoxTypeRenderer implements RenderType {

  private final String label;
  private final SimpleBooleanProperty observable;

  public CheckBoxTypeRenderer(String label) {
    Objects.requireNonNull(label, "label cannot be null");
    this.label = label;
    this.observable = new SimpleBooleanProperty();
  }

  /**
   * 
   * @param result
   * @param column 
   */
  @Override
  public void createCellFactory(TableView<?> result, TableColumn<Object, ?> column) {
    Callback<TableColumn<Object, Boolean>, TableCell<Object, Boolean>> a = CheckBoxTableCell
      .forTableColumn((TableColumn<Object, Boolean>) column);
    ((TableColumn<Object, Boolean>) column).setCellFactory(a);
    column.setEditable(true);
  }
}
