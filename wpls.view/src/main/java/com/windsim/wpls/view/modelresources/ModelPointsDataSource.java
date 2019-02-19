package com.windsim.wpls.view.modelresources;

import com.rm.datasources.RecordValue;
import com.rm.springjavafx.datasources.AbstractDataSource;
import gov.inl.glass3.linesolver.Model;
import gov.inl.glass3.modelpoints.ModelPoint;
import gov.inl.glass3.modelpoints.ModelPoints;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component(value = "modelpoints_datasource")
public class ModelPointsDataSource extends AbstractDataSource<RecordValue>
  implements InitializingBean {

  @Autowired
  @Qualifier(value = "glassModelProperty")
  private Property<Model> modelProperty;

  @Autowired
  @Qualifier(value = "selectedModelPoint")
  private Property<ModelPoint> modelPointProperty;

  @Override
  public void afterPropertiesSet() throws Exception {
    this.getSingleSelectionProperty().addListener((obs, old, change) -> {
      if (change != null) {
        String modelPointId = String.valueOf(change.get("modelPointId"));
        ModelPoint modelPoint = this.modelProperty.getValue().getModelPoints().get(modelPointId);
        this.modelPointProperty.setValue(modelPoint);
      } else {
        this.modelPointProperty.setValue(null);
      }
    });
    this.modelPointProperty.addListener((obs, old, change) -> {
      ObservableList<RecordValue> listItems = super.listProperty().getValue();
      if (change != null) {
        for (RecordValue listItem : listItems) {
          if (listItem.get("modelPointId").equals(change.getModelPointId())) {
            this.getSingleSelectionProperty().setValue(listItem);
            break;
          }
        }
      } else {
        this.getSingleSelectionProperty().setValue(null);
      }
    });

    this.modelProperty.addListener((obs, old, change) -> {
      this.setRecords(new ArrayList<>());
      if (change != null) {
        List<RecordValue> recordValues = new ArrayList<>();
        ModelPoints modelPoints = change.getModelPoints();
        List<ModelPoint> temp = new ArrayList<>();
        for (ModelPoint modelPoint : modelPoints) {
          temp.add(modelPoint);
        }
        temp.sort((o1, o2) -> o1.getModelPointId().compareTo(o2.getModelPointId()));

        for (ModelPoint modelPoint : temp) {
          HashMap<String, Object> values = new HashMap<String, Object>() {
            {
              put("lineSectionId", modelPoint.getLineId());
              put("modelPointId", modelPoint.getModelPointId());
              put("weather_station", modelPoint.getWeatherStationId());
            }
          };
          RecordValue recordValue = new RecordValue("modelPointId", values);
          recordValues.add(recordValue);
        }
        this.setRecords(recordValues);
      } else {
        this.setRecords(Collections.EMPTY_LIST);
      }
    });
  }

}
