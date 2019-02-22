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
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Ricardo Marquez
 */
public class WplsGetTerrainIT {

  private DbConnection dbConnection;

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
    "300.0, 26918, 0.10, 1914, 1354",
    "200.0, 26918, 0.10, 2220, 1426"
  })
  public void test_get_terrain(double minVoltage, int srid, double pctResolution,
    int expWidth, int expHeight) {
    TransmissionLines.Filter filter = new TransmissionLines.Filter.Builder()
      .filterByMinRatedVoltage(minVoltage)
      .filterByStateName("New York")
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
