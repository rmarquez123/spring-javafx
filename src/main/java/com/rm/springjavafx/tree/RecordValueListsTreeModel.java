package com.rm.springjavafx.tree;

import com.rm.datasources.RecordValue;
import java.util.Collections;
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

/**
 *
 * @author rmarquez
 */
public class RecordValueListsTreeModel implements TreeModel<RecordValue> {

  private final ListProperty<ListProperty<TreeNode<RecordValue>>> treeNodes = new SimpleListProperty<>(FXCollections.observableArrayList());
  private final IntegerProperty numLevelsProperty = new SimpleIntegerProperty();
  private final Map<Integer, String> idFields = new HashMap<>();
  private final Map<Integer, Link> links = new HashMap<>();

  /**
   *
   */
  public RecordValueListsTreeModel() {
    this.treeNodes.addListener((obs, old, change) -> {
      this.numLevelsProperty.setValue(this.treeNodes.size());
    });
  }

  /**
   *
   * @param idField
   * @param link
   * @param recordsProperty
   */
  public void addLevel(String idField, Link link, ListProperty<RecordValue> recordsProperty) {
    int level = this.getNumberOfLevels();
    if (level > 0) {
      link.setLevel(level);
      this.links.put(level, link);
    }
    this.idFields.put(level, idField);
    recordsProperty.getClass().getGenericInterfaces();
    recordsProperty.getClass().getGenericSuperclass();
    recordsProperty.addListener((obs, old, change) -> {
      this.setTreeNodes(level, change);
    });
    List<RecordValue> records = recordsProperty.getValue();
    this.setTreeNodes(level, records);
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
    ListProperty<TreeNode<RecordValue>> levelRecords = this.treeNodes.getValue().get(level);
    TreeNode<RecordValue> result = null;
    for (TreeNode<RecordValue> node : levelRecords.getValue()) {
      String idField = this.idFields.get(level);
      if (Objects.equals(node.getObject().get(idField), idValue)) {
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
  public List<TreeNode<RecordValue>> getNodes(TreeNode<RecordValue> parentNode) {
    int parentLevel = parentNode.getLevel();
    int childLevel = parentLevel + 1;
    List<TreeNode<RecordValue>> result;
    if (childLevel < this.getNumberOfLevels()) {
      ListProperty<TreeNode<RecordValue>> childLevelNodes = this.treeNodes.getValue().get(childLevel);
      List<TreeNode<RecordValue>> allChildren = childLevelNodes.getValue();
      Link link = this.links.get(childLevel);
      result = allChildren.stream().filter((TreeNode<RecordValue> child) -> {
        return link.isAssociated(parentNode, child);
      }).collect(Collectors.toList());
    } else {
      result = Collections.EMPTY_LIST;
    }
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
    List<TreeNode<RecordValue>> nodes = this.getNodes(parentNode);
    TreeNode<RecordValue> result = null;
    int childLevel = parentNode.getLevel() + 1;
    String idField = this.idFields.get(childLevel);
    for (TreeNode<RecordValue> node : nodes) {
      if (Objects.equals(node.getObject().get(idField), idValue)) {
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
  public ListProperty<TreeNode<RecordValue>> getNodes(int level) {
    ListProperty<TreeNode<RecordValue>> result;
    if (level >= this.treeNodes.size()) {
      if (level == 0) {
        result = new SimpleListProperty<>();
      } else {
        throw new IllegalArgumentException("Level index exceeds size. Check args : {"
                + "level = " + level
                + ", size = " + this.treeNodes.size()
                + "}");
      }
    } else {
      result = this.treeNodes.getValue().get(level);
    }
    return result;
  }

  /**
   *
   * @param records
   * @param level
   */
  private void setTreeNodes(int level, List<RecordValue> records) {
    ObservableList<TreeNode<RecordValue>> result;
    if (records != null) {
      List<TreeNode<RecordValue>> a = records.stream().map((r) -> new TreeNode<>(level, r))
              .collect(Collectors.toList());
      result = FXCollections.observableArrayList(a);
    } else {
      result = FXCollections.emptyObservableList();
    }
    if (level >= this.treeNodes.getValue().size()) {
      this.treeNodes.getValue().add(new SimpleListProperty<>(result));
    } else {
      this.treeNodes.getValue().get(level).setValue(result);
    }
  }
}
