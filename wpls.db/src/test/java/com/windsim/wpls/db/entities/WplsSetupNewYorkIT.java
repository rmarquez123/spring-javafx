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
import common.types.DateTimeRange;
import java.io.File;
import java.time.ZoneId;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Ricardo Marquez
 */
public class WplsSetupNewYorkIT {

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
  
  /**
   * 
   */
  @Test
  public void createPowerLineModuleFiles() throws Exception {
    double minVoltage = 200.0;
    int outSrid = 26918;
    WplsSetupSource setUpSource = this.createPowerLineModule(minVoltage);
    File file = new File("C:\\Data\\Test\\powerlinemodule\\new_york");
    ZoneId zoneId = ZoneId.of("US/Eastern");
    Project project = new Project(DateTimeRange.of(zoneId, "yyyy/MM", "2017/02", "2017/04"), outSrid, file, 0.06);
    WplsSetup setup = new WplsSetup(project, setUpSource);
    setup.preparePowerLineModule();
  }
  
  /**
   * 
   * @param minVoltage
   * @return 
   */
  private WplsSetupSource createPowerLineModule(double minVoltage) {
    TransmissionLines.Filter filter = new TransmissionLines.Filter.Builder()
      .filterByMinRatedVoltage(minVoltage)
      .filterByStateName("New York")
      .build();
    TransmissionLineSource trLineSource = new PgTransmissionLineSource(dbConnection);
    WeatherStationsSource wsSource = new PgWeatherStationsSource(dbConnection);
    TerrainDataSource terrainSource = new PgTerrainDataSource(dbConnection);
    WeatherRecordsSource recordsSource = (stations, dateRange)->new PgWeatherRecords(dbConnection, stations, dateRange);
    WplsSetupSource setUpSource = new WplsSetupSource(filter, trLineSource, wsSource, recordsSource, terrainSource);
    return setUpSource;
  }
}
