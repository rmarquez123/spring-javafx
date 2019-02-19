package com.windsim.wpls.view.modelresources;

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
@Component(value = "linesections_datasource")
public class LineSectionsDataSource extends AbstractDataSource<RecordValue>
  implements InitializingBean {

  @Autowired
  @Qualifier(value = "glassModelProperty")
  private Property<Model> modelProperty;

  public LineSectionsDataSource() {
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.modelProperty.addListener((obs, old, change) -> {
      this.setRecords(new ArrayList<>());
      if (change != null) {
        List<RecordValue> recordValues = new ArrayList<>();
        LineSections lineSections = change.getLineSections();
        for (LineSection lineSection : lineSections) {
          Map<String, Object> values = new HashMap<String, Object>() {
            {
              put("lineSectionId", lineSection.getLineSectionId());
            }
          };
          RecordValue recordValue = new RecordValue("lineSectionId", values);
          recordValues.add(recordValue);
        }
        this.setRecords(recordValues);
      }

    });

  }

}
