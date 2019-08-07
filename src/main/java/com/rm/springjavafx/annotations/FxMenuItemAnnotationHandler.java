package com.rm.springjavafx.annotations;

import com.rm.springjavafx.AnnotationHandler;
import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.SpringFxUtils;
import com.rm.springjavafx.menu.AbstractFxMenuItem;
import com.rm.springjavafx.menu.FxMenuItem;
import com.rm.springjavafx.menu.FxMenuItemImpl;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeansException;
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
public class FxMenuItemAnnotationHandler implements InitializingBean, AnnotationHandler {

  @Autowired
  private FxmlInitializer fxmlInitializer;
  @Autowired
  private ApplicationContext appContext;

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
    Map<String, Object> items = appContext.getBeansWithAnnotation(FxMenuItem.class);
    Set<FxMenuItem> menuItems = new HashSet<>();
    
    for (Object value : items.values()) {
      if (value instanceof AbstractFxMenuItem) {
        FxMenuItem fxMenuItem = FxMenuItemImpl.wrap(value.getClass().getDeclaredAnnotation(FxMenuItem.class));
        if (!menuItems.add(fxMenuItem)) {
          throw new IllegalStateException("Duplicate menu item configuration is not allowed: '{"
            + "fxml = "  + fxMenuItem.fxml()
            + "id = "  + fxMenuItem.id()
            + "}'"); 
        }
      }
      
    }
    
    
    for (Object value : items.values()) {
      if (value instanceof AbstractFxMenuItem) {
        Class<? extends Object> clazz = value.getClass();
        FxMenuItem fxMenuItem = clazz.getDeclaredAnnotation(FxMenuItem.class);
        String fxml = fxMenuItem.fxml();
        this.addFxml(fxml);
      } else {
        throw new IllegalArgumentException("Bean with FxMenuItem should also extend '"
          + AbstractFxMenuItem.class + "'");
      }
    }
  }

  /**
   *
   * @throws BeansException
   * @throws RuntimeException
   */
  @Override
  public void setNodes() {
    Map<String, Object> tabItemBeans = this.appContext.getBeansWithAnnotation(FxMenuItem.class);
    for (Object value : tabItemBeans.values()) {
      
      if (value == null) {
        throw new NullPointerException("tableItemBean cannot be null.");
      }
      
      if (!(value instanceof AbstractFxMenuItem)) {
        throw new IllegalStateException(
          String.format("Class '%s' must extend %s", 
            value.getClass().getSimpleName(), 
            AbstractFxMenuItem.class.getSimpleName()));
      }
      
      Class<? extends Object> clazz = value.getClass();
      FxMenuItem fxMenuItem = clazz.getDeclaredAnnotation(FxMenuItem.class);
      String fxml = fxMenuItem.fxml();
      String id = fxMenuItem.id();
      Parent node = this.fxmlInitializer.getRoot(fxml);
      MenuItem menuItem = SpringFxUtils.getChildByID(node, id);
      if (menuItem == null) {
        throw new IllegalStateException("Menu item not found in node.  Check args: {"
          + "fxml = "  + fxml
          + ", id = "  + id
          + "}"); 
      }
      ((AbstractFxMenuItem) value).init(fxmlInitializer, menuItem);
    }
  }

  /**
   *
   * @param fxml
   * @throws RuntimeException
   * @throws IllegalStateException
   */
  private void addFxml(String fxml) {
    if (this.getClass().getClassLoader().getResource(fxml) == null) {
      throw new IllegalStateException("Fxml does not exist: '" + fxml + "'");
    }
    if (!FilenameUtils.getExtension(fxml).endsWith("fxml")) {
      throw new RuntimeException("File does not have .fxml extension: '" + fxml + "'");
    }
    this.fxmlInitializer.addFxml(fxml);
  }
}
