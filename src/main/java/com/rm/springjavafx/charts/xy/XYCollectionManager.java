package com.rm.springjavafx.charts.xy;

import common.bindings.RmBindings;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;

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

  /**
   *
   * @param plot
   */
  public XYCollectionManager(XYChartPane chart) {
    this.chart = chart;
    this.plot = chart.getPlot();
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
    JFreeXYDataSet collection = (JFreeXYDataSet) plot.getDataset(datasetId);
    if (collection == null) {
      throw new IllegalStateException(
        String.format("No timeseries collection for datasetId = '%d'", datasetId));
    }
    RmBindings.bindActionOnAnyChange(
      () -> this.updateSeries(dataset, collection),
       dataset.seriesProperty());

    this.updateSeries(dataset, collection);

  }

  /**
   *
   * @param dataset
   * @param collection
   */
  private synchronized void updateSeries(SpringFxXYDataSet dataset, JFreeXYDataSet collection) {
    XYValues series = dataset.getSeries();
    JFreeXYDataSet jfdataset = (JFreeXYDataSet) this.plot.getDataset(dataset.getDatasetId());
    jfdataset.addOrUpdate(series);
    this.resetRenderer(jfdataset, dataset.getDatasetId());
    this.updateChartDatasetsProperty();
  }

  /**
   *
   * @param collection
   * @param datasetId
   */
  private void resetRenderer(JFreeXYDataSet collection, int datasetId) {
    XYItemRenderer renderer = new DefaultXYItemRenderer();
    for (SpringFxXYDataSet dataset1 : this.datasets.values()) {
      if (Objects.equals(dataset1.getDatasetId(), datasetId)) {
        int seriesIndex = collection.getSeriesIndex(dataset1.getKey());
        renderer.setSeriesPaint(seriesIndex, dataset1.getLineColorAwt(), true);
        renderer.setSeriesStroke(seriesIndex, dataset1.getLineStroke(), true);
        renderer.setSeriesShape(seriesIndex, dataset1.getShape(), true);
        renderer.setSeriesVisible(seriesIndex, this.getVisibility(dataset1), true);
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
    }
    this.plot.setRenderer(datasetId, renderer);
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
   * @param dataset
   */
  private boolean getVisibility(SpringFxXYDataSet dataset) {
    String key = dataset.getKey();
    List<String> value = this.chart.visibleDatasetsProperty().getValue();
    boolean visible = value == null ? true : value.contains(key);
    return visible;
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
    JFreeXYDataSet collection = (JFreeXYDataSet) plot.getDataset(datasetId);
    int seriesIndex = collection.getSeriesIndex(key);
    this.plot.getRenderer(datasetId).setSeriesVisible(seriesIndex, visible, Boolean.TRUE);
  }
}
