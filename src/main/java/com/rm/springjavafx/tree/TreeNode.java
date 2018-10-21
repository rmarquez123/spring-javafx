package com.rm.springjavafx.tree;

/**
 *
 * @author rmarquez
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
