package com.windsim.wpls.view.projectopen.sources;

import com.rm.datasources.RecordValue;
import com.rm.springjavafx.datasources.AbstractDataSource;
import gov.inl.glass3.conductors.Conductor;
import gov.inl.glass3.conductors.ConductorCatalogue;
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
@Component(value = "confirm_projopen_conductors_tabledata")
public class ConductorDataSource extends AbstractDataSource<RecordValue>
  implements InitializingBean {

  @Autowired
  @Qualifier(value = "confirmOpenModelProperty")
  private Property<Model> modelProperty;

  public ConductorDataSource() {
    super.setRecords(new ArrayList<>());
  }
  
  

  @Override
  public void afterPropertiesSet() throws Exception {
    this.modelProperty.addListener((obs, old, change) -> {
      this.setRecords(new ArrayList<>());
      if (change != null) {
        List<RecordValue> recordValues = new ArrayList<>();
        ConductorCatalogue conductorCatalogue = change.getConductorCatalogue();
        for (Conductor conductor : conductorCatalogue) {
          Map<String, Object> values = new HashMap<String, Object>() {
            {
              put("type", conductor.getType());
              put("name", conductor.getName());
              put("diameter", conductor.getDiameter());
              put("min resistence", conductor.getMaxResistence());
              put("max resistence", conductor.getMinResistence());
              put("min temperature", conductor.getMinTemperature());
              put("max temperature", conductor.getMaxTemperature());
              put("heat capacity", conductor.getLinearHeatCapacity());
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
