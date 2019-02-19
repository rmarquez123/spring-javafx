package com.windsim.wpls.view.realtime;

import gov.inl.glass3.frcstdlr.FcstDlrExporter;
import gov.inl.glass3.linesolver.ModelPointAmpacities;
import gov.inl.glass3.linesolver.ModelPointAmpacity;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.beans.property.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class RealTimeFcstExporter implements FcstDlrExporter {

  @Autowired
  @Qualifier(value = "frcstModelPointAmpacities")
  private Property<ModelPointAmpacities> frcstModelPointAmpacitiesProperty;
  
  /**
   * 
   * @param t
   * @param ampacities 
   */
  @Override
  public void export(ZonedDateTime t, List<ModelPointAmpacity> ampacities) {
    Map<String, Set<ModelPointAmpacity>> map = new HashMap<>();
    for (ModelPointAmpacity ampacity : ampacities) {
      if (!map.containsKey(ampacity.getModelPointId())) {
        map.put(ampacity.getModelPointId(), new HashSet<>());
      }
      map.get(ampacity.getModelPointId()).add(ampacity);
    }
    ModelPointAmpacities current = this.frcstModelPointAmpacitiesProperty.getValue();
    if (current != null) {
      for (String modelPointId : map.keySet()) {
        Set<ModelPointAmpacity> a = current.get(modelPointId);
        map.get(modelPointId).addAll(a);
      }
    }
    ModelPointAmpacities modelPointAmapacities = new ModelPointAmpacities(map);
    this.frcstModelPointAmpacitiesProperty.setValue(modelPointAmapacities); 
  }

}
