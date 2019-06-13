package com.rm.springjavafx.tabs;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 *
 * @author Ricardo Marquez
 */
public abstract class AbstractTabItem {

  private Tab tab;

  /**
   *
   * @param tab
   */
  public final void setTab(Tab tab) {
    this.tab = tab;
    TabPane tabPane = this.tab.getTabPane();
    this.visibleProperty().addListener((obs, old, change) -> {
      this.updateTabVisibility(tabPane);
    });
    this.focusedProperty().addListener((obs, old, change) -> {
      this.updateTabFocused(tabPane);
    });
    this.tab.tabPaneProperty().addListener((obs, old, change) -> {
      this.visibleProperty().setValue(change != null);
    });

    this.tab.selectedProperty().addListener((obs, old, change) -> {
      this.focusedProperty().setValue(change);
    });
    this.updateTabFocused(tabPane);
  }

  /**
   *
   * @param tabPane
   */
  private void updateTabVisibility(TabPane tabPane) {

    if (this.visibleProperty().getValue()) {
      if (!tabPane.getTabs().contains(this.tab)) {
        tabPane.getTabs().add(this.tab);

      }
    } else {
      tabPane.getTabs().remove(this.tab);
    }
  }

  /**
   *
   * @param tabPane
   */
  private void updateTabFocused(TabPane tabPane) {
    this.tab.setClosable(true);
    tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
    Boolean focused = this.focusedProperty().getValue();
    if (focused) {
      Platform.runLater(() -> {
        tabPane.getSelectionModel().select(this.tab);
      });

    }
  }

  /**
   *
   * @return
   */
  protected abstract BooleanProperty visibleProperty();

  /**
   *
   * @return
   */
  protected abstract BooleanProperty focusedProperty();

}
