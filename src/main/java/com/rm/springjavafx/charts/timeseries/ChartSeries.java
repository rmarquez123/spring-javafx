package com.rm.springjavafx.charts.timeseries;

import common.timeseries.TimeSeries;
import common.timeseries.TimeStepValue;
import java.awt.Shape;
import java.awt.Stroke;
import java.util.function.Function;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;
import javafx.scene.paint.Color;

/**
 *
 * @author Ricardo Marquez
 */
public interface ChartSeries {

  /**
   *
   */
  public String getKey();

  /**
   *
   */
  public Color getLineColor();

  /**
   *
   */
  public java.awt.Color getLineColorAwt();

  /**
   *
   * @return
   */
  public TimeSeries<?> getSeries();

  /**
   *
   * @return
   */
  public Shape getShape();

  /**
   *
   * @return
   */
  public ReadOnlyProperty<TimeSeries<?>> seriesProperty();

  /**
   *
   */
  public void setTimeSeries(TimeSeries<?> timeseries);

  /**
   *
   */
  public void validate() throws Exception;

  /**
   *
   * @return
   */
  public Property<Function<TimeStepValue<?>, Double>> valueAccessorProperty();

  /**
   *
   * @return
   */
  public Function<TimeStepValue<?>, Double> getValueAccessor();

  /**
   *
   * @return
   */
  public Stroke getLineStroke();

  /**
   *
   * @return
   */
  public int getDatasetId();
}
