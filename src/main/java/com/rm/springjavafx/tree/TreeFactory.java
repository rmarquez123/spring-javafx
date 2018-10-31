package com.rm.springjavafx.tree;

import com.rm.datasources.RecordValue;
import com.rm.springjavafx.FxmlInitializer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    TreeItem<Object> rootItem = new TreeItem<>("Inbox");
    this.addTreeItems(rootItem);
    result.setRoot(rootItem);
    Map<Integer, LevelCellFactory> cellFactoriesMap = new HashMap<>(); 
    for (LevelCellFactory cellFactory : cellFactories) {
      cellFactoriesMap.put(cellFactory.getLevel(), cellFactory); 
    }
    result.setCellFactory((param) -> new TreeCell<Object>(){
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
