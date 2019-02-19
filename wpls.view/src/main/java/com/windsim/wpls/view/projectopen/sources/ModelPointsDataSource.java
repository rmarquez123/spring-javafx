package com.windsim.wpls.view.projectopen.sources;

import com.rm.datasources.RecordValue;
import com.rm.springjavafx.datasources.AbstractDataSource;
import gov.inl.glass3.modelpoints.ModelPoint;
import gov.inl.glass3.modelpoints.ModelPoints;
import gov.inl.glass3.linesolver.Model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.Property;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component(value = "confirm_projopen_modelpoints_tabledata")
public class ModelPointsDataSource extends AbstractDataSource<RecordValue>
  implements InitializingBean {

  @Autowired
  @Qualifier(value = "confirmOpenModelProperty")
  private Property<Model> modelProperty;

  public ModelPointsDataSource() {
    super.setRecords(new ArrayList<>());
  }
  
  

  @Override
  public void afterPropertiesSet() throws Exception {
    this.modelProperty.addListener((obs, old, change) -> {
      this.setRecords(new ArrayList<>());
      if (change != null) {
        List<RecordValue> recordValues = new ArrayList<>();
        ModelPoints data = change.getModelPoints();
        for (ModelPoint item : data) {
          Map<String, Object> values = new HashMap<String, Object>() {
            {
              put("modelPointId", item.getModelPointId());
              put("linesectionId", item.getLineId());
              put("weatherStationId", item.getWeatherStationId());
              put("azimuth", item.getGeometry().getAzimuth());
              
              put("elevation", item.getGeometry().getElevation());
              put("longitude", item.getGeometry().getPoint().getX());
              put("latitude", item.getGeometry().getPoint().getY());
            }
          };
          RecordValue recordValue = new RecordValue("modelPointId", values);
          recordValues.add(recordValue);
        }
        this.setRecords(recordValues);
      }

    });
  }

}
