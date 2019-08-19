package com.rm.springjavafx._00.ignore.tree.contextmenus;

import com.rm.springjavafx.contextmenu.AbstractContextMenuItem;
import com.rm.springjavafx.contextmenu.FxContextMenuItem;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Lazy(false)
@FxContextMenuItem(
  contextMenuRef = "office.departmentcontextmenu",
  label = "Add New Staff"
)
public class AddStaffToDepartment extends AbstractContextMenuItem {

  @Override
  protected void onAction(ActionEvent evt, MenuItem menuItem, Object userData) {
    Object s = evt.getSource();
    evt.getTarget();
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.show();
  }
}
