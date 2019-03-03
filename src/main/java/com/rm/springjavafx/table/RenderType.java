package com.rm.springjavafx.table;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 *
 * @author Ricardo Marquez
 */
public interface RenderType {

  /**
   * 
   * @param result
   * @param columnDef 
   */
  public void createCellFactory(TableView<?> result, TableColumn<Object, ?> column);
}
