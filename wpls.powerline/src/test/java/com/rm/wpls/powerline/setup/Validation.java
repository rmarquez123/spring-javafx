package com.rm.wpls.powerline.setup;

import com.rm.wpls.powerline.WindRecord;
import com.rm.wpls.powerline.io.tws.TwsWindRecordsReader;
import gov.inl.glass3.linesolver.impl.files.FilesLookupTablesLoader;
import gov.inl.glass3.linesolver.impl.files.FilesModelPointsLoader;
import gov.inl.glass3.modelpoints.ModelPoint;
import gov.inl.glass3.modelpoints.ModelPoints;
import gov.inl.glass3.weather.WeatherRecord;
import gov.inl.glass3.windmodel.LookupTables;
import java.io.File;
import java.io.FileInputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.measure.Measure;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import junitparams.Parameters;
import org.junit.Test;

/**
 *
 * @author Ricardo Marquez
 */
public class Validation {

  @Test
  @Parameters({
    "true", 
    "false"
  })
  public void evaluatePredictedWeatherRecords(boolean useLookup) throws Exception {
    FilesLookupTablesLoader lookupTablesLoader = new FilesLookupTablesLoader(
      new FileInputStream("C:\\Users\\Ricardo Marquez\\windsim_runs\\idaho_power_validation\\power_line_outputs\\LookUpTable.csv"),
      FilesLookupTablesLoader.SECTION_NAME_NA);
    LookupTables lookupTables = lookupTablesLoader.loadLookupTables();

    File modelPointsFile = new File("C:\\Users\\Ricardo Marquez\\windsim_runs\\idaho_power_validation\\power_line_outputs\\MidPointData.csv");
    FilesModelPointsLoader loader = new FilesModelPointsLoader(new FileInputStream(modelPointsFile), FilesModelPointsLoader.SECTION_NAME_NA);
    ModelPoints modelPoints = loader.loadModelPoints();

    FileInputStream refWeatherRecordsInputStream = new FileInputStream("C:\\Users\\Ricardo Marquez\\windsim_runs\\idaho_power_validation\\WS3.tws");
    List<WindRecord> refWindRecords = new TwsWindRecordsReader(refWeatherRecordsInputStream, "WS3").loadRecords();
    Map<Date, WindRecord> refWindRecordsMap = new HashMap<>();
    for (WindRecord refWindRecord : refWindRecords) {
      Date date = refWindRecord.getDate();
      refWindRecordsMap.put(date, refWindRecord);
    }
    Map<String, Double> comparisons = new HashMap<>();
    for (ModelPoint modelPoint : modelPoints) {
      String modelPointId = modelPoint.getModelPointId();
      String weatherStation = this.getWeatherStation(modelPointId);
      FileInputStream weatherRecordsInputStream = new FileInputStream("C:\\Users\\Ricardo Marquez\\windsim_runs\\idaho_power_validation\\" + weatherStation + ".tws");
      List<WindRecord> windRecords = new TwsWindRecordsReader(weatherRecordsInputStream, weatherStation).loadRecords();
      double sum = 0.0;
      int count = 0;
      for (WindRecord windRecord : windRecords) {
        Date date = windRecord.getDate();
        WindRecord refWindRecord = refWindRecordsMap.get(date);

        if (refWindRecord != null) {
          
          double diffSquared;
          if (!useLookup) {
            diffSquared = windRecord.vectorSquaredDifference(refWindRecord);
          } else {
            WeatherRecord adjusted = lookupTables.applyAdjustments(modelPointId, new WeatherRecord.Builder()
              .setStationId(weatherStation)
              .setDateTime(ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()))
              .setWindAngle(Measure.valueOf(windRecord.getWindDir(), NonSI.DEGREE_ANGLE))
              .setWindSpeed(Measure.valueOf(windRecord.getWindSpeed(), SI.METRES_PER_SECOND))
              .build()
            );
            WindRecord adjustedWindRecord = new WindRecord(modelPointId, date,
              adjusted.getWindSpeed().doubleValue(SI.METRES_PER_SECOND),
              adjusted.getWindAngle());
            diffSquared = adjustedWindRecord.vectorSquaredDifference(refWindRecord);
          }
          System.out.println(Math.sqrt(diffSquared));
          sum += diffSquared;
          count++;
        }
        
      }
      double rmse = Math.sqrt(sum / count);
      comparisons.put(weatherStation, rmse);
    }
    System.out.println(comparisons);
  }

  /**
   *
   * @param modelPointId
   * @return
   */
  public String getWeatherStation(String modelPointId) {
    return "WS" + modelPointId.split("_")[1];
  }

}
