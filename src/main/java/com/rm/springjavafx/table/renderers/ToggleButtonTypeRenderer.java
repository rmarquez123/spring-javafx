package com.rm.springjavafx.table.renderers;

import common.db.RecordValue;
import com.rm.springjavafx.table.RenderType;
import com.rm.springjavafx.table.TableViewColumn;
import com.rm.springjavafx.table.renderers.ActionToggleButtonTableCell.Handler;
import java.util.Objects;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 *
 * @author Ricardo Marquez
 */
public class ToggleButtonTypeRenderer implements RenderType {

  private final Action action;
  private final String label;
  private final String secondaryLabel;

  /**
   *
   * @param action
   * @param label
   * @param secondaryLabel
   */
  public ToggleButtonTypeRenderer(Action action, String label, String secondaryLabel) {
    Objects.requireNonNull(action, "Action cannot be null");
    this.action = action;
    this.label = label;
    this.secondaryLabel = secondaryLabel;
  }

  /**
   *
   * @param result
   * @param column
   */
  @Override
  public void createCellFactory(TableView<?> result, TableColumn<Object, ?> column) {
    
    Callback forTableColumn = ActionToggleButtonTableCell
      .forTableColumn(this.label, this.secondaryLabel, new Handler() {
      @Override
      public void handle(boolean selected, Object entity) {
        
        TableViewColumn columnDef = (TableViewColumn) column.getUserData();
        Object value = ((RecordValue) entity).get(columnDef.getPropertyName());
        if (value instanceof BooleanProperty) {
          ((BooleanProperty) value).setValue(selected);
        }
        action.onAction(entity);
      }
    });
    column.setCellFactory(forTableColumn);
  }

}
