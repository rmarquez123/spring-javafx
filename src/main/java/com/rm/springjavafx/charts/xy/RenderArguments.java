package com.rm.springjavafx.charts.xy;

import org.jfree.chart.renderer.xy.XYItemRenderer;

/**
 *
 * @author Ricardo Marquez
 */
public class RenderArguments {
  final XYItemRenderer renderer;
  final XYDataSetGroup datasetgroup;
  final int seriesIndex;
  final SpringFxXYDataSet dataset;

  /**
   * 
   * @param renderer
   * @param seriesIndex
   * @param dataset 
   */
  public RenderArguments(SpringFxXYDataSet dataset, XYDataSetGroup datasetgroup, //  
    XYItemRenderer renderer, int seriesIndex) {
    this.datasetgroup = datasetgroup;
    this.renderer = renderer;
    this.seriesIndex = seriesIndex;
    this.dataset = dataset;
  }
  
}
