package com.rm.springjavafx.charts.timeseries;

import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 *
 * @author Ricardo Marquez
 */
public class TimeSeriesDatasetImpl implements TimeSeriesDataset {

  private final String chart;
  private final int dataset;
  private final String name;
  private final String lineColorHex;
  private final String visibilityProperty;

  public TimeSeriesDatasetImpl(String chart, int dataset, String name, String lineColorHex, String visibilityProperty) {
    this.chart = chart;
    this.dataset = dataset;
    this.name = name;
    this.lineColorHex = lineColorHex;
    this.visibilityProperty = visibilityProperty;
  }

  @Override
  public String chart() {
    return this.chart;
  }

  @Override
  public int dataset() {
    return this.dataset;
  }

  @Override
  public String name() {
    return this.name;
  }

  @Override
  public String lineColorHex() {
    return this.lineColorHex;
  }

  @Override
  public String visibilityProperty() {
    return this.visibilityProperty;
  }

  /**
   *
   * @return
   */
  @Override
  public Class<? extends Annotation> annotationType() {
    return TimeSeriesDataset.class;
  }

  public static class Builder {

    private String chart;
    private int dataset;
    private String name;
    private String lineColorHex = "#000000";
    private String visibilityProperty = "";

    public Builder() {
    }

    public Builder setChart(String chart) {
      this.chart = chart;
      return this;
    }

    public Builder setDataset(int dataset) {
      this.dataset = dataset;
      return this;
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setLineColorHex(String lineColorHex) {
      this.lineColorHex = lineColorHex;
      return this;
    }

    public Builder setVisibilityProperty(String visibilityProperty) {
      this.visibilityProperty = visibilityProperty;
      return this;
    }
    
    public TimeSeriesDatasetImpl build() {
      Objects.requireNonNull(this.name);
      Objects.requireNonNull(this.chart);
      Objects.requireNonNull(this.dataset);
      Objects.requireNonNull(this.lineColorHex);
      Objects.requireNonNull(this.visibilityProperty);
      return new TimeSeriesDatasetImpl(chart, dataset, name, lineColorHex, visibilityProperty); 
    }
    
    
  }
}
