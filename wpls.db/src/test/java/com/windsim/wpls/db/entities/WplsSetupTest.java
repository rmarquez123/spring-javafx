package com.windsim.wpls.db.entities;

import com.rm.datasources.DbConnection;
import com.rm.wpls.powerline.TransmissionLines;
import com.rm.wpls.powerline.setup.Project;
import com.rm.wpls.powerline.setup.TerrainDataSource;
import com.rm.wpls.powerline.setup.TransmissionLineSource;
import com.rm.wpls.powerline.setup.WeatherRecordsSource;
import com.rm.wpls.powerline.setup.WeatherStationsSource;
import com.rm.wpls.powerline.setup.WplsSetup;
import com.rm.wpls.powerline.setup.WplsSetupSource;
import com.windsim.wpls.plsetup.impl.pg.PgTerrainDataSource;
import com.windsim.wpls.plsetup.impl.pg.PgTransmissionLineSource;
import com.windsim.wpls.plsetup.impl.pg.PgWeatherRecords;
import com.windsim.wpls.plsetup.impl.pg.PgWeatherStationsSource;
import java.io.File;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Ricardo Marquez
 */
public class WplsSetupTest {

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
  @Ignore
  public void createPowerLineModuleFiles() {
    double minVoltage = 200.0;
    int srid = 26918;
    WplsSetupSource setUpSource = this.createPowerLineModule(minVoltage);
    Project project = new Project(srid, new File("C:\\Data\\Test\\powerlinemodule"));
    WplsSetup setup = new WplsSetup(project, setUpSource);
    setup.preparePowerLineModule();
  }
  
  private WplsSetupSource createPowerLineModule(double minVoltage) {
    TransmissionLines.Filter filter = new TransmissionLines.Filter.Builder()
      .filterByMinRatedVoltage(minVoltage)
      .build();
    TransmissionLineSource trLineSource = new PgTransmissionLineSource(dbConnection);
    WeatherStationsSource wsSource = new PgWeatherStationsSource(dbConnection);
    TerrainDataSource terrainSource = new PgTerrainDataSource(dbConnection);
    WeatherRecordsSource recordsSource = (stations)->new PgWeatherRecords(dbConnection, stations);
    WplsSetupSource setUpSource = new WplsSetupSource(filter, trLineSource, wsSource, recordsSource, terrainSource);
    return setUpSource;
  }
}
