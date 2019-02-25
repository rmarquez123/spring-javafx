package com.windsim.wpls.view.map.impl;

import com.rm.panzoomcanvas.impl.points.PointShapeSymbology;
import com.windsim.wpls.view.series.ModelSeriesData;
import com.windsim.wpls.view.series.SeriesType;
import common.colormap.ColorModel;
import common.colormap.LinearRangeColorModel;
import gov.inl.glass3.modelpoints.ModelPoint;
import gov.inl.glass3.weather.WeatherRecord;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;

/**
 *
 * @author Ricardo Marquez
 */
public enum DefaultModelPointSeriesSymbolizer {

  /**
   *
   */
  AIRTEMPERATURE(SeriesType.STATION_AMB_TEMPERATURE) {
    @Override
    public PointShapeSymbology getSymology(ModelSeriesData seriesData,
      ZonedDateTime dateTime, ModelPoint modelPoint) {
      PointShapeSymbology result = new PointShapeSymbology();
      String stationId = modelPoint.getWeatherStationId();
      Set<WeatherRecord> series = seriesData.getWeatherRecords().get(stationId);
      ColorModel colorModel = new LinearRangeColorModel(
        new Stop(-10.0, Color.BLACK),
        new Stop(10.0, Color.WHITE));
      for (WeatherRecord timeStepValue : series) {
        if (Objects.equals(timeStepValue.getDateTime(), dateTime)) {
          double value = timeStepValue.getAmbientTemp().getValue().doubleValue();
          Color color = colorModel.getColor(value);
          
          result.fillColorProperty().setValue(color);
        }
      }
      return result;
    }

  }, WIND_SPEED(SeriesType.STATION_AMB_WIND_SPEED) {
    @Override
    public PointShapeSymbology getSymology(ModelSeriesData seriesData,
      ZonedDateTime dateTime, ModelPoint modelPoint) {
      PointShapeSymbology result = new PointShapeSymbology();
      String stationId = modelPoint.getWeatherStationId();
      Set<WeatherRecord> series = seriesData.getWeatherRecords().get(stationId);
      ColorModel colorModel = new LinearRangeColorModel(
        new Stop(0, Color.BLACK),
        new Stop(20, Color.WHITE));
      for (WeatherRecord timeStepValue : series) {
        if (Objects.equals(timeStepValue.getDateTime(), dateTime)) {
          double value = timeStepValue.getWindSpeed().getValue().doubleValue();
          Color color = colorModel.getColor(value);
          result.fillColorProperty().setValue(color);
        }
      }
      return result;
    }

  }, WIND_DIR(SeriesType.STATION_AMB_WIND_DIR) {
    @Override
    public PointShapeSymbology getSymology(ModelSeriesData seriesData,
      ZonedDateTime dateTime, ModelPoint modelPoint) {
      PointShapeSymbology result = new PointShapeSymbology();
      String stationId = modelPoint.getWeatherStationId();
      Set<WeatherRecord> series = seriesData.getWeatherRecords().get(stationId);
      ColorModel colorModel = new LinearRangeColorModel(
        new Stop(0, Color.BLACK),
        new Stop(100, Color.WHITE));
      for (WeatherRecord timeStepValue : series) {
        if (Objects.equals(timeStepValue.getDateTime(), dateTime)) {
          double value = timeStepValue.getWindAngle();
          Color color = colorModel.getColor(value);
          result.fillColorProperty().setValue(color);
        }
      }
      return result;
    }
  }, SOLAR(SeriesType.STATION_AMB_SOLAR) {
    @Override
    public PointShapeSymbology getSymology(ModelSeriesData seriesData,
      ZonedDateTime dateTime, ModelPoint modelPoint) {
      PointShapeSymbology result = new PointShapeSymbology();
      String stationId = modelPoint.getWeatherStationId();
      Set<WeatherRecord> series = seriesData.getWeatherRecords().get(stationId);
      ColorModel colorModel = new LinearRangeColorModel(
        new Stop(0, Color.BLACK),
        new Stop(100, Color.WHITE));
      for (WeatherRecord timeStepValue : series) {
        if (Objects.equals(timeStepValue.getDateTime(), dateTime)) {
          double value = timeStepValue.getSolar();
          Color color = colorModel.getColor(value);
          result.fillColorProperty().setValue(color);
        }
      }
      return result;
    }
  };
  private final SeriesType seriesType;

  DefaultModelPointSeriesSymbolizer(SeriesType seriesType) {
    this.seriesType = seriesType;
  }

  /**
   *
   * @param seriesType
   * @return
   */
  static DefaultModelPointSeriesSymbolizer getInstance(SeriesType seriesType) {
    DefaultModelPointSeriesSymbolizer result = null;
    for (DefaultModelPointSeriesSymbolizer value : values()) {
      if (value.seriesType == seriesType) {
        result = value;
      }
    }
    return result;
  }

  /**
   *
   * @param seriesData
   * @param dateTime
   * @param modelPoint
   * @return
   */
  public abstract PointShapeSymbology getSymology(ModelSeriesData seriesData,
    ZonedDateTime dateTime, ModelPoint modelPoint);

}
