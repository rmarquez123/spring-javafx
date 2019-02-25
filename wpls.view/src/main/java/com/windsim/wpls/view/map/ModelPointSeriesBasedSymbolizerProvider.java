package com.windsim.wpls.view.map;

import com.rm.panzoomcanvas.impl.points.PointShapeSymbology;
import com.windsim.wpls.view.series.ModelSeriesData;
import com.windsim.wpls.view.series.SeriesType;
import gov.inl.glass3.modelpoints.ModelPoint;
import java.time.ZonedDateTime;

/**
 *
 * @author Ricardo Marquez
 */
public interface ModelPointSeriesBasedSymbolizerProvider {

  /**
   *
   * @param seriesData
   * @param seriesType
   * @param dateTime
   * @param modelPoint
   * @return
   */
  PointShapeSymbology getSymbolizer(ModelSeriesData seriesData, SeriesType seriesType, ZonedDateTime dateTime, ModelPoint modelPoint);
}
