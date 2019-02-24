package com.rm.wpls.powerline.setup;

import com.rm.wpls.powerline.TerrainData;
import com.rm.wpls.powerline.TransmissionLines;
import com.rm.wpls.powerline.WindRecords;
import com.vividsolutions.jts.geom.Envelope;
import gov.inl.glass3.weather.WeatherStations;

/**
 *
 * @author Ricardo Marquez
 */
public final class WplsSetup {

  private final WplsSetupSource setUpSource;
  private final WplsSetupExporter exporter;
  private final Project project;
  
  /**
   *
   * @param project
   * @param setUpSource
   */
  public WplsSetup(Project project, WplsSetupSource setUpSource) {
    this.project = project;
    this.setUpSource = setUpSource;
    this.exporter = new WplsSetupExporter(project);
  }

  /**
   *
   */
  public void preparePowerLineModule() {
    int srid = this.project.getSrid();
    
    TransmissionLines transmissionLines = this.setUpSource.getTransmissionLines(srid);
    Envelope env; 
    if (this.setUpSource.getFilterEnvelope() == null) {
      env = transmissionLines.getEnvelope();
    } else {
      env = this.setUpSource.getFilterEnvelope(); 
    }
    WeatherStations stations = this.setUpSource.getWeatherStations(srid, env); 
    this.exporter.exportStations(transmissionLines, stations);
    WindRecords records = this.setUpSource.getWeatherRecords(stations); 
    this.exporter.exportWeatherRecords(records);
    double pctResolution = this.project.getTerrainPctResolution();
    TerrainData terrainData = this.setUpSource.getTerrain(srid, env, pctResolution);
    this.exporter.exportTerrain(terrainData);    
  }
}
