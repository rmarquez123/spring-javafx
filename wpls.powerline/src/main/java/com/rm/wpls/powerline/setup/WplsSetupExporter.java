package com.rm.wpls.powerline.setup;

import com.rm.springjavafx.properties.DateRange;
import com.rm.wpls.powerline.TerrainData;
import com.rm.wpls.powerline.TransmissionLine;
import com.rm.wpls.powerline.TransmissionLines;
import com.rm.wpls.powerline.WindRecords;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;
import gov.inl.glass3.weather.WeatherStation;
import gov.inl.glass3.weather.WeatherStations;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.mutable.MutableObject;

/**
 *
 * @author Ricardo Marquez
 */
public final class WplsSetupExporter {

  private static final String NEW_LINE = "\r\n";
  private final Project project;

  /**
   *
   * @param project
   */
  public WplsSetupExporter(Project project) {
    this.project = project;
  }

  /**
   *
   * @param transmissionLines
   * @param stations
   */
  void exportStations(TransmissionLines transmissionLines, WeatherStations stations) {
    FileWriter writer = null;
    try {
      File exportDir = this.project.getExportDir();
      if (!exportDir.exists()) {
        exportDir.mkdirs();
      }
      File output = new File(exportDir, "powerline_conf.txt");
      boolean append = false;

      writer = new FileWriter(output, append);
      writer.write(NEW_LINE);
      for (WeatherStation station : stations.asList()) {
        String csq = this.stationToText(station);
        writer.append(csq).append(NEW_LINE);
      }
      writer.write(NEW_LINE);
      int index = 0;
      for (TransmissionLine line : transmissionLines.asList()) {
        index++;
        String csq = this.transmissionLineToText(line, index);
        writer.append(csq).append(NEW_LINE);
      }
      writer.flush();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    } finally {
      try {
        if (writer != null) {
          writer.close();
        }
      } catch (IOException ex) {
        Logger.getLogger(WplsSetupExporter.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  /**
   *
   * @param stations
   */
  void exportWeatherRecords(WindRecords records) {
    for (WeatherStation station : records.getWeatherStations()) {
      try {
        String name = station.getName();
        DateRange dateRange = records.getDateRange(station);
        Date startDt = dateRange.getStartDate();
        Date endDt = dateRange.getEndDate();
        SimpleDateFormat MMMYYFormat = new SimpleDateFormat("MMM YY");
        File file = new File(this.project.getExportDir(), name + ".txt");
        FileWriter writer = new FileWriter(file, false);
        writer.write("version            : 48                     ");
        writer.append(NEW_LINE);
        writer.write("site name          : " + name);
        writer.append(NEW_LINE);
        writer.write("measurement period : "
          + (startDt == null ? "" : MMMYYFormat.format(startDt)) + " - "
          + (endDt == null ? "" : MMMYYFormat.format(endDt)) + "");
        writer.append(NEW_LINE);
        writer.write("coordinate system  : 3 ");
        writer.append(NEW_LINE);
        writer.write("measurement height : 10.0");
        writer.append(NEW_LINE);
        writer.write("rec nr:   year:   mon:  date: hour: min:  dir:     speed:  SDspeed:");
        MutableObject<Integer> counter = new MutableObject<>(0);
        DateFormat formatter = new SimpleDateFormat("yyyy\tMM\tdd\tHH\tmm");
        records.forEachRecord(station, (record) -> {
          counter.setValue(counter.getValue() + 1);
          try {
            writer.append(NEW_LINE);
            writer.append("\t").append(String.valueOf(counter.getValue()))
              .append("\t").append(formatter.format(record.getDate()))
              .append("\t").append(String.valueOf(record.getWindDir()))
              .append("\t").append(String.valueOf(record.getWindSpeed()));
          } catch (Exception ex) {
            throw new RuntimeException(ex);
          }
        });
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  /**
   *
   * @param terrainData
   */
  void exportTerrain(TerrainData terrainData) {
    FileWriter writer = null;
    try {
      File exportDir = this.project.getExportDir();
      File output = new File(exportDir, "grid.gws");
      writer = new FileWriter(output, false);

      int numRows = terrainData.getNumRows();
      int numCols = terrainData.getNumCols();
      Envelope env = terrainData.getExtent();
      final int columnsLimit = 10;
      writer.append("WindSim version    : 470")
        .append(NEW_LINE).append(NEW_LINE);
      writer.append("area name          : power_line")
        .append(NEW_LINE).append(NEW_LINE);
      String tab = "          ";
      writer.append("#nodes nxp nyp     :").append(tab).append(String.valueOf(numCols))
        .append(tab).append(String.valueOf(numRows)).append(NEW_LINE).append(NEW_LINE);
      writer.append("co-ordinate system : 3").append(NEW_LINE); 
      writer.append("ext. xmin xmax     :")
        .append(tab).append(String.valueOf(env.getMinX() + terrainData.getDeltaX()/2.0))
        .append(tab).append(String.valueOf(env.getMaxX() - terrainData.getDeltaX()/2.0))
        .append(NEW_LINE);
      writer.append("ext. ymin ymax     :")
        .append(tab).append(String.valueOf(env.getMinY()  + terrainData.getDeltaY()/2.0 ))
        .append(tab).append(String.valueOf(env.getMaxY()  - terrainData.getDeltaY()/2.0))
        .append(NEW_LINE);
      double minValue = terrainData.getMinValue();
      double maxValue = terrainData.getMaxValue();
      writer.append("ext. zmin zmax     :")
        .append(tab).append(String.valueOf(minValue))
        .append(tab).append(String.valueOf(maxValue))
        .append(NEW_LINE);

      writer.append("HEIGHT" + tab + ":").append(NEW_LINE);
      float no_data_value = -32768.0f;
      NumberFormat formatter = new DecimalFormat(".00000E00");
      
      for (int row = numRows - 1; row >= 0 ; row--) {
        int rowPlus1 = numRows - row;
        writer.append(tab).append(String.valueOf(rowPlus1)).append(NEW_LINE);
        for (int column = 0; column < numCols; column = column + columnsLimit) {
          float[] rowData = terrainData.getRowData(row, column, columnsLimit);
          
          String line = Arrays.asList(ArrayUtils.toObject(rowData)).stream()
            .map((e) -> {
              double number = (Objects.equals(e, no_data_value) ? 0.00001 : e); 
              String text = String.format("%16s", "0"+formatter.format(number).replace("E0", "E+0"));
              return text;
            }).collect(Collectors.joining());
          writer.append(line).append(NEW_LINE);
        }
      }
      
      writer.append("ROUGHNESS" + tab + ":").append(NEW_LINE);
      
      for (int row = numRows - 1; row >= 0 ; row--) {
        int rowPlus1 = numRows - row;
        writer.append(tab).append(String.valueOf(rowPlus1)).append(NEW_LINE);
        for (int column = 0; column < numCols; column = column + columnsLimit) {
          float[] rowData = terrainData.getRowData(row, column, columnsLimit);
          
          String line = Arrays.asList(ArrayUtils.toObject(rowData)).stream()
            .map((e) -> {
              double number = (Objects.equals(e, no_data_value) ? 0.01 : e/10000.0*2);
              String text = String.format("%16s", "0"+formatter.format(number).replace("E0", "E+0"));
              return text;
            }).collect(Collectors.joining());
          writer.append(line).append(NEW_LINE);
        }
      }
      writer.flush();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    } finally {
      try {
        if (writer != null) {
          writer.close();
        }
      } catch (IOException ex) {
        Logger.getLogger(WplsSetupExporter.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  /**
   *
   * @param station
   * @return
   */
  private String stationToText(WeatherStation station) {
    Coordinate coordinate = station.getGeometry().getCoordinate();
    String result = String.format("%s, %f, %f, 10", new Object[]{
      station.getName(), coordinate.x, coordinate.y
    });
    return result;
  }

  /**
   *
   * @param line
   * @return
   */
  private String transmissionLineToText(TransmissionLine line, int index) {
    Point center = line.getCenter();
    String result = String.format("1, %s, %d, %f, %f, 10", new Object[]{
      line.getLineName(), index, center.getX(), center.getY()
    });
    return result;
  }

}
