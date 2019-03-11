package com.rm.springjavafx.tree;

import com.rm.datasources.RecordValue;
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
