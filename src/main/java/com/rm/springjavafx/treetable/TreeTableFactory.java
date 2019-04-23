package com.rm.springjavafx.treetable;

import com.rm.datasources.RecordValue;
import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.tree.LevelCellFactory;
import com.rm.springjavafx.tree.TreeModel;
import com.rm.springjavafx.tree.TreeNode;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
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
public class TreeTableFactory implements FactoryBean<TreeTableView>,
  InitializingBean, ApplicationContextAware {

  @Autowired
  FxmlInitializer fxmlInitializer;

  private String id;
  private String fxml;
  private String fxmlId;
  private TreeModel treeModel;
  private List<TreeTableColumnDef> cellFactories = new ArrayList<>();

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

  /**
   *
   * @param cellFactories
   */
  public void setCellFactories(List<TreeTableColumnDef> cellFactories) {
    this.cellFactories = cellFactories;
  }

  /**
   *
   * @return @throws Exception
   */
  @Override
  public TreeTableView getObject() throws Exception {
    TreeTableView result = (TreeTableView) this.fxmlInitializer.getNode(fxml, fxmlId);
    TreeItem rootItem = new TreeItem<>("Root");
    result.showRootProperty().setValue(false);
    result.setRoot(rootItem);
    bindDataModels(result, rootItem);
    this.bindSelections(rootItem, result);
    return result;
  }

  /**
   *
   * @param result
   * @param rootItem
   */
  private void bindDataModels(TreeTableView result, TreeItem rootItem) {
    this.setColumns(result);
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
   * @param rootItem
   * @param result
   */
  private void bindSelections(TreeItem<Object> rootItem, TreeTableView<Object> result) {
    this.treeModel.singleSelectionProperty().addListener((obs, old, change) -> {
      if (change != null) {
        TreeItem<Object> selection = findNode(rootItem, change);
        if (!Objects.equals(result.getSelectionModel().getSelectedItem(), selection)) {
          result.getSelectionModel().select(selection);
          int row = result.getRow(selection);
          ObservableList<Node> childrenUnmodifiable = result.getChildrenUnmodifiable();
          for (Node node : childrenUnmodifiable) {
            if (node instanceof VirtualFlow) {
              VirtualFlow virtualFlow = (VirtualFlow) node;
              IndexedCell firstVisibleCell = virtualFlow.getFirstVisibleCell();
              if (firstVisibleCell != null) {
                int first = firstVisibleCell.getIndex();
                int last = virtualFlow.getLastVisibleCell().getIndex();
                if (!(first <= row && row <= last)) {
                  result.scrollTo(row);
                }
              }
              break;
            }
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
   * @param result
   */
  private void setColumns(TreeTableView result) {
    TreeTableColumnDef[] cellFactoriesMap = new TreeTableColumnDef[cellFactories.size()];
    for (TreeTableColumnDef cellFactory : cellFactories) {
      cellFactoriesMap[cellFactory.getColIndex()] = cellFactory;
    }
    result.getColumns().clear();
    List<TreeTableColumn<?, ?>> cols = new ArrayList<>();
    for (TreeTableColumnDef ttCol : cellFactoriesMap) {
      try {
        String label = ttCol.getLabel();
        TreeTableColumn<Object, Object> col = new TreeTableColumn<>(label);
        col.setCellValueFactory((param) -> {
          Property<Object> resultProp = new SimpleObjectProperty<>();
          TreeItem treeItem = param.getValue();
          Object value = treeItem.getValue();
          if (value instanceof TreeNode) {
            int level = ((TreeNode) value).getLevel();
            LevelCellFactory cellFactory = ttCol.getCellFactory(level);
            if (cellFactory != null) {
              String textField = cellFactory.getTextField();
              RecordValue recordVal = (RecordValue) ((TreeNode) value).getValueObject();
              resultProp.setValue(recordVal.get(textField));
              if (cellFactory.isCheckBox()) {
                col.setCellFactory((p) -> {
                  TreeTableCell<Object, Object> res
                    = (TreeTableCell<Object, Object>) this.createCheckBoxCellFactory();
                  return res;
                });
              }
            }
          }
          return resultProp;
        });

        cols.add(col);
      } catch (Exception ex) {
        throw new RuntimeException("Error creating Tree table column for 'ttCol'.  Check args: {"
          + "ttCol = " + ttCol
          + "}", ex);

      }

    }
    ObservableList<TreeTableColumn<? extends Object, ?>> colsObs = FXCollections.observableArrayList(cols);
    for (TreeTableColumn colsOb : colsObs) {
      result.getColumns().add(colsOb);
    }
  }

  /**
   *
   * @return
   */
  private TreeTableCell<Object, Object> createCheckBoxCellFactory() {
    TreeTableCell<Object, Object> result
      = new TreeTableCell<Object, Object>() {
      @Override
      protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
          super.setGraphic(null);
          super.setText(null);
        } else {
          CheckBox checkBox = new CheckBox();
          ObjectProperty<Boolean> property = (ObjectProperty<Boolean>) item;
          checkBox.selectedProperty().bindBidirectional(property);
          super.setGraphic(checkBox);
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
    List<TreeNode> nodes;
    if (val instanceof TreeNode) {
      nodes = this.treeModel.getNodes((TreeNode) val);
    } else {
      int level = 0;
      nodes = this.treeModel.getNodes(level);
    }
    return nodes;
  }

  /**
   *
   * @return
   */
  @Override
  public Class<?> getObjectType() {
    return TreeTableView.class;
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
    this.context.getBean(this.id);
  }

  /**
   *
   * @param ac
   * @throws BeansException
   */
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
