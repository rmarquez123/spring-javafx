package com.rm.springjavafx.contextmenu;

import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;

/**
 *
 * @author Ricardo Marquez
 */
public abstract class AbstractContextMenuItem {

  /**
   *
   */
  protected AbstractContextMenuItem() {
  }

  /**
   *
   * @return
   */
  public MenuItem getMenuItem(String label) {
    return new MenuItem(label);
  }

  /**
   *
   */
  public final void invokeAction(ActionEvent evt) {
    MenuItem source = (MenuItem) evt.getSource();
    this.onAction(evt, source, source.getUserData());
  }

  /**
   *
   */
  protected void onAction(ActionEvent evt, MenuItem menuItem, Object userData) {
    
  }

  /**
   *
   * @param userData
   * @return
   */
  public Object convertUserData(Object userData) {
    return userData;
  }

}
