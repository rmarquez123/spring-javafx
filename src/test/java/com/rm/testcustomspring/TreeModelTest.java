package com.rm.testcustomspring;

import com.rm.datasources.RecordValue;
import com.rm.springjavafx.tree.Link;
import com.rm.springjavafx.tree.RecordValueListsTreeModel;
import com.rm.springjavafx.tree.TreeNode;
import java.util.HashMap;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author rmarquez
 */
public class TreeModelTest {
  
  
  @Test
  public void testTreeModel() {
    RecordValueListsTreeModel treeModel = new RecordValueListsTreeModel();
    ListProperty<RecordValue> levelRecords0 = new SimpleListProperty<>();
    levelRecords0.setValue(FXCollections.observableArrayList(new RecordValue("id", new HashMap<String, Object>() {
      {
        put("id", 0);
        put("station_name", "Sacramento");
      }
    }), new RecordValue("id", new HashMap<String, Object>() {
      {
        put("id", 1);
        put("station_name", "Merced");
      }
    }), new RecordValue("id", new HashMap<String, Object>() {
      {
        put("id", 2);
        put("station_name", "Rancho Cordova"); 
      }
    })
    ));
    ListProperty<RecordValue> levelRecords1 = new SimpleListProperty<>();
    levelRecords1.setValue(FXCollections.observableArrayList(new RecordValue("id", new HashMap<String, Object>() {
      {
        put("id", 0);
        put("station_id", 0);  
        put("sensor", "temperature");
        
      }
    }), new RecordValue("id", new HashMap<String, Object>() {
      {
        put("id", 1);
        put("station_id", 0);
        put("sensor", "wind");
      }
    }), new RecordValue("id", new HashMap<String, Object>() {
      {
        put("id", 2);
        put("station_id", 0);
        put("sensor", "solar radiatiaon");
      }
    })
    ));
    Assert.assertEquals(0, treeModel.getNumberOfLevels());
    treeModel.addLevel("id", null, levelRecords0);
    Assert.assertEquals(1, treeModel.getNumberOfLevels());
    TreeNode<RecordValue> station = treeModel.getNode(0, 0);
    Assert.assertEquals("Sacramento", station.getObject().get("station_name"));
    Assert.assertEquals(0, treeModel.getNode(0, 0).getObject().get("id"));
    Assert.assertEquals("Merced", treeModel.getNode(0, 1).getObject().get("station_name"));
    treeModel.addLevel("id", new Link("id", "station_id"), levelRecords1);
    Assert.assertEquals(3, treeModel.getNodes(station).size());
    Assert.assertEquals("temperature", treeModel.getNode(station, 0).getValueObject().get("sensor"));
  }
}
