package com.rm.springjavafx.bindings;

import com.rm.springjavafx.converters.Converter;
import com.rm.springjavafx.properties.ElementSelectableListProperty;
import com.rm.springjavafx.properties.ListItem;
import com.rm.testrmfxmap.javafx.FxmlInitializer;
import java.util.List;
import java.util.Objects;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author rmarquez
 */
public class TabsBinding implements InitializingBean, ApplicationContextAware {

  @Autowired
  FxmlInitializer fxmlInitializer;

  private String id;
  private String fxml;
  private String fxmlId;
  private ElementSelectableListProperty<ListItem> listRef;
  private ElementSelectableListProperty<TabItem> tabs;
  private ApplicationContext context;

  public void setId(String id) {
    this.id = id;
  }

  public void setFxml(String fxml) {
    this.fxml = fxml;
  }

  public void setFxmlId(String fxmlId) {
    this.fxmlId = fxmlId;
  }

  public void setListRef(ElementSelectableListProperty<ListItem> listRef) {
    this.listRef = listRef;
  }

  public void setTabs(List<TabItem> tabs) {
    this.tabs = new ElementSelectableListProperty<>();
    this.tabs.setListItems(tabs);
  }

  @Override
  public String toString() {
    return "TabsBinding{" + "id=" + id + ", fxml=" + fxml + ", fxmlId=" + fxmlId + ", listRef=" + listRef + ", tabs=" + tabs + '}';
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    if (!this.fxmlInitializer.isInitialized()) {
      this.fxmlInitializer.initializeRoots(context);
    }
    ElementSelectableListProperty.bind(tabs.getSelection(), listRef.getSelection(), new ConverterImpl());
    for (TabItem tabItem : tabs.getListProperty().getValue()) {
      tabItem.setOnAction(() -> {
        tabs.getSelection().select(tabItem);
      });
    }
    Pane refPane = (Pane) fxmlInitializer.getNode(fxml, fxmlId);
    listRef.getSelection().selectedItemProperty().addListener((obs, old, change) -> {
      Object newNodeFxml = change.getValue(change.getId());
      Parent newNode = fxmlInitializer.getRoot(String.valueOf(newNodeFxml));
      refPane.getChildren().clear();
      refPane.getChildren().add(newNode);
    });
  }

  @Override
  public void setApplicationContext(ApplicationContext ac) throws BeansException {
    this.context = ac;
  }

  /**
   *
   */
  private class ConverterImpl implements Converter<TabItem, ListItem> {

    public ConverterImpl() {
    }

    @Override
    public ListItem convert(TabItem e) {
      ListItem result = null;
      for (ListItem listItem : listRef.getListProperty().getValue()) {
        if (Objects.equals(listItem.getId(), e.getSelectionId())) {
          result = listItem;
          break;
        }
      }
      return result;
    }

    @Override
    public TabItem deconvert(ListItem value) {
      TabItem result = null;
      for (TabItem tabItem : tabs.getListProperty().getValue()) {
        if (Objects.equals(value.getId(), tabItem.getSelectionId())) {
          result = tabItem;
          break;
        }
      }
      return result;
    }
  }

}
