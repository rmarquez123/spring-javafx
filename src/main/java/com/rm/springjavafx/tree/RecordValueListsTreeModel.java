package com.rm.springjavafx.tree;

import com.rm.datasources.RecordValue;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 *
 * @author rmarquez
 */
public class RecordValueListsTreeModel implements TreeModel<RecordValue>{
  
  private final ListProperty<ListProperty<TreeNode<RecordValue>>> recordValues = new SimpleListProperty<>(FXCollections.observableArrayList());
  private final IntegerProperty numLevelsProperty = new SimpleIntegerProperty();
  private final Map<Integer, String> idFields = new HashMap<>(); 
  private final Map<Integer, Link> links = new HashMap<>(); 
  
  /**
   * 
   */
  public RecordValueListsTreeModel() {
    this.recordValues.addListener((obs, old, change)->{
      this.numLevelsProperty.setValue(this.recordValues.size());
    });
  }
    
  /**
   * 
   * @param idField
   * @param link
   * @param records 
   */
  public void addLevel(String idField, Link link, ListProperty<RecordValue> records) {
    int level = this.getNumberOfLevels();
    if (level > 0) {
      link.setLevel(level);
      this.links.put(level, link); 
    }
    this.idFields.put(level, idField); 
    List<TreeNode<RecordValue>> a = records.getValue().stream().map((r)->new TreeNode<>(level, r))
            .collect(Collectors.toList());
    SimpleListProperty<TreeNode<RecordValue>> listProp = new SimpleListProperty<>(FXCollections.observableArrayList(a));
    this.recordValues.getValue().add(listProp);
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public int getNumberOfLevels() {
    return this.numLevelsProperty.getValue();
  }

  @Override
  public ReadOnlyIntegerProperty getNumberOfLevelsProperty() {
    return this.numLevelsProperty;
  }
  
  @Override
  public String getIdField(int level) {
    return this.idFields.get(level); 
  } 
  
  @Override
  public TreeNode<RecordValue> getNode(int level, Object idValue) {
    ListProperty<TreeNode<RecordValue>> levelRecords = this.recordValues.getValue().get(level);
    TreeNode<RecordValue> result = null; 
    for (TreeNode<RecordValue> node : levelRecords.getValue()) {
      String idField = this.idFields.get(level);
      if (Objects.equals(node.getObject().get(idField), idValue) ) {
        result = node; 
        break; 
      }
    }
    return result;
  }
  
  /**
   * 
   * @param parentNode
   * @return 
   */
  @Override
  public ObservableList<TreeNode<RecordValue>> getNodes(TreeNode<RecordValue> parentNode) {
    int parentLevel = parentNode.getLevel();
    int childLevel = parentLevel+1;
    ObservableList<TreeNode<RecordValue>> allChildren = this.recordValues.getValue().get(childLevel).getValue();
    Link link = this.links.get(childLevel);
    FilteredList<TreeNode<RecordValue>> result = allChildren.filtered((TreeNode<RecordValue> child) -> {  
      return link.isAssociated(parentNode, child); 
    }); 
    return result; 
  }
  
  /**
   * 
   * @param parentNode
   * @param idValue
   * @return 
   */
  @Override
  public TreeNode<RecordValue> getNode(TreeNode<RecordValue> parentNode, Object idValue) {
    ObservableList<TreeNode<RecordValue>> nodes = this.getNodes(parentNode);
    TreeNode<RecordValue> result = null;
    int childLevel = parentNode.getLevel() + 1;
    String idField = this.idFields.get(childLevel);
    for (TreeNode<RecordValue> node : nodes) {
      if (Objects.equals(node.getObject().get(idField), idValue) ) {
        result = node; 
        break; 
      }
    }
    return result; 
  }
  
  /**
   * 
   * @param level
   * @return 
   */
  @Override
  public List<TreeNode<RecordValue>> getNodes(int level) {
    return this.recordValues.get(level).getValue(); 
  }
  
  
}
