package com.rm.springjavafx.annotations;

import com.rm.springjavafx.AnnotationHandler;
import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.contextmenu.AbstractContextMenuItem;
import com.rm.springjavafx.contextmenu.FxContextMenuItem;
import com.rm.springjavafx.popup.Popup;
import java.util.Map;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.stage.Window;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Lazy(false)
public class ContextMenuItemAnnotationHandler implements AnnotationHandler, InitializingBean {

  @Autowired
  private FxmlInitializer fxmlInitializer;
  @Autowired
  private ApplicationContext applicationContext;

  /**
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    this.fxmlInitializer.addAnnotationHandler(this);
  }

  /**
   *
   */
  @Override
  public void readyFxmls() {

  }

  /**
   *
   */
  @Override
  public void setNodes() {
    Map<String, Object> beansMap = this.applicationContext.getBeansWithAnnotation(FxContextMenuItem.class);
    for (Map.Entry<String, Object> entry : beansMap.entrySet()) {
      String beanid = entry.getKey();
      Object bean = entry.getValue();
      if (!(bean instanceof AbstractContextMenuItem)) {
        throw new IllegalStateException(
          String.format("bean '%s' is not an instance of '%s'",
            beanid, AbstractContextMenuItem.class.getName())
        );
      }
      if (bean == null) {
        throw new NullPointerException();
      }
      AbstractContextMenuItem menuItemObj = (AbstractContextMenuItem) bean;
      FxContextMenuItem conf = bean.getClass().getDeclaredAnnotation(FxContextMenuItem.class);
      ContextMenu contextMenu = (ContextMenu) this.applicationContext.getBean(conf.contextMenuRef());
      MenuItem menuItem = menuItemObj.getMenuItem(conf.label());
      contextMenu.getItems().add(menuItem);
      if (!conf.popupId().isEmpty()) {
        this.fxmlInitializer.addListener((i) -> {
          menuItem.setOnAction((evt) -> {
            Popup popup = (Popup) this.applicationContext.getBean(conf.popupId());
            Window window = this.fxmlInitializer.getMainRoot().getScene().getWindow();
            popup.windowProperty().setValue(window);
            Object userData = menuItemObj.convertUserData(((MenuItem) evt.getSource()).getUserData()); 
            popup.setUserData(userData);
            popup.show();
          });
        });
      } else {
        menuItem.setOnAction((evt) -> {
          menuItemObj.invokeAction(evt);
        });
      }
    }
  }

}
