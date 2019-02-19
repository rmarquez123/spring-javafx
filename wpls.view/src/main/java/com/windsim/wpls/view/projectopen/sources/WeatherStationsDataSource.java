package com.windsim.wpls.view.projectopen.sources;

import com.rm.datasources.RecordValue;
import com.rm.springjavafx.datasources.AbstractDataSource;
import gov.inl.glass3.linesolver.Model;
import gov.inl.glass3.weather.WeatherStation;
import gov.inl.glass3.weather.WeatherStations;
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
@Component(value = "confirm_projopen_weatherstations_tabledata")
public class WeatherStationsDataSource extends AbstractDataSource<RecordValue>
  implements InitializingBean {

  @Autowired
  @Qualifier(value = "confirmOpenModelProperty")
  private Property<Model> modelProperty;

  public WeatherStationsDataSource() {
    super.setRecords(new ArrayList<>());
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.modelProperty.addListener((obs, old, change) -> {
      this.setRecords(new ArrayList<>());
      if (change != null) {
        List<RecordValue> recordValues = new ArrayList<>();
        WeatherStations data = change.getWeatherStations();
        for (WeatherStation item : data) {
          Map<String, Object> values = new HashMap<String, Object>() {
            {
              put("name", item.getName());
              put("elevation", item.getElevation());
              put("longitude", item.getGeometry().getX());
              put("latitude", item.getGeometry().getY());
            }
          };
          RecordValue recordValue = new RecordValue("name", values);
          recordValues.add(recordValue);
        }
        this.setRecords(recordValues);
      }

    });
  }

}
