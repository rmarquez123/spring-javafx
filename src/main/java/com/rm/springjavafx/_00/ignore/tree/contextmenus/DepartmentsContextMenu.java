package com.rm.springjavafx._00.ignore.tree.contextmenus;

import common.db.RecordValue;
import com.rm.springjavafx.tree.ContextMenuProvider;
import javafx.scene.control.ContextMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component("office.departmentcontextmenu.provider")
public class DepartmentsContextMenu implements ContextMenuProvider {

  @Autowired
  @Qualifier("office.departmentcontextmenu")
  private ContextMenu contextMenu;

  /**
   *
   * @param object
   * @return
   */
  @Override
  public ContextMenu getContextMenu(RecordValue object) {
    return this.contextMenu;
  }

}
