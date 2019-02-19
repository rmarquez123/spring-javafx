package com.windsim.wpls.view.series;

import gov.inl.glass3.linesolver.ModelPointAmpacities;
import gov.inl.glass3.linesolver.ModelPointAmpacity;
import gov.inl.glass3.modelpoints.ModelPoint;
import gov.inl.glass3.weather.WeatherRecord;
import gov.inl.glass3.weather.WeatherRecords;
import java.util.List;
import java.util.Set;
import javafx.scene.chart.XYChart;

/**
 *
 * @author Ricardo Marquez
 */
public enum SeriesType implements SeriesModel {
  STATION_AMB_TEMPERATURE() {
    @Override
    public void displayRecords(ModelSeriesData seriesData, ModelPoint modelPoint, List<XYChart.Data<Long, Double>> result) {
      String stationId = modelPoint.getWeatherStationId();
      WeatherRecords weatherRecords = seriesData.getWeatherRecords();
      if (weatherRecords != null) {
        Set<WeatherRecord> records = weatherRecords.get(stationId);
        for (WeatherRecord record : records) {
          long timevalue = record.getDateTime().toEpochSecond();
          double value = record.getAmbientTemp().getValue().doubleValue();
          result.add(new XYChart.Data<>(timevalue, value));
        }
      }

    }
  },
  STATION_AMB_WIND_SPEED() {
    @Override
    public void displayRecords(ModelSeriesData seriesData, ModelPoint modelPoint, List<XYChart.Data<Long, Double>> result) {
      String stationId = modelPoint.getWeatherStationId();
      WeatherRecords weatherRecords = seriesData.getWeatherRecords();
      if (weatherRecords != null) {
        Set<WeatherRecord> records = weatherRecords.get(stationId);
        for (WeatherRecord record : records) {
          long timevalue = record.getDateTime().toEpochSecond();
          double random = record.getWindSpeed().getValue().doubleValue();
          result.add(new XYChart.Data<>(timevalue, random));
        }
      }

    }
  },
  STATION_AMB_WIND_DIR() {
    @Override
    public void displayRecords(ModelSeriesData seriesData, ModelPoint modelPoint, List<XYChart.Data<Long, Double>> result) {
      String stationId = modelPoint.getWeatherStationId();
      WeatherRecords weatherRecords = seriesData.getWeatherRecords();
      if (weatherRecords != null) {
        Set<WeatherRecord> records = weatherRecords.get(stationId);
        for (WeatherRecord record : records) {
          long timevalue = record.getDateTime().toEpochSecond();
          double random = record.getWindAngle();
          result.add(new XYChart.Data<>(timevalue, random));
        }
      }

    }
  },
  STATION_AMB_SOLAR() {
    @Override
    public void displayRecords(ModelSeriesData seriesData, ModelPoint modelPoint, List<XYChart.Data<Long, Double>> result) {
      String stationId = modelPoint.getWeatherStationId();
      WeatherRecords weatherRecords = seriesData.getWeatherRecords();
      if (weatherRecords != null) {
        Set<WeatherRecord> records = weatherRecords.get(stationId);
        for (WeatherRecord record : records) {
          long timevalue = record.getDateTime().toEpochSecond();
          double random = record.getSolar();
          result.add(new XYChart.Data<>(timevalue, random));
        }
      }

    }
  },
  LINE_AMPACITY() {
    @Override
    public void displayRecords(ModelSeriesData seriesData, ModelPoint modelPoint, List<XYChart.Data<Long, Double>> result) {
      ModelPointAmpacities modelPointAmpacities = seriesData.getModelPointAmpacities();
      if (modelPointAmpacities != null) {
        Set<ModelPointAmpacity> records = modelPointAmpacities.get(modelPoint.getModelPointId());
        for (ModelPointAmpacity record : records) {
          long timevalue = record.getDateTime().toEpochSecond();
          double random = record.getAmpacity().getValue().doubleValue();
          result.add(new XYChart.Data<>(timevalue, random));
        }
      }

    }
  },
  FCST_LINE_AMPACITY() {
    @Override
    public void displayRecords(ModelSeriesData seriesData, ModelPoint modelPoint, List<XYChart.Data<Long, Double>> result) {
      ModelPointAmpacities modelPointAmpacities = seriesData.getFcstedModelPointAmpacities();
      if (modelPointAmpacities != null) {
        Set<ModelPointAmpacity> records = modelPointAmpacities.get(modelPoint.getModelPointId());
        for (ModelPointAmpacity record : records) {
          long timevalue = record.getDateTime().toEpochSecond();
          double random = record.getAmpacity().getValue().doubleValue();
          result.add(new XYChart.Data<>(timevalue, random));
        }
        System.out.println("Number of records : " + records.size());
      }

    }
  };

  @Override
  public abstract void displayRecords(ModelSeriesData weatherRecords, ModelPoint modelPoint, List<XYChart.Data<Long, Double>> result);
}
