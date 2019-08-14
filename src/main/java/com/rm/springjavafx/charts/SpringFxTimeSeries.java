package com.rm.springjavafx.charts;

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
public class SpringFxTimeSeries {

  private final int datasetId;
  private final String name;

  private final Property<TimeSeries<?>> seriesProperty = new SimpleObjectProperty<>();
  private final Property<Function<TimeStepValue<?>, Double>> valueAccessorProperty = new SimpleObjectProperty<>();
  private final Color lineColor;

  /**
   *
   */
  public SpringFxTimeSeries() {
    TimeSeriesDataset conf = this.getClass().getDeclaredAnnotation(TimeSeriesDataset.class);
    this.name = conf.name();
    this.datasetId = conf.dataset();
    this.lineColor = Color.web(conf.lineColorHex());
  }

  /**
   *
   */
  public void validate() throws Exception {
    Objects.requireNonNull(this.valueAccessorProperty.getValue(), "value accesor cannot be null");
  }

  /**
   *
   */
  public Color getLineColor() {
    return lineColor;
  }

  /**
   *
   */
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
  public Shape getShape() {
    double radius = 4;
    Ellipse2D e =new Ellipse2D.Double(-radius/2., -radius/2., radius, radius);
    return e;
  }

  /**
   *
   */
  public String getKey() {
    return name;
  }

  /**
   *
   * @return
   */
  int getDatasetId() {
    return this.datasetId;
  }

  /**
   *
   */
  public void setTimeSeries(TimeSeries<?> timeseries) {
    this.seriesProperty.setValue(timeseries);
  }

  /**
   *
   * @return
   */
  public ReadOnlyProperty<TimeSeries<?>> seriesProperty() {
    return this.seriesProperty;
  }

  /**
   *
   * @return
   */
  public TimeSeries<?> getSeries() {
    return seriesProperty.getValue();
  }

  /**
   *
   * @return
   */
  public Property<Function<TimeStepValue<?>, Double>> valueAccessorProperty() {
    return this.valueAccessorProperty;
  }

  /**
   *
   * @return
   */
  Function<TimeStepValue<?>, Double> getValueAccessor() {
    return this.valueAccessorProperty.getValue();
  }

  /**
   * 
   * @return 
   */
  Stroke getLineStroke() {
    return new BasicStroke(2); 
  }
}
