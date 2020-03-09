package com.rm.springjavafx.charts.timeseries;

import common.bindings.RmBindings;
import common.timeseries.TimeStepValue;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;

/**
 *
 * @author Ricardo Marquez
 */
public final class TimeSeriesCollectionManager {

  /**
   *
   */
  private final XYPlot plot;
  private final TimeSeriesChartPane chart;
  private final Map<String, SpringFxTimeSeries> datasets = new HashMap<>();

  /**
   *
   * @param plot
   */
  public TimeSeriesCollectionManager(TimeSeriesChartPane chart) {
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
  public TimeSeriesChartPane getChart() {
    return chart;
  }

  /**
   *
   * @param dataset
   */
  public void addDataSet(SpringFxTimeSeries dataset) {
    if (this.datasets.containsKey(dataset.getKey())) {
      throw new IllegalArgumentException(
        String.format("Data set '%s' cannot be added twice", dataset.getKey()));
    }
    this.datasets.put(dataset.getKey(), dataset);
    int datasetId = dataset.getDatasetId();
    TimeSeriesCollection collection = (TimeSeriesCollection) plot.getDataset(datasetId);
    if (collection == null) {
      throw new IllegalStateException(
        String.format("No timeseries collection for datasetId = '%d'", datasetId));
    }

    RmBindings.bindActionOnAnyChange(() -> {
      updateChartDataSet(dataset, collection);
    }, dataset.valueAccessorProperty(), dataset.seriesProperty());
    updateChartDataSet(dataset, collection);
  }

  /**
   *
   * @param dataset
   * @param collection
   */
  private void updateChartDataSet(SpringFxTimeSeries dataset, TimeSeriesCollection collection) {
    this.updateSeries(dataset, collection);
    this.resetRenderer(collection, dataset.getDatasetId());
    this.updateChartDatasetsProperty();
  }

  /**
   *
   * @param dataset
   * @param collection
   */
  private synchronized void updateSeries(SpringFxTimeSeries dataset, TimeSeriesCollection collection) {
    common.timeseries.TimeSeries<?> series = dataset.getSeries();
    Function<TimeStepValue<?>, Double> accessor = dataset.getValueAccessor();
    if (accessor == null) {
      Logger.getLogger(TimeSeriesCollectionManager.class.getName()).log(Level.WARNING,
        String.format("accessor is not defined for dataset '%s'", dataset.getKey()));
    }
    JfcSeriesBuilder builder = new JfcSeriesBuilder(dataset, dataset.getKey());
    if (series != null && accessor != null) {
      series.forEach(builder::appendTimeStepValue);
    }
    TimeSeries old = collection.getSeries(dataset.getKey());
    collection.setNotify(false);
    if (old != null) {
      collection.removeSeries(old);
    }
    collection.addSeries(builder.build());
  }

  /**
   *
   * @param collection
   * @param datasetId
   */
  private void resetRenderer(TimeSeriesCollection collection, int datasetId) {
    XYItemRenderer renderer = new DefaultXYItemRenderer();
    for (SpringFxTimeSeries dataset1 : this.datasets.values()) {
      if (Objects.equals(dataset1.getDatasetId(), datasetId)) {
        int seriesIndex = collection.getSeriesIndex(dataset1.getKey());
        renderer.setSeriesPaint(seriesIndex, dataset1.getLineColorAwt(), true);
        renderer.setSeriesStroke(seriesIndex, dataset1.getLineStroke(), true);
        renderer.setSeriesShape(seriesIndex, dataset1.getShape(), true);
        renderer.setSeriesVisible(seriesIndex, this.getVisibility(dataset1), true);
        
        String formatOption = StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT;
        SimpleDateFormat xformat = new SimpleDateFormat("d-MMM-yyyy HH:mm");
        DecimalFormat yformat = new DecimalFormat("0.00");
        XYToolTipGenerator tooltipgenerator //
          = new StandardXYToolTipGenerator(formatOption, xformat, yformat);
        renderer.setSeriesToolTipGenerator(seriesIndex, tooltipgenerator);
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
      for (Map.Entry<String, SpringFxTimeSeries> entry : this.datasets.entrySet()) {
        SpringFxTimeSeries dataset = entry.getValue();
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
      SpringFxTimeSeries dataset = this.datasets.get(key);
      setVisibility(dataset, Boolean.TRUE);
    }
  }

  /**
   *
   * @param dataset
   */
  private boolean getVisibility(SpringFxTimeSeries dataset) {
    String key = dataset.getKey();
    List<String> value = this.chart.visibleDatasetsProperty().getValue();
    boolean visible = value == null ? true : value.contains(key);
    return visible;
  }

  /**
   *
   */
  private void updateChartDatasetsProperty() {
    ArrayList<String> chartIds = new ArrayList<>(this.datasets.keySet());
    this.chart.writableDatasetsProperty().setValue(chartIds);
  }

  /**
   *
   * @param dataset
   */
  private void setVisibility(SpringFxTimeSeries dataset) {
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
  private void setVisibility(SpringFxTimeSeries dataset, boolean visible) {
    int datasetId = dataset.getDatasetId();
    String key = dataset.getKey();
    TimeSeriesCollection collection = (TimeSeriesCollection) plot.getDataset(datasetId);
    int seriesIndex = collection.getSeriesIndex(key);
    this.plot.getRenderer(datasetId).setSeriesVisible(seriesIndex, visible, Boolean.TRUE);
  }
  
  /**
   *
   */
  static class JfcSeriesBuilder {

    private final TimeSeries jfcSeries;
    private final SpringFxTimeSeries dataset;

    JfcSeriesBuilder(SpringFxTimeSeries dataset, String seriesId) {
      this.jfcSeries = new TimeSeries(seriesId);
      this.dataset = dataset;
    }

    /**
     *
     * @param timestepvalue
     */
    void appendTimeStepValue(TimeStepValue timestepvalue) {
      Function<TimeStepValue<?>, Double> accessor = dataset.getValueAccessor();
      ZonedDateTime dateTime = timestepvalue.getZoneDateTime();
      Date time = Date.from(dateTime.toOffsetDateTime()
        .atZoneSimilarLocal(ZoneId.systemDefault()).toInstant());
      Double value = accessor.apply(timestepvalue);
      Second second = new Second(time);
      TimeSeriesDataItem item = new TimeSeriesDataItem(second, value);
      this.jfcSeries.add(item, false);
    }

    /**
     *
     */
    TimeSeries build() {
      return this.jfcSeries;
    }
  }

}
