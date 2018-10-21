package com.rm.springjavafx.tree;

/**
 *
 * @author rmarquez
 */
public class TreeNode<T> {
  private final T valueObject;
  private final int level;
  
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
  
}
