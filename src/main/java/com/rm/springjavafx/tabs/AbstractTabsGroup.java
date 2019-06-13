/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rm.springjavafx.tabs;

import com.rm.springjavafx.FxmlInitializer;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author Ricardo Marquez
 */
public abstract class AbstractTabsGroup implements InitializingBean {

  @Autowired
  protected FxmlInitializer fxmlInitializer;
  @Autowired
  protected ApplicationContext appContext;

  /**
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    this.fxmlInitializer.addListener((i) -> {
      TabsGroup a = this.getClass().getDeclaredAnnotation(TabsGroup.class);
      TabPane tabPane;
      try {
        tabPane = (TabPane) i.getNode(a.fxml(), a.nodeId());
      } catch (IllegalAccessException ex) {
        throw new RuntimeException(ex);
      }
      String[] beanNames = this.appContext.getBeanNamesForAnnotation(TabItem.class);
      for (String beanName : beanNames) {
        Object bean = this.appContext.getBean(beanName);
        TabItem tabItem = bean.getClass().getDeclaredAnnotation(TabItem.class);
        if (tabItem.tabGroupId().equals(a.tabGroupId())) {
          String fxml = tabItem.fxml();
          String label = tabItem.label();
          Parent node = this.fxmlInitializer.getRoot(fxml);
          Tab tab = new Tab(label, node);
          Tab current = tabPane.getSelectionModel().getSelectedItem();
          tabPane.getTabs().add(tab);
          if (current != null) {
            Platform.runLater(() -> {
              tabPane.getSelectionModel().select(current);
            });
          }
          ((AbstractTabItem) bean).setTab(tab);
        }
      }
    });
  }

}
