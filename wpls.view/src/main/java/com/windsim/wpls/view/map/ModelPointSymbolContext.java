package com.windsim.wpls.view.map;

import com.rm.panzoomcanvas.impl.points.PointShapeSymbology;
import com.rm.panzoomcanvas.layers.points.PointSymbology;
import com.windsim.wpls.view.series.ModelSeriesData;
import com.windsim.wpls.view.series.SeriesType;
import gov.inl.glass3.linesolver.ModelPointAmpacities;
import gov.inl.glass3.modelpoints.ModelPoint;
import gov.inl.glass3.weather.WeatherRecords;
import java.time.ZonedDateTime;
import javafx.beans.property.Property;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class ModelPointSymbolContext {

  @Autowired
  @Qualifier(value = "selectedSeriesType")
  private Property<SeriesType> seriesTypeProperty;

  @Autowired
  @Qualifier(value = "weatherRecordsProperty")
  private Property<WeatherRecords> weatherRecordsProperty;

  @Autowired
  @Qualifier(value = "modelPointAmapacitiesProperty")
  private Property<ModelPointAmpacities> modelPointAmapacitiesProperty;

  @Autowired
  @Qualifier(value = "frcstModelPointAmpacities")
  private Property<ModelPointAmpacities> frcstModelPointAmpacitiesProperty;

  @Autowired
  @Qualifier(value = "referenceDate")
  private Property<ZonedDateTime> referenceDateProperty;

  @Autowired
  ModelPointSeriesBasedSymbolizerProvider symbolizerProvider;

  /**
   *
   */
  public PointSymbology getSymbolizer() {
    PointShapeSymbology symbology = new PointShapeSymbology();
    symbology.markerSymbology().setValue((t) -> {
      ModelPoint modelPoint = (ModelPoint) t.getUserObject();
      PointShapeSymbology result;
      SeriesType seriesType = this.seriesTypeProperty.getValue();
      if ((seriesType != null && this.weatherRecordsProperty.getValue() != null)) {
        SeriesType value = seriesType;
        ModelPointAmpacities modelPointAmpacities = modelPointAmapacitiesProperty.getValue();
        ModelPointAmpacities fcstedModelPointAmpacities = frcstModelPointAmpacitiesProperty.getValue();
        WeatherRecords weatherRecords = this.weatherRecordsProperty.getValue();
        ModelSeriesData seriesData
          = new ModelSeriesData(weatherRecords, modelPointAmpacities, fcstedModelPointAmpacities);

        ZonedDateTime dateTime = referenceDateProperty.getValue();
        result = this.getSymbolizer(seriesData, seriesType, dateTime, modelPoint);
        if (result == null) {
          result = symbology;
        }
      } else {
        result = symbology;
      }
      return result;
    });
    symbology.getSelected().fillColorProperty().setValue(Color.CYAN);
    return symbology;
  }

  /**
   *
   * @param seriesData
   * @param seriesType
   * @param dateTime
   * @param modelPoint
   * @return
   */
  private PointShapeSymbology getSymbolizer(
    ModelSeriesData seriesData, SeriesType seriesType, ZonedDateTime dateTime, ModelPoint modelPoint) {
    return symbolizerProvider.getSymbolizer(seriesData, seriesType, dateTime, modelPoint);
  }

}
