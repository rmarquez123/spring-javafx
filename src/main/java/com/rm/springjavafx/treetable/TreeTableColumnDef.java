package com.rm.springjavafx.treetable;

import com.rm.springjavafx.tree.LevelCellFactory;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author rmarquez
 */
public class TreeTableColumnDef {

  private int colIndex;
  private String label;
  private List<LevelCellFactory> cellFactories;
  private String clazz;
  private Integer width;

  @Required
  public void setColIndex(int colIndex) {
    this.colIndex = colIndex;
  }

  @Required
  public void setLabel(String label) {
    this.label = label;
  }
  
  public void setClass(String clazz) {
    this.clazz = clazz;
  }
  
  public void setWidth(Integer width) {
    this.width = width;
  }

  @Required
  public void setCellFactories(List<LevelCellFactory> cellFactories) {
    this.cellFactories = cellFactories;
  }

  public int getColIndex() {
    return colIndex;
  }

  public String getLabel() {
    return label;
  }

  public String getClazz() {
    return clazz;
  }

  public Integer getWidth() {
    return width;
  }
  
  
  /**
   * 
   * @param level
   * @return 
   */
  public LevelCellFactory getCellFactory(int level) {
    LevelCellFactory result = null;
    for (LevelCellFactory cellFactory : cellFactories) {
      if (Objects.equals(cellFactory.getLevel(), level)) {
        result = cellFactory;
      }
    }
    return result;
  }

  @Override
  public String toString() {
    return "TreeTableColumn{" + "colIndex=" + colIndex + ", station=" + label + ", cellFactory=" + cellFactories + '}';
  }
}
