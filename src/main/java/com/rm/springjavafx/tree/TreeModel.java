package com.rm.springjavafx.tree;

import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;

/**
 *
 * @author rmarquez
 */
public interface TreeModel<T> {

  /**
   *
   * @return
   */
  public SelectionMode getSelectionMode();

  /**
   *
   * @return
   */
  public ObservableList<T> getMultiSelectionProperty();

  /**
   *
   * @return
   */
  public ObjectProperty<T> getSingleSelectionProperty();

  /**
   *
   * @return
   */
  public int getNumberOfLevels();

  /**
   *
   * @return
   */
  public ReadOnlyIntegerProperty getNumberOfLevelsProperty();

  /**
   *
   * @param level
   * @return
   */
  public String getIdField(int level);

  /**
   *
   * @param level
   * @param idValue
   * @return
   */
  public TreeNode<T> getNode(int level, Object idValue);

  /**
   *
   * @param node
   * @return
   */
  public List<TreeNode<T>> getNodes(TreeNode<T> node);

  /**
   *
   * @param parentNode
   * @param idValue
   * @return
   */
  public TreeNode<T> getNode(TreeNode<T> parentNode, Object idValue);

  /**
   *
   * @param level
   * @return
   */
  public ListProperty<TreeNode<T>> getNodes(int level);

}
