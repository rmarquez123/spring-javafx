package com.windsim.wpls.view.menu;

import com.rm.springjavafx.FxmlInitializer;
import com.windsim.wpls.view.projectopen.RequestProjectOpenAction;
import gov.inl.glass3.linesolver.Model;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class MenuBarController implements InitializingBean {

  @Autowired
  private FxmlInitializer initalizer;
  @Autowired
  @Qualifier(value = "glassModelProperty")
  private Property<Model> modelProperty;

  @Autowired
  @Qualifier("requestProjectOpenAction")
  Property<RequestProjectOpenAction> requestProjectOpenAction;

  /**
   *
   * @param initalizer
   */
  public void setInitalizer(FxmlInitializer initalizer) {
    this.initalizer = initalizer;
  }

  public void setModelProperty(Property<Model> modelProperty) {
    this.modelProperty = modelProperty;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.initalizer.addListener((a) -> {
      Platform.runLater(() -> {
        initializeMenus();
      });
    });
  }

  /**
   *
   * @throws RuntimeException
   */
  private void initializeMenus() throws RuntimeException {
    MenuBar menuBar;
    try {
      menuBar = (MenuBar) this.initalizer.getNode("fxml/Main.fxml", "menuBar");
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }

    ObservableList<Menu> menus = menuBar.getMenus();
    Menu fileMenu = menus.get(0);
    ObservableList<MenuItem> fileMenuItems = fileMenu.getItems();
    fileMenuItems.clear();
    addOpenModelMenuItem(menuBar, fileMenuItems);
    addCloseModelMenuItem(menuBar, fileMenuItems);

  }

  /**
   *
   * @param menuBar
   * @param fileMenuItems
   */
  private void addOpenModelMenuItem(MenuBar menuBar, ObservableList<MenuItem> fileMenuItems) {
    MenuItem menuItem = new MenuItem("Open Glass Model (.glass file)");
    menuItem.setOnAction((a) -> {
      requestOpenModel(menuBar);
    });
    modelProperty.addListener((obs, old, change) -> {
      menuItem.disableProperty().setValue(change != null);
    });
    menuItem.disableProperty().setValue(modelProperty.getValue() != null);
    fileMenuItems.add(menuItem);
  }

  private void addCloseModelMenuItem(MenuBar menuBar, ObservableList<MenuItem> fileMenuItems) {
    MenuItem menuItem = new MenuItem("Close current model");
    menuItem.setOnAction((a) -> {
      Model model = modelProperty.getValue();
      modelProperty.setValue(null);
    });
    fileMenuItems.add(menuItem);
  }

  /**
   *
   */
  private void requestOpenModel(MenuBar menuBar) {
    requestProjectOpenAction.getValue().onAction((m) -> {
    });
  }

}
