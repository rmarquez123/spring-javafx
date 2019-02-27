package com.rm.springjavafx.tree;

import com.rm.datasources.DataSource;
import com.rm.datasources.RecordValue;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author rmarquez
 */
public class TreeDataSource {

  private Integer level;
  private String idField;
  private DataSource datasource;

  public TreeDataSource() {
  }

  @Required
  public void setIdField(String idField) {
    this.idField = idField;
  }

  @Required
  public void setLevel(Integer level) {
    this.level = level;
  }

  @Required
  public void setDatasource(DataSource<RecordValue> datasource) {
    this.datasource = datasource;
  }

  public int getLevel() {
    return level;
  }

  public DataSource<RecordValue> getDatasource() {
    return datasource;
  }

  String getIdField() {
    return idField;
  }

  @Override
  public String toString() {
    return "TreeDataSource{" + "level=" + level + ", datasource=" + datasource + '}';
  }

}
