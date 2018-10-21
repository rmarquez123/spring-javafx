package com.rm.springjavafx.tree;

import com.rm.datasources.DataSource;

/**
 *
 * @author rmarquez
 */
public class TreeDataSource {

  private Integer level;
  private DataSource datasource;

  public TreeDataSource() {

  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  public void setDatasource(DataSource datasource) {
    this.datasource = datasource;
  }

  public int getLevel() {
    return level;
  }

  public DataSource getDatasource() {
    return datasource;
  }

  @Override
  public String toString() {
    return "TreeDataSource{" + "level=" + level + ", datasource=" + datasource + '}';
  }

}
