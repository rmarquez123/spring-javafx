package com.rm.springjavafx.tree;

import com.rm.springjavafx.FxmlInitializer;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import common.db.RecordValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javafx.beans.property.ListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
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

  /**
   *
   * @param id
   */
  @Required
  public void setId(String id) {
    this.id = id;
  }

  /**
   *
   * @param fxml
   */
  @Required
  public void setFxml(String fxml) {
    this.fxml = fxml;
  }

  /**
   *
   * @param fxmlId
   */
  @Required
  public void setFxmlId(String fxmlId) {
    this.fxmlId = fxmlId;
  }

  /**
   *
   * @param treeModel
   */
  @Required
  public void setTreeModel(TreeModel treeModel) {
    this.treeModel = treeModel;
  }

  /**
   *
   * @param cellFactories
   */
  public void setCellFactories(List<LevelCellFactory> cellFactories) {
    this.cellFactories = cellFactories;
  }

  /**
   *
   * @return
   */
  @Override
  public Class<?> getObjectType() {
    return TreeView.class;
  }

  /**
   *
   * @return @throws Exception
   */
  @Override
  public TreeView getObject() throws Exception {
    TreeView<Object> result = (TreeView) this.fxmlInitializer.getNode(fxml, fxmlId);
    TreeItem<Object> rootItem = new TreeItem<>("Root");
    result.showRootProperty().setValue(false);
    result.setRoot(rootItem);
    this.setCellFactories(result);
    this.bindDataModels(result, rootItem);
    this.bindSelections(rootItem, result);
    return result;
  }

  /**
   *
   * @param result
   * @param rootItem
   */
  private void bindDataModels(TreeView<Object> result, TreeItem<Object> rootItem) {
    for (int level = 0; level < this.treeModel.getNumberOfLevels(); level++) {
      ListProperty listProperty = this.treeModel.getNodes(level);
      listProperty.addListener((obs, old, change) -> {
        result.getRoot().getChildren().clear();
        this.addTreeItems(rootItem);
      });
    }
    this.addTreeItems(rootItem);
  }

  /**
   *
   * @param result
   */
  private void setCellFactories(TreeView<Object> result) {
    Map<Integer, LevelCellFactory> cellFactoriesMap = new HashMap<>();
    for (LevelCellFactory cellFactory : this.cellFactories) {
      cellFactoriesMap.put(cellFactory.getLevel(), cellFactory);
    }
    result.setCellFactory((param) -> this.createCellFactory(cellFactoriesMap));
  }

  /**
   *
   * @param rootItem
   * @param result
   */
  private void bindSelections(TreeItem<Object> rootItem, TreeView<Object> result) {
    this.treeModel.singleSelectionProperty().addListener((obs, old, change) -> {
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

    if (this.treeModel.getSelectionMode() == SelectionMode.SINGLE) {
      result.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends TreeItem<Object>> c) -> {
        while (c.next()) {
          if (c.wasAdded()) {
            for (TreeItem<Object> treeItem : c.getAddedSubList()) {
              TreeNode<?> value = (TreeNode<?>) treeItem.getValue();
              this.treeModel.singleSelectionProperty().setValue(value.getValueObject());
            }
          } else if (c.wasRemoved()) {
            this.treeModel.singleSelectionProperty().setValue(null);
          }
        }
      });
    }
  }

  /**
   *
   * @return
   */
  private TreeCell<Object> createCellFactory(Map<Integer, LevelCellFactory> cellFactoriesMap) {
    TreeCell<Object> result = new TreeCell<Object>() {
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
            LevelCellFactory cellFactory = cellFactoriesMap.get(level);
            String textField = cellFactory.getTextField();
            String textVal = String.valueOf(treeNode.getValueObject().get(textField));
            super.setText(textVal);
            ContextMenu contextMenu = cellFactory.getContextMenu(treeNode.getObject());
            super.setContextMenu(contextMenu);
            if (cellFactory.isCheckBox()) {

              CheckBox checkBox = new CheckBox();
              checkBox.selectedProperty().addListener((obs, old, change) -> {
                ObservableList currentObs = treeModel.checkedValuesProperty().getValue();
                if (currentObs != null) {
                  if (change) {
                    List current = new ArrayList<>(currentObs);
                    if (!current.contains(treeNode.getValueObject())) {
                      current.add(treeNode.getValueObject());
                      treeModel.checkedValuesProperty()
                        .setValue(FXCollections.observableList(current));
                    }
                  } else {
                    List current = new ArrayList<>(currentObs);
                    if (current.contains(treeNode.getValueObject())) {
                      current.remove(treeNode.getValueObject());
                      treeModel.checkedValuesProperty()
                        .setValue(FXCollections.observableList(current));
                    }
                  }
                } else {
                  if (change) {
                    treeModel.checkedValuesProperty()
                      .setValue(FXCollections.observableArrayList(treeNode.getValueObject()));
                  }
                }

              });
              super.setGraphic(checkBox);
            } else {
              super.setGraphic(null);
            }
          } else {
            super.setText(String.valueOf(item));
          }

        }
      }
    };
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
