package com.rm.springjavafx.table;

/**
 *
 * @author rmarquez
 */

public class TableViewColumn {
  private String label;
  private int columnIndex;
  private String propertyName;
  private String rendererType;

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }
  
  public String getPropertyName() {
    return propertyName;
  }

  public void setPropertyName(String propertyName) {
    this.propertyName = propertyName;
  }

  public int getColumnIndex() {
    return columnIndex;
  }

  public void setColumnIndex(int columnIndex) {
    this.columnIndex = columnIndex;
  }

  public String getRendererType() {
    return rendererType;
  }

  public void setRendererType(String rendererType) {
    this.rendererType = rendererType;
  }
  
  @Override
  public String toString() {
    return "TableViewRenderer{" + "name=" + label + ", column=" + columnIndex + ", rendererType=" + rendererType + '}';
  }
  
 
  
}
