package com.rm.springjavafx.treetable;

import com.rm.datasources.RecordValue;
import com.rm.springjavafx.tree.LevelCellFactory;
import com.rm.springjavafx.tree.TreeModel;
import com.rm.springjavafx.tree.TreeNode;
import com.rm.springjavafx.FxmlInitializer;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
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
public class TreeTableFactory implements FactoryBean<TreeTableView>, InitializingBean, ApplicationContextAware {

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

  public void setCellFactories(List<TreeTableColumnDef> cellFactories) {
    this.cellFactories = cellFactories;
  }

  @Override
  public TreeTableView getObject() throws Exception {
    TreeTableView result = (TreeTableView) this.fxmlInitializer.getNode(fxml, fxmlId);
    TreeItem rootItem = new TreeItem<>("Inbox");
    this.addTreeItems(rootItem);
    result.setRoot(rootItem);
    this.setColumns(result);
    return result;
  }
  
  /**
   * 
   * @param result 
   */
  private <T> void setColumns(TreeTableView<T> result) {
    TreeTableColumnDef[] cellFactoriesMap = new TreeTableColumnDef[cellFactories.size()];
    for (TreeTableColumnDef cellFactory : cellFactories) {
      cellFactoriesMap[cellFactory.getColIndex()] = cellFactory;
    }
    result.getColumns().clear();
    List<TreeTableColumn<?, ?>> cols = new ArrayList<>();
    for (TreeTableColumnDef ttCol : cellFactoriesMap) {
      String label = ttCol.getLabel();
      TreeTableColumn<T, Object> col = new TreeTableColumn<>(label);
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
          } 
        } 
        return resultProp;
      });
      cols.add(col);
    }
    ObservableList<TreeTableColumn<? extends Object, ?>> colsObs = FXCollections.observableArrayList(cols);
    for (TreeTableColumn colsOb : colsObs) {
      result.getColumns().add(colsOb);
    }
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

  @Override
  public void setApplicationContext(ApplicationContext ac) throws BeansException {
    this.context = ac;
  }

}
