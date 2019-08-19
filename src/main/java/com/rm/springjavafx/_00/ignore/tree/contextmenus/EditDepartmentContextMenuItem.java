package com.rm.springjavafx._00.ignore.tree.contextmenus;

import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.contextmenu.AbstractContextMenuItem;
import com.rm.springjavafx.contextmenu.FxContextMenuItem;
import org.springframework.beans.factory.annotation.Autowired;
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
  label = "Edit Department", 
  popupId = "office.editdepartments.popup"
)
public class EditDepartmentContextMenuItem extends AbstractContextMenuItem{
  
  @Autowired
  private FxmlInitializer fxmlInitializer;
}
