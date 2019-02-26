package com.rm.springjavafx.tree;

import com.rm.datasources.RecordValue;
import java.util.Objects;

/**
 *
 * @author rmarquez
 */
public class Link {

  private final String childField;
  private final String parentField;
  private int level = -1;
  /**
   * 
   * @param parentField
   * @param childField
   */
  public Link(String parentField, String childField) {
    this.parentField = parentField;
    this.childField = childField;
  }
  
  
  public void setLevel(int level) {
    this.level = level;
  }

  @Override
  public String toString() {
    return "Link{" + "childField=" + childField + ", parentField=" + parentField + ", level=" + level + '}';
  }
  
  /**
   * 
   * @param parent
   * @param child
   * @return 
   */
  boolean isAssociated(TreeNode<RecordValue> parent, TreeNode<RecordValue> child) {
    Object childVal = child.getObject().get(this.childField);
    Object parentVal = parent.getObject().get(this.parentField);
    boolean result = Objects.equals(parentVal, childVal);
    return result;
  }

  Integer getLevel() {
    return level;
  }
}
