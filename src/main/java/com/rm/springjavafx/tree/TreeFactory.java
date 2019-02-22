package com.rm.springjavafx.tree;

import com.rm.datasources.RecordValue;
import com.rm.springjavafx.FxmlInitializer;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javafx.beans.property.ListProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author rmarquez
 */
public class TreeFactory implements FactoryBean<TreeView>, InitializingBean, ApplicationContextAware {

  @Autowired
  FxmlInitializer fxmlInitializer;

  private String id;
  private String fxml;
  private String fxmlId;
  private TreeModel treeModel;
  private List<LevelCellFactory> cellFactories = new ArrayList<>();

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
  public void setTreeModel(TreeModel treeModel) {
    this.treeModel = treeModel;
  }

  public void setCellFactories(List<LevelCellFactory> cellFactories) {
    this.cellFactories = cellFactories;
  }

  @Override
  public Class<?> getObjectType() {
    return TreeView.class;
  }

  @Override
  public TreeView getObject() throws Exception {
    TreeView<Object> result = (TreeView) this.fxmlInitializer.getNode(fxml, fxmlId);
    TreeItem<Object> rootItem = new TreeItem<>("Root");
    result.showRootProperty().setValue(false);
    result.setRoot(rootItem);
    Map<Integer, LevelCellFactory> cellFactoriesMap = new HashMap<>();
    for (LevelCellFactory cellFactory : cellFactories) {
      cellFactoriesMap.put(cellFactory.getLevel(), cellFactory);
    }
    result.setCellFactory((param) -> new TreeCell<Object>() {
      @Override
      protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
          super.setGraphic(null);
          super.setText(null);
        } else {
          if (item instanceof TreeNode) {
            TreeNode<RecordValue> treeNode = (TreeNode<RecordValue>) item;
            int level = treeNode.getLevel();
            String textField = cellFactoriesMap.get(level).getTextField();
            String textVal = String.valueOf(treeNode.getValueObject().get(textField));
            super.setText(textVal);
          } else {
            super.setText(String.valueOf(item));
          }
        }
      }
    });
    for (int level = 0; level < this.treeModel.getNumberOfLevels(); level++) {
      ListProperty listProperty = this.treeModel.getNodes(level);
      listProperty.addListener((obs, old, change) -> {
        result.getRoot().getChildren().clear();
        this.addTreeItems(rootItem);
      });

    }

    this.treeModel.getSingleSelectionProperty().addListener((obs, old, change) -> {
      if (change != null) {
        TreeItem<Object> selection = findNode(rootItem, change);
        if (!Objects.equals(result.getSelectionModel().getSelectedItem(), selection)) {
          result.getSelectionModel().select(selection);
          int row = result.getRow(selection);
          VirtualFlow virtualFlow = (VirtualFlow) result.getChildrenUnmodifiable().get(0);
          int first = virtualFlow.getFirstVisibleCell().getIndex();
          int last = virtualFlow.getLastVisibleCell().getIndex();
          if (!(first <= row && row <= last)) {
            result.scrollTo(row);
          }
        }
      } else {
        if (!result.getSelectionModel().getSelectedIndices().isEmpty()) {
          //result.getSelectionModel().select(null);
        }
      }
    });

    this.addTreeItems(rootItem);
    if (this.treeModel.getSelectionMode() == SelectionMode.SINGLE) {
      result.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends TreeItem<Object>> c) -> {
        while (c.next()) {
          if (c.wasAdded()) {
            for (TreeItem<Object> treeItem : c.getAddedSubList()) {
              TreeNode<?> value = (TreeNode<?>) treeItem.getValue();
              treeModel.getSingleSelectionProperty().setValue(value.getValueObject());
            }
          } else if (c.wasRemoved()) {
            treeModel.getSingleSelectionProperty().setValue(null);
          }
        }
      });
    }

    return result;
  }

  /**
   *
   * @param level
   * @param parentTreeItem
   */
  private void addTreeItems(TreeItem<Object> parentTreeItem) {
    List<TreeNode> childNodes = this.getChildNodes(parentTreeItem);
    for (TreeNode node : childNodes) {
      TreeItem<Object> treeItem = new TreeItem<>();
      treeItem.setValue(node);
      parentTreeItem.getChildren().add(treeItem);
      this.addTreeItems(treeItem);
    }
  }

  private List<TreeNode> getChildNodes(TreeItem<Object> rootItem) {
    Object val = rootItem.getValue();
    List<TreeNode> result;
    if (val instanceof TreeNode) {
      result = this.treeModel.getNodes((TreeNode) val);
    } else {
      int level = 0;
      ListProperty nodesProperty = this.treeModel.getNodes(level);
      result = nodesProperty.getValue();
    }
    return result;
  }

  /**
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    this.fxmlInitializer.addListener((i) -> {
      this.context.getBean(this.id);
    });
  }

  @Override
  public void setApplicationContext(ApplicationContext ac) throws BeansException {
    this.context = ac;
  }

  /**
   *
   * @param root
   * @param value
   * @return
   */
  private static TreeItem<Object> findNode(TreeItem<Object> root, Object value) {
    TreeItem<Object> result = null;
    for (TreeItem<Object> child : root.getChildren()) {
      if (((TreeNode) child.getValue()).getValueObject().equals(value)) {
        result = child;
        break;
      } else {
        if (!child.getChildren().isEmpty()) {
          result = findNode(child, value);
          if (result != null) {
            break;
          }
        }
      }
    }
    return result;
  }
}
