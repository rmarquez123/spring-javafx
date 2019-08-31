package com.rm.springjavafx.tree;

import common.db.RecordValue;
import javafx.scene.control.ContextMenu;

/**
 *
 * @author Ricardo Marquez
 */
public interface ContextMenuProvider {

  /**
   * 
   * @param object
   * @return 
   */
  ContextMenu getContextMenu(RecordValue object);
  
}
