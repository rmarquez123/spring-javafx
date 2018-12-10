package com.rm.springjavafx.tree;

import com.rm.datasources.DataSource;
import com.rm.datasources.RecordValue;

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

  public void setDatasource(DataSource<RecordValue> datasource) {
    this.datasource = datasource;
  }

  public int getLevel() {
    return level;
  }

  public DataSource<RecordValue> getDatasource() {
    return datasource;
  }

  @Override
  public String toString() {
    return "TreeDataSource{" + "level=" + level + ", datasource=" + datasource + '}';
  }

}
