package com.windsim.wpls.view.map;

import com.rm.panzoomcanvas.impl.points.PointShapeSymbology;
import com.rm.panzoomcanvas.layers.points.PointSymbology;
import com.windsim.wpls.view.series.SeriesModel;
import gov.inl.glass3.modelpoints.ModelPoint;
import java.time.ZonedDateTime;
import javafx.beans.property.Property;
import javafx.scene.paint.Color;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class ModelPointSymbolizer {

  private ModelPoint modelPoint;

  private Property<SeriesModel> seriesModel;

  private Property<ZonedDateTime> dateTimeProperty;

  public ModelPointSymbolizer() {

  }

  /**
   *
   */
  public PointSymbology getSymbolizer() {
    PointShapeSymbology symbology = new PointShapeSymbology();
    symbology.getSelected().fillColorProperty().setValue(Color.CYAN);
    return symbology;
  }

}
