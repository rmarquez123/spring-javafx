package com.windsim.wpls.view.modelpoint;

import com.rm.datasources.RecordValue;
import com.rm.springjavafx.datasources.AbstractDataSource;
import gov.inl.glass3.linesolver.Model;
import gov.inl.glass3.modelpoints.ModelPoint;
import gov.inl.glass3.windmodel.LookupTable;
import gov.inl.glass3.windmodel.LookupTables;
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
@Component(value = "modelinfo_lookuptable_tabledata")
public class LookupTableDataSource extends AbstractDataSource<RecordValue>
  implements InitializingBean {

  @Autowired
  @Qualifier(value = "glassModelProperty")
  private Property<Model> modelProperty;

  @Autowired
  @Qualifier(value = "selectedModelPoint")
  private Property<ModelPoint> modelPointProperty;

  public LookupTableDataSource() {
    super.setRecords(new ArrayList<>());
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.modelPointProperty.addListener((obs, old, change) -> {
      this.setRecords(new ArrayList<>());
      if (change != null) {
        List<RecordValue> recordValues = new ArrayList<>();
        String modelPointId = change.getModelPointId();
        LookupTables data = this.modelProperty.getValue().getLookupTableByModelPoint(modelPointId);
        for (LookupTable item : data) {
          Map<String, Object> values = new HashMap<String, Object>() {
            {
              put("modelPointId", item.getModelPointId());
              put("sectorAngle", item.getSectorNum());
              put("speedUp", item.getSpeedUp());
              put("directionShift", item.getDirectionShift());
            }
          };

          RecordValue recordValue = new RecordValue("modelPointId", values);
          recordValues.add(recordValue);
        }
        recordValues.sort((r1, r2) 
          -> ((Double) r1.get("sectorAngle")).compareTo((Double) r2.get("sectorAngle")));
        this.setRecords(recordValues);
      }
    });

  }

}
