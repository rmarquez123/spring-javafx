package com.rm.springjavafx.charts.category;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;

/**
 *
 * @author Ricardo Marquez
 */
public class SeriesRenderer {

  private final CategoryChartPane chart;

  private final Map<String, SpringFxCategoryDataSet> datasets;
  
  /**
   * 
   * @param chart
   * @param datasets 
   */
  SeriesRenderer(CategoryChartPane chart, Map<String, SpringFxCategoryDataSet> datasets) {
    this.chart = chart;
    this.datasets = datasets;
    
  }

  /**
   *
   * @param collection
   * @param datasetId
   */
  CategoryItemRenderer resetRenderer(JFreeCategoryDataSet collection, int datasetId) {
    CategoryFxDataSetGroup datasetgroup = this.chart.getDatasetgroup(datasetId);
    CategoryItemRenderer renderer = this.chart.getPlot().getRenderer(datasetId);
    for (SpringFxCategoryDataSet dataset : this.datasets.values()) {
      if (Objects.equals(dataset.getDatasetId(), datasetId)) {
        int seriesIndex = collection.getSeriesIndex(dataset.getKey());
        RenderArguments args = new RenderArguments(dataset, datasetgroup, renderer, seriesIndex);
        this.setSeriesRenderer(args);
        this.setTooltip(renderer, seriesIndex);
      }
    }
    return renderer;
  }
  
  /**
   * 
   * @param renderer
   * @param seriesIndex
   * @param dataset 
   */
  private void setSeriesRenderer(RenderArguments args) {
    PlotType plotType = args.datasetgroup.plotType(); 
    plotType.setSeriesRenderer(args);
    boolean visibility = this.getVisibility(args.dataset);
    args.renderer.setSeriesVisible(args.seriesIndex, visibility, true);
  }
  
  /**
   * 
   * @param renderer
   * @param seriesIndex 
   */
  private void setTooltip(CategoryItemRenderer renderer, int seriesIndex) {
    StandardCategoryToolTipGenerator ttg = new StandardCategoryToolTipGenerator(
      StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT, new DecimalFormat("0.00")) {
        @Override
        public String generateToolTip(CategoryDataset dataset, int series, int item) {
          String tooltip = super.generateToolTip(dataset, series, item);
          return tooltip;
        }
      };
    renderer.setSeriesToolTipGenerator(seriesIndex, ttg);
  }

  /**
   *
   * @param dataset
   */
  private boolean getVisibility(SpringFxCategoryDataSet dataset) {
    String key = dataset.getKey();
    List<String> value = this.chart.visibleDatasetsProperty().getValue();
    boolean visible = value == null ? true : value.contains(key);
    return visible;
  }

}
