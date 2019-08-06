package com.rm.springjavafx.menu;

import com.rm.springjavafx.FxmlInitializer;
import java.util.Objects;
import javafx.event.Event;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author Ricardo Marquez
 */
public abstract class AbstractFxMenuItem {

  protected FxmlInitializer fxmlInitializer;

  private MenuItem menuItem;

  /**
   *
   * @param fxmlInitializer
   * @param menuItem
   */
  public final void init(FxmlInitializer fxmlInitializer, MenuItem menuItem) {
    Objects.requireNonNull(menuItem, "Menu Item cannot be null");
    this.fxmlInitializer = fxmlInitializer;
    
    this.menuItem = menuItem;
    
    this.fxmlInitializer.addListener((i) -> {
      this.init(menuItem);
      this.menuItem.setOnAction((evt) -> {
        this.onAction(evt);
      });
      Parent mainRoot = this.fxmlInitializer.getMainRoot();
      mainRoot.addEventHandler(KeyEvent.KEY_PRESSED, (evt)->{
        FxMenuItem fxMenuItem = this.getClass().getDeclaredAnnotation(FxMenuItem.class);
        if (evt.getCode().equals(fxMenuItem.code()) 
          && fxMenuItem.modifier().test(evt) ) {
          if (!this.menuItem.isDisable()) {
            this.onAction(evt);
          }
        }
      });
    });
  }

  /**
   *
   * @param menuItem
   */
  protected abstract void init(MenuItem menuItem);

  /**
   *
   * @param evt
   */
  protected abstract void onAction(Event evt);

}
