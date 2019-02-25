package com.windsim.wpls.view.map.impl;

import com.rm.panzoomcanvas.impl.points.PointShapeSymbology;
import com.windsim.wpls.view.map.ModelPointSeriesBasedSymbolizerProvider;
import com.windsim.wpls.view.series.ModelSeriesData;
import com.windsim.wpls.view.series.SeriesType;
import gov.inl.glass3.modelpoints.ModelPoint;
import java.time.ZonedDateTime;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class ModelPointSeriesBasedSymbolizerProviderImpl implements ModelPointSeriesBasedSymbolizerProvider{
  
  /**
   * 
   * @param seriesData
   * @param seriesType
   * @param dateTime
   * @param modelPoint
   * @return 
   */
  @Override
  public PointShapeSymbology getSymbolizer(ModelSeriesData seriesData, 
    SeriesType seriesType, ZonedDateTime dateTime, ModelPoint modelPoint) {
    PointShapeSymbology result;
    DefaultModelPointSeriesSymbolizer symbolizer = DefaultModelPointSeriesSymbolizer.getInstance(seriesType);
    result = symbolizer.getSymology(seriesData, dateTime, modelPoint);
    return result;
  }
  
}
