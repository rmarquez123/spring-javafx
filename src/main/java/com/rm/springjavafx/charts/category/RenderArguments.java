package com.rm.springjavafx.charts.category;

import org.jfree.chart.renderer.category.CategoryItemRenderer;

/**
 *
 * @author Ricardo Marquez
 */
public class RenderArguments {
  final CategoryItemRenderer renderer;
  final CategoryFxDataSetGroup datasetgroup;
  final int seriesIndex;
  final SpringFxCategoryDataSet dataset;

  /**
   * 
   * @param renderer
   * @param seriesIndex
   * @param dataset 
   */
  public RenderArguments(SpringFxCategoryDataSet dataset, CategoryFxDataSetGroup datasetgroup, //  
    CategoryItemRenderer renderer, int seriesIndex) {
    this.datasetgroup = datasetgroup;
    this.renderer = renderer;
    this.seriesIndex = seriesIndex;
    this.dataset = dataset;
  }
  
}
