package com.windsim.wpls.db.entities;

import com.rm.datasources.DbConnection;
import com.rm.panzoomcanvas.core.Dimension;
import com.rm.wpls.powerline.TerrainData;
import com.rm.wpls.powerline.TransmissionLines;
import com.rm.wpls.powerline.setup.TerrainDataSource;
import com.rm.wpls.powerline.setup.TransmissionLineSource;
import com.rm.wpls.powerline.setup.WeatherRecordsSource;
import com.rm.wpls.powerline.setup.WeatherStationsSource;
import com.rm.wpls.powerline.setup.WplsSetupSource;
import com.vividsolutions.jts.geom.Envelope;
import com.windsim.wpls.plsetup.impl.pg.PgTerrainDataSource;
import com.windsim.wpls.plsetup.impl.pg.PgTransmissionLineSource;
import com.windsim.wpls.plsetup.impl.pg.PgWeatherStationsSource;
import gov.inl.glass3.weather.WeatherStations;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Ricardo Marquez
 */
@RunWith(JUnitParamsRunner.class)
public class WplsSetupSourceTest {

  private DbConnection dbConnection;

  public WplsSetupSourceTest() {
  }

  /**
   *
   */
  @Before
  public void setUp() {
    this.dbConnection = new DbConnection.Builder()
      .setUrl("localhost")
      .setPort(5432)
      .setUser("postgres")
      .setPassword("postgres")
      .setSchema("transmission")
      .createDbConnection();
  }

  @Test
  @Parameters({
    "300.0, 123", 
    "200.0, 195"})
  public void test_get_transmission_lines(double minVoltage, int expSize) {
    TransmissionLines.Filter filter = new TransmissionLines.Filter.Builder()
      .filterByMinRatedVoltage(minVoltage)
      .build();
    TransmissionLineSource trLineSource = new PgTransmissionLineSource(dbConnection);
    WeatherStationsSource wsSource = null;
    TerrainDataSource terrainSource = null;
    WeatherRecordsSource recordsSource = null;
    WplsSetupSource instance = new WplsSetupSource(filter, trLineSource, wsSource, recordsSource, terrainSource);
    TransmissionLines transmissionLines = instance.getTransmissionLines(26918);
    Assert.assertEquals(expSize, transmissionLines.size());
  }
  
  
  @Test
  @Parameters({
    "300.0, 7", 
    "200.0, 7"})
  public void test_get_weather_stations(double minVoltage, int expSize) {
    TransmissionLines.Filter filter = new TransmissionLines.Filter.Builder()
      .filterByMinRatedVoltage(minVoltage)
      .build();
    TransmissionLineSource trLineSource = new PgTransmissionLineSource(this.dbConnection);
    WeatherStationsSource wsSource = new PgWeatherStationsSource(this.dbConnection);
    TerrainDataSource terrainSource = null;
    WeatherRecordsSource recordsSource = null;
    WplsSetupSource instance = new WplsSetupSource(filter, trLineSource, wsSource, recordsSource, terrainSource);
    int srid = 26918; 
    TransmissionLines transmissionLines = instance.getTransmissionLines(srid); 
    Envelope envelope = transmissionLines.getEnvelope();
    WeatherStations weatherStations = instance.getWeatherStations(srid, envelope);
    Assert.assertEquals(expSize, weatherStations.size());
  }
  
  @Test
  @Parameters({
    "300.0, 26918, 0.10, 1914, 1354", 
    "200.0, 26918, 0.10, 2220, 1426"
  })
  @Ignore
  public void test_get_terrain(double minVoltage, int srid, double pctResolution,
    int expWidth, int expHeight) {
    TransmissionLines.Filter filter = new TransmissionLines.Filter.Builder()
      .filterByMinRatedVoltage(minVoltage)
      .build();
    TransmissionLineSource trLineSource = new PgTransmissionLineSource(this.dbConnection);
    WeatherStationsSource wsSource = null;
    TerrainDataSource terrainSource = new PgTerrainDataSource(this.dbConnection);
    WeatherRecordsSource recordsSource = null;
    WplsSetupSource instance = new WplsSetupSource(filter, trLineSource, wsSource, recordsSource, terrainSource);
    TransmissionLines transmissionLines = instance.getTransmissionLines(srid); 
    Envelope envelope = transmissionLines.getEnvelope();
    TerrainData terrainData = instance.getTerrain(srid, envelope, pctResolution);
    
    // Assertions
    Dimension size = terrainData.size();
    Assert.assertEquals(expWidth, size.getWidth());
    Assert.assertEquals(expHeight, size.getHeight());
  }
}
