package com.windsim.wpls.view.projectopen.sources;

import com.rm.datasources.RecordValue;
import com.rm.springjavafx.datasources.AbstractDataSource;
import gov.inl.glass3.linesolver.Model;
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
@Component(value = "confirm_projopen_lookuptables_tabledata")
public class LookupTablesDataSource extends AbstractDataSource<RecordValue>
  implements InitializingBean {

  @Autowired
  @Qualifier(value = "confirmOpenModelProperty")
  private Property<Model> modelProperty;

  public LookupTablesDataSource() {
    super.setRecords(new ArrayList<>());
  }
  
  

  @Override
  public void afterPropertiesSet() throws Exception {
    this.modelProperty.addListener((obs, old, change) -> {
      this.setRecords(new ArrayList<>());
      if (change != null) {
        List<RecordValue> recordValues = new ArrayList<>();
        LookupTables data = change.getLookupTables();
        for ( LookupTable item : data) {
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
        this.setRecords(recordValues);
      }

    });
  }

}
