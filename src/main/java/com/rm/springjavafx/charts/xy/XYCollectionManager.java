package com.rm.springjavafx.charts.xy;

import common.bindings.RmBindings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.ReadOnlyProperty;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;

/**
 *
 * @author Ricardo Marquez
 */
public final class XYCollectionManager {
  
  /**
   *
   */
  private final XYPlot plot;
  private final XYChartPane chart;
  private final Map<String, SpringFxXYDataSet> datasets = new HashMap<>();
  private final SeriesRenderer renderer; 

  /**
   *
   * @param plot
   */
  public XYCollectionManager(XYChartPane chart) {
    this.chart = chart;
    this.plot = chart.getPlot();
    
    this.renderer = new SeriesRenderer(this.chart, this.datasets);
    this.chart.visibleDatasetsProperty().addListener((obs, old, change) -> {
      this.updateVisibilities();
    });
    this.updateChartDatasetsProperty();
  }

  /**
   *
   * @return
   */
  public XYChartPane getChart() {
    return chart;
  }

  /**
   *
   * @param dataset
   */
  public void addDataSet(SpringFxXYDataSet dataset) {
    if (this.datasets.containsKey(dataset.getKey())) {
      throw new IllegalArgumentException(
        String.format("Data set '%s' cannot be added twice", dataset.getKey()));
    }
    this.datasets.put(dataset.getKey(), dataset);
    int datasetId = dataset.getDatasetId();
    JFreeDataSet collection = (JFreeDataSet) plot.getDataset(datasetId);
    if (collection == null) {
      throw new IllegalStateException(
        String.format("No timeseries collection for datasetId = '%d'", datasetId));
    }
    
    ReadOnlyProperty<XYValues> seriesProperty = dataset.seriesProperty();
    RmBindings.bindActionOnAnyChange(() -> this.updateSeries(dataset), seriesProperty);
    this.updateSeries(dataset);

  }

  /**
   *
   * @param dataset
   * @param collection
   */
  private synchronized void updateSeries(SpringFxXYDataSet dataset) {
    XYValues series = dataset.getSeries();
    int datasetId = dataset.getDatasetId();
    JFreeDataSet jfdataset = (JFreeDataSet) this.plot.getDataset(datasetId);
    jfdataset.addOrUpdate(series);
    XYItemRenderer r = renderer.resetRenderer(jfdataset, datasetId);
    this.plot.setRenderer(datasetId, r);
    this.updateChartDatasetsProperty();
  }

  
  /**
   *
   */
  private void updateVisibilities() {
    List<String> value = this.chart.visibleDatasetsProperty().getValue();
    if (value == null) {
      this.allVisible();
    } else {
      for (Map.Entry<String, SpringFxXYDataSet> entry : this.datasets.entrySet()) {
        SpringFxXYDataSet dataset = entry.getValue();
        this.setVisibility(dataset);
      }
    }
  }

  /**
   *
   */
  private void allVisible() {
    this.plot.getDataset();
    for (String key : this.datasets.keySet()) {
      SpringFxXYDataSet dataset = this.datasets.get(key);
      setVisibility(dataset, Boolean.TRUE);
    }
  }

  /**
   *
   */
  private void updateChartDatasetsProperty() {
    this.chart.writableDatasetsProperty().setValue(new ArrayList<>(this.datasets.keySet()));
  }

  /**
   *
   * @param dataset
   */
  private void setVisibility(SpringFxXYDataSet dataset) {
    String key = dataset.getKey();
    List<String> value = this.chart.visibleDatasetsProperty().getValue();
    boolean visible = value == null ? true : value.contains(key);
    setVisibility(dataset, visible);
  }

  /**
   *
   * @param dataset
   * @param key
   * @param visible
   */
  private void setVisibility(SpringFxXYDataSet dataset, boolean visible) {
    int datasetId = dataset.getDatasetId();
    String key = dataset.getKey();
    JFreeDataSet collection = (JFreeDataSet) plot.getDataset(datasetId);
    int seriesIndex = collection.getSeriesIndex(key);
    this.plot.getRenderer(datasetId).setSeriesVisible(seriesIndex, visible, Boolean.TRUE);
  }
}
