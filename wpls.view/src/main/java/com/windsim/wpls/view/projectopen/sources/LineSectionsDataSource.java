package com.windsim.wpls.view.projectopen.sources;

import com.rm.datasources.RecordValue;
import com.rm.springjavafx.datasources.AbstractDataSource;
import gov.inl.glass3.linesections.LineSection;
import gov.inl.glass3.linesections.LineSections;
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
@Component(value = "confirm_projopen_linesections_tabledata")
public class LineSectionsDataSource extends AbstractDataSource<RecordValue>
  implements InitializingBean {

  @Autowired
  @Qualifier(value = "confirmOpenModelProperty")
  private Property<Model> modelProperty;

  public LineSectionsDataSource() {
    super.setRecords(new ArrayList<>());
  }
  
  

  @Override
  public void afterPropertiesSet() throws Exception {
    this.modelProperty.addListener((obs, old, change) -> {
      this.setRecords(new ArrayList<>());
      if (change != null) {
        List<RecordValue> recordValues = new ArrayList<>();
        LineSections linesections = change.getLineSections();
        for (LineSection linesection : linesections) {
          Map<String, Object> values = new HashMap<String, Object>() {
            {
              put("linesectionId", linesection.getLineSectionId());
              put("conductorId", linesection.getConductorId());
              put("bundle", linesection.getBundle());
              put("airQuality", linesection.getAirQuality());
              
              put("absorptivity", linesection.getRadProps().getAbsorptivity());
              put("emissivity", linesection.getRadProps().getEmissivity());
              put("maxTemp", linesection.getMaxTemperature());
              put("emergencyTemp", linesection.getEmergencyTemperature());
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
