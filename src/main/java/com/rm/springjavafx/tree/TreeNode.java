package com.rm.springjavafx.tree;

/**
 * The TreeNode data structure used throughout by the {@linkplain  TreeFactory},
 * {@linkplain TreeModel}, etc.
 *
 * @author rmarquez
 * @param T the type of value object
 */
public class TreeNode<T> {

  private final int level;
  private final T valueObject;

  /**
   *
   * @param level
   * @param valueObject
   */
  public TreeNode(int level, T valueObject) {
    this.valueObject = valueObject;
    this.level = level;
  }

  /**
   *
   * @return
   */
  public int getLevel() {
    return level;
  }

  /**
   *
   * @return
   */
  public T getObject() {
    return valueObject;
  }

  public T getValueObject() {
    return valueObject;
  }

  @Override
  public String toString() {
    return "TreeNode{" + "level=" + level + ", valueObject=" + valueObject + '}';
  }
}
