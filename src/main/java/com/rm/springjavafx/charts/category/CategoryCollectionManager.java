package com.rm.springjavafx.charts.category;

import common.bindings.RmBindings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.ReadOnlyProperty;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;

/**
 *
 * @author Ricardo Marquez
 */
public final class CategoryCollectionManager {
  
  /**
   *
   */
  private final CategoryChartPane chart;
  private final Map<String, SpringFxCategoryDataSet> datasets = new HashMap<>();
  private final SeriesRenderer renderer; 
  private final CategoryPlot plot;

  /**
   *
   * @param plot
   */
  public CategoryCollectionManager(CategoryChartPane chart) {
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
  public CategoryChartPane getChart() {
    return chart;
  }

  /**
   *
   * @param dataset
   */
  public void addDataSet(SpringFxCategoryDataSet dataset) {
    if (this.datasets.containsKey(dataset.getKey())) {
      throw new IllegalArgumentException(
        String.format("Data set '%s' cannot be added twice", dataset.getKey()));
    }
    this.datasets.put(dataset.getKey(), dataset);
    int datasetId = dataset.getDatasetId();
    JFreeCategoryDataSet collection = (JFreeCategoryDataSet) plot.getDataset(datasetId);
    if (collection == null) {
      throw new IllegalStateException(
        String.format("No timeseries collection for datasetId = '%d'", datasetId));
    }
    
    ReadOnlyProperty<CategoryValues> seriesProperty = dataset.seriesProperty();
    RmBindings.bindActionOnAnyChange(() -> this.updateSeries(dataset), seriesProperty);
    this.updateSeries(dataset);

  }

  /**
   *
   * @param dataset
   * @param collection
   */
  private synchronized void updateSeries(SpringFxCategoryDataSet dataset) {
    CategoryValues series = dataset.getSeries();
    int datasetId = dataset.getDatasetId();
    JFreeCategoryDataSet jfdataset = (JFreeCategoryDataSet) this.plot.getDataset(datasetId);
    jfdataset.addOrUpdate(series);
    CategoryItemRenderer r = renderer.resetRenderer(jfdataset, datasetId);
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
      for (Map.Entry<String, SpringFxCategoryDataSet> entry : this.datasets.entrySet()) {
        SpringFxCategoryDataSet dataset = entry.getValue();
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
      SpringFxCategoryDataSet dataset = this.datasets.get(key);
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
  private void setVisibility(SpringFxCategoryDataSet dataset) {
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
  private void setVisibility(SpringFxCategoryDataSet dataset, boolean visible) {
    int datasetId = dataset.getDatasetId();
    String key = dataset.getKey();
    JFreeCategoryDataSet collection = (JFreeCategoryDataSet) plot.getDataset(datasetId);
    int seriesIndex = collection.getSeriesIndex(key);
    this.plot.getRenderer(datasetId).setSeriesVisible(seriesIndex, visible, Boolean.TRUE);
  }
}
