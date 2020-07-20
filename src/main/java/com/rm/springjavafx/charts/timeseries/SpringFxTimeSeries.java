package com.rm.springjavafx.charts.timeseries;

import common.timeseries.TimeSeries;
import common.timeseries.TimeStepValue;
import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.util.Objects;
import java.util.function.Function;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

/**
 *
 * @author Ricardo Marquez
 */
public class SpringFxTimeSeries implements ChartSeries {

  private final int datasetId;
  private final String name;

  private final Property<TimeSeries<?>> seriesProperty = new SimpleObjectProperty<>();
  private final Property<Function<TimeStepValue<?>, Double>> valueAccessorProperty = new SimpleObjectProperty<>();
  private final Color lineColor;
  private TimeSeriesDataset configuration;

  /**
   *
   */
  public SpringFxTimeSeries() {
    this.configuration = this.getClass().getDeclaredAnnotation(TimeSeriesDataset.class);
    this.name = this.configuration.name();
    this.datasetId = this.configuration.dataset();
    this.lineColor = Color.web(this.configuration.lineColorHex());
  }
  
  /**
   *
   */
  public SpringFxTimeSeries(TimeSeriesDataset conf) {
    this.configuration = conf;
    this.name = conf.name();
    this.datasetId = conf.dataset();
    this.lineColor = Color.web(conf.lineColorHex());
  }

  /**
   *
   */
  @Override
  public void validate() throws Exception {
    Objects.requireNonNull(this.valueAccessorProperty.getValue(), "value accesor cannot be null");
  }

  /**
   *
   */
  @Override
  public Color getLineColor() {
    return lineColor;
  }

  /**
   *
   */
  @Override
  public java.awt.Color getLineColorAwt() {
    float r = new Double(this.lineColor.getRed()).floatValue();
    float g = new Double(this.lineColor.getGreen()).floatValue();
    float b = new Double(this.lineColor.getBlue()).floatValue();
    return new java.awt.Color(r, g, b);
  }

  /**
   * 
   * @return 
   */
  @Override
  public Shape getShape() {
    double radius = 4;
    Ellipse2D e =new Ellipse2D.Double(-radius/2., -radius/2., radius, radius);
    return e;
  }

  /**
   *
   */
  @Override
  public String getKey() {
    return name;
  }

  /**
   *
   * @return
   */
  @Override
  public int getDatasetId() {
    return this.datasetId;
  }

  /**
   *
   */
  @Override
  public void setTimeSeries(TimeSeries<?> timeseries) {
    this.seriesProperty.setValue(timeseries);
  }

  /**
   *
   * @return
   */
  @Override
  public ReadOnlyProperty<TimeSeries<?>> seriesProperty() {
    return this.seriesProperty;
  }

  /**
   *
   * @return
   */
  @Override
  public TimeSeries<?> getSeries() {
    return seriesProperty.getValue();
  }

  /**
   *
   * @return
   */
  @Override
  public Property<Function<TimeStepValue<?>, Double>> valueAccessorProperty() {
    return this.valueAccessorProperty;
  }

  /**
   *
   * @return
   */
  @Override
  public Function<TimeStepValue<?>, Double> getValueAccessor() {
    return this.valueAccessorProperty.getValue();
  }

  /**
   * 
   * @return 
   */
  @Override
  public Stroke getLineStroke() {
    return new BasicStroke(2); 
  }

  /**
   * 
   * @return 
   */
  public TimeSeriesDataset getConfiguration() {
    return this.configuration;
  }
}
