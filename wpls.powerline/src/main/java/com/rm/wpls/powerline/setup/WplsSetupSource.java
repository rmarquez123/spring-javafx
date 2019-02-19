package com.rm.wpls.powerline.setup;

import com.vividsolutions.jts.geom.Envelope;
import com.rm.wpls.powerline.TerrainData;
import com.rm.wpls.powerline.TransmissionLines;
import com.rm.wpls.powerline.WindRecords;
import gov.inl.glass3.weather.WeatherStations;

/**
 *
 * @author Ricardo Marquez
 */
public final class WplsSetupSource {
  private final TransmissionLines.Filter filter;
  private final TransmissionLineSource trLineSource;
  private final WeatherStationsSource wsSource;
  private final WeatherRecordsSource recordsSource;
  private final TerrainDataSource terrainSource;
  
  
  /**
   *
   * @param filter
   * @param trLineSource
   * @param wsSource
   * @param recordsSource
   * @param terrainSource
   */
  public WplsSetupSource(TransmissionLines.Filter filter, 
    TransmissionLineSource trLineSource, 
    WeatherStationsSource wsSource, 
    WeatherRecordsSource recordsSource, 
    TerrainDataSource terrainSource) {
    this.filter = filter;
    this.trLineSource = trLineSource;
    this.wsSource = wsSource;
    this.recordsSource = recordsSource;
    this.terrainSource = terrainSource;
  }

  /**
   *
   * @param srid
   * @return
   */
  public TransmissionLines getTransmissionLines(int srid) {
    TransmissionLines result = this.trLineSource.getTransmissionLines(srid, this.filter); 
    return result; 
  }

  /**
   *
   * @param srid
   * @param env
   * @return
   */
  public WeatherStations getWeatherStations(int srid, Envelope env) {
    WeatherStations result = this.wsSource.getWeatherStations(srid, env); 
    return result; 
  }

  /**
   *
   * @param srid
   * @param env
   * @param pctResolution
   * @return
   */
  public TerrainData getTerrain(int srid, Envelope env, double pctResolution) {
    TerrainData result = this.terrainSource.getTerrain(srid, env, pctResolution);
    return result;
  }
  
  /**
   * 
   * @param stations
   * @return 
   */
  public WindRecords getWeatherRecords(WeatherStations stations) {
    WindRecords result = this.recordsSource.getWeatherRecords(stations);
    return result;
  }

}
