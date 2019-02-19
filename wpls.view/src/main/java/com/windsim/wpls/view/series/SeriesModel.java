package com.windsim.wpls.view.series;

import gov.inl.glass3.modelpoints.ModelPoint;
import java.util.List;
import javafx.scene.chart.XYChart;

/**
 *
 * @author Ricardo Marquez
 */
public interface SeriesModel {

  /**
   *
   * @param seriesData
   * @param modelPoint
   * @param result
   */
  public void displayRecords(ModelSeriesData seriesData, ModelPoint modelPoint, List<XYChart.Data<Long, Double>> result);
  
}
