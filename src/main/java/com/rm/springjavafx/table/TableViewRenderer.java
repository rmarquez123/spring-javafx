package com.rm.springjavafx.table;

/**
 *
 * @author rmarquez
 */

public class TableViewRenderer {
  private String name;
  private int column;
  private String rendererType;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getColumn() {
    return column;
  }

  public void setColumn(int column) {
    this.column = column;
  }

  public String getRendererType() {
    return rendererType;
  }

  public void setRendererType(String rendererType) {
    this.rendererType = rendererType;
  }
  
  @Override
  public String toString() {
    return "TableViewRenderer{" + "name=" + name + ", column=" + column + ", rendererType=" + rendererType + '}';
  }
  
 
  
}
