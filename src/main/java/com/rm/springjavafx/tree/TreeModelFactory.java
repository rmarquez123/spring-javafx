package com.rm.springjavafx.tree;

import common.db.RecordValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.ListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author rmarquez
 */
public class TreeModelFactory implements FactoryBean<TreeModel> {

  private String id;
  private List<Link> links = new ArrayList<>();
  private List<TreeDataSource> datasources = new ArrayList<>();

  @Required
  public void setId(String id) {
    this.id = id;
  }

  @Required
  public void setLinks(List<Link> links) {
    this.links = links;
  }

  @Required
  public void setDatasources(List<TreeDataSource> datasources) {
    this.datasources = datasources;
  }

  /**
   *
   * @return @throws Exception
   */
  @Override
  public TreeModel getObject() throws Exception {
    final RecordValueListsTreeModel result = new RecordValueListsTreeModel();
    Map<Integer, Link> linksMap = new HashMap<>();
    for (Link link : this.links) {
      Integer level = link.getLevel();
      linksMap.put(level, link);
    }
    int level;
    for (TreeDataSource treeDs : this.datasources) {
      level = treeDs.getLevel();
      ListProperty prop = treeDs.getDatasource().listProperty();
      Link link = (level == 0) ? null : linksMap.get(level);
      result.addLevel(treeDs.getIdField(), link, prop);
      treeDs.getDatasource().getSingleSelectionProperty().addListener((obs, old, change) -> {
        result.singleSelectionProperty().setValue(change);
      });
    }

    result.singleSelectionProperty().addListener((obs, old, change) -> {
      if (change != null) {
        List<TreeDataSource> ds = this.datasources;
        TreeDataSource dataSource = null;
        Integer levelOf = result.getLevelOf(change);
        if (levelOf != null) {
          dataSource = datasources.get(levelOf);
          dataSource.getDatasource().getSingleSelectionProperty().setValue(change);
        }
        if (dataSource == null) {
          throw new IllegalStateException("No data source is associated with record : " + change);
        }
        for (TreeDataSource dss : this.datasources) {
          if (dss != dataSource) {
            dss.getDatasource().getSingleSelectionProperty().setValue(null);
          }
        }
      } else {
        
      }
    });
    result.checkedValuesProperty().addListener((obs, old, change) -> {
      List<TreeDataSource> ds = TreeModelFactory.this.datasources;
      if (change != null) {
        for (TreeDataSource datasource : this.datasources) {
          ObservableList<RecordValue> newList = this.getNewCheckedList(datasource, change);
          datasource.getDatasource().checkedValuesProperty()
            .setValue(newList);
        }
      } else {
        for (TreeDataSource datasource : this.datasources) {
          datasource.getDatasource().checkedValuesProperty().setValue(null);
        }
      }
    });

    return result;
  }

  /**
   *
   * @param datasource
   * @param change
   * @return
   */
  private ObservableList<RecordValue> getNewCheckedList(TreeDataSource datasource, ObservableList<RecordValue> change) {
    ObservableList<RecordValue> newList;
    ObservableList<RecordValue> currentObs = datasource.getDatasource().checkedValuesProperty().getValue();
    if (currentObs != null) {
      List<RecordValue> current = new ArrayList<>(currentObs);
      current.removeIf((RecordValue t) -> !change.contains(t));
      for (RecordValue recordValue : change) {
        if (datasource.getDatasource().listProperty().getValue().contains(recordValue)
          && !current.contains(recordValue)) {
          current.add(recordValue);
        }
      }
      newList = FXCollections.observableList(current);
      datasource.getDatasource().checkedValuesProperty()
        .setValue(newList);
    } else {
      List<RecordValue> current = new ArrayList<>();
      for (RecordValue recordValue : change) {
        if (datasource.getDatasource().listProperty().getValue().contains(recordValue)) {
          current.add(recordValue);
        }
      }
      newList = FXCollections.observableList(current);
    }
    return newList;
  }

  /**
   *
   * @return
   */
  @Override
  public Class<?> getObjectType() {
    return TreeModel.class;
  }

}
