package com.windsim.wpls.view.loadseries.sources;

import com.rm.datasources.RecordValue;
import com.rm.springjavafx.datasources.AbstractDataSource;
import gov.inl.glass3.linesolver.Model;
import gov.inl.glass3.weather.WeatherRecord;
import gov.inl.glass3.weather.WeatherRecords;
import gov.inl.glass3.weather.WeatherStation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.beans.property.Property;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component(value = "confirm_loadseries_weatherrecords_tabledata")
public class WeatherRecordsDataSource extends AbstractDataSource<RecordValue>
  implements InitializingBean {

  @Autowired
  @Qualifier(value = "glassModelProperty")
  private Property<Model> modelProperty;

  @Autowired
  @Qualifier(value = "confirmLoadSeriesProperty")
  private Property<WeatherRecords> weatherRecordsProperty;

  public WeatherRecordsDataSource() {
    super.setRecords(new ArrayList<>());
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.weatherRecordsProperty.addListener((obs, old, change) -> {
      this.setRecords(new ArrayList<>());
      if (change != null) {
        List<RecordValue> recordValues = new ArrayList<>();
        for (WeatherStation station : modelProperty.getValue().getWeatherStations()) {
          Set<WeatherRecord> data = change.get(station.getName());
          for (WeatherRecord item : data) {
            Map<String, Object> values = new HashMap<String, Object>() {
              {
                put("stationId", item.getStationId());
                put("datetime", item.getDateTime());
                put("ambientTemperature", item.getAmbientTemp());
                put("windSpeed", item.getWindSpeed());
                put("windDir", item.getWindAngle());
                put("solar", item.getSolar());
              }
            };
            RecordValue recordValue = new RecordValue("stationId", values);
            recordValues.add(recordValue);
          }
        }
        this.setRecords(recordValues);

      }

    });
  }

}
