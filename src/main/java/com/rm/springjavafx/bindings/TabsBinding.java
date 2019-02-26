package com.rm.springjavafx.bindings;

import com.rm.springjavafx.SpringFxUtils;
import com.rm.springjavafx.converters.Converter;
import com.rm.springjavafx.properties.ElementSelectableListProperty;
import com.rm.springjavafx.properties.ListItem;
import com.rm.springjavafx.FxmlInitializer;
import java.util.List;
import java.util.Objects;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
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

  @Required
  public void setId(String id) {
    this.id = id;
  }

  @Required
  public void setFxml(String fxml) {
    this.fxml = fxml;
  }

  @Required
  public void setFxmlId(String fxmlId) {
    this.fxmlId = fxmlId;
  }

  @Required
  public void setListRef(ElementSelectableListProperty<ListItem> listRef) {
    this.listRef = listRef;
  }

  @Required
  public void setTabs(List<TabItem> tabs) {
    this.tabs = new ElementSelectableListProperty<>();
    this.tabs.setListItems(tabs);
  }

  @Override
  public String toString() {
    return "TabsBinding{" + "id=" + id + ", fxml=" + fxml + ", fxmlId=" + fxmlId + ", listRef=" + listRef + ", tabs=" + tabs + '}';
  }

  /**
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    if (!this.fxmlInitializer.isInitialized()) {
      this.fxmlInitializer.initializeRoots(context);
    }
    for (ListItem listItem : this.listRef.getListProperty().getValue()) {
      String fxml = String.valueOf(listItem.getValue("fxml"));
      Parent r = this.fxmlInitializer.getRoot(fxml);
      if (r == null) {
        throw new NullPointerException("list item fxml does not exist.  Check args : {"
                + "listItem =" + listItem
                + ", fxml = " + fxml
                + "}"); 
      }
    }
    
    ConverterImpl converter = new ConverterImpl();
    ElementSelectableListProperty.bind(tabs.getSelection(), listRef.getSelection(), converter);
    for (TabItem tabItem : tabs.getListProperty().getValue()) {
      ListItem c = converter.convert(tabItem); 
      if (c == null) {
        throw new IllegalStateException("Tab item with selection id : '" + tabItem.getSelectionId() + "' does not have an association to the model."); 
      }
      tabItem.bindToSelectionModel(tabs.getSelection()); 
      tabItem.setOnAction(() -> {
        tabs.getSelection().select(tabItem);
      });
    }
    listRef.getSelection().selectedItemProperty().addListener((obs, old, change) -> {
      this.setNodeOnRefPane(change);
    });
    this.setNodeOnRefPane(listRef.getSelection().getSelectedItem());
  }

  /**
   *
   * @param change
   */
  private void setNodeOnRefPane(ListItem change) {
    Pane refPane;
    try {
      refPane = (Pane) fxmlInitializer.getNode(fxml, fxmlId);
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
    Object newNodeFxml = change.getValue("fxml");
    Parent newNode = fxmlInitializer.getRoot(String.valueOf(newNodeFxml));
    if (newNode == null) {
      throw new NullPointerException("Node doe not exist for fxml : '" + newNodeFxml + "'");
    }
    SpringFxUtils.setNodeOnRefPane(refPane, newNode);    
    System.out.println("Showing node: '" + newNodeFxml + "'");
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
