package com.rm.springjavafx.charts.xy;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author Ricardo Marquez
 */
public class SeriesRenderer {

  private final XYChartPane chart;

  private final Map<String, SpringFxXYDataSet> datasets;
  
  /**
   * 
   * @param chart
   * @param datasets 
   */
  SeriesRenderer(XYChartPane chart, Map<String, SpringFxXYDataSet> datasets) {
    this.chart = chart;
    this.datasets = datasets;
    
  }

  /**
   *
   * @param collection
   * @param datasetId
   */
  XYItemRenderer resetRenderer(JFreeDataSet collection, int datasetId) {
    XYDataSetGroup datasetgroup = this.chart.getDatasetgroup(datasetId);
    XYItemRenderer renderer = this.chart.getPlot().getRenderer(datasetId);
    for (SpringFxXYDataSet dataset : this.datasets.values()) {
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
  private void setTooltip(XYItemRenderer renderer, int seriesIndex) {
    StandardXYToolTipGenerator ttg = new StandardXYToolTipGenerator(
      StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
      new DecimalFormat("0.00"), new DecimalFormat("0.00")) {
        @Override
        public String generateToolTip(XYDataset dataset, int series, int item) {
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
  private boolean getVisibility(SpringFxXYDataSet dataset) {
    String key = dataset.getKey();
    List<String> value = this.chart.visibleDatasetsProperty().getValue();
    boolean visible = value == null ? true : value.contains(key);
    return visible;
  }

}
