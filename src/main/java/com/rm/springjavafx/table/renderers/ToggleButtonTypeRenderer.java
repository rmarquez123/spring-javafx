package com.rm.springjavafx.table.renderers;

import com.rm.datasources.RecordValue;
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

  public ToggleButtonTypeRenderer(Action action, String label) {
    Objects.requireNonNull(action, "Action cannot be null");
    this.action = action;
    this.label = label;
  }

  /**
   *
   * @param result
   * @param column
   */
  @Override
  public void createCellFactory(TableView<?> result, TableColumn<Object, ?> column) {
    
    Callback forTableColumn = ActionToggleButtonTableCell.forTableColumn(this.label, new Handler() {
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
