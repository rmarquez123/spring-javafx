package com.rm.springjavafx.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.ListProperty;
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
    RecordValueListsTreeModel result = new RecordValueListsTreeModel();
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
      result.addLevel(this.id, link, prop);
    }
    return result;
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
