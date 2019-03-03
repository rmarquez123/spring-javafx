package com.rm.springjavafx.table.renderers;

import com.rm.springjavafx.table.RenderType;
import java.util.Objects;
import java.util.function.Function;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 *
 * @author Ricardo Marquez
 */
public class ButtonTypeRenderer implements RenderType {

  private final Action action;
  private final String label;

  /**
   *
   * @param action
   */
  public ButtonTypeRenderer(Action action, String label) {
    Objects.requireNonNull(action, "Action cannot be null");
    this.action = action;
    this.label = label;
  }


  @Override
  public void createCellFactory(TableView<?> result, TableColumn<Object, ?> column) {
    
    column.setCellFactory(ActionButtonTableCell.forTableColumn(this.label, new Function() {
      @Override
      public Object apply(Object t) {
        action.onAction(t);
        return t;
      }
    }));
  }

}
