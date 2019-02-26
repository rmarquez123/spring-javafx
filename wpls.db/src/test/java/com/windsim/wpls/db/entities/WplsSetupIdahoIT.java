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
import com.vividsolutions.jts.geom.Envelope;
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
public class WplsSetupIdahoIT {

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
      .setSchema("idaho_power")
      .createDbConnection();
  }

  /**
   *
   */
  @Test
  public void createPowerLineModuleFiles() throws Exception {
    double minVoltage = 200.0;
    int outSrid = 32612;
    double x1 = 83362;
    double x2 = 217362;
    double y1 = 4748152;
    double y2 = 4841652;
    Envelope env = new Envelope(x1, x2, y1, y2);
    WplsSetupSource setUpSource = this.createPowerLineModule(env, minVoltage);
    File file = new File("C:\\Data\\Test\\powerlinemodule\\idaho_power");
    ZoneId zoneId = ZoneId.of("US/Mountain");
    Project project = new Project(DateTimeRange.of(zoneId, "yyyy/MM", "2016/12", "2017/08"), outSrid, file, 0.06);
    WplsSetup setup = new WplsSetup(project, setUpSource);
    setup.preparePowerLineModule();
  }

  /**
   *
   * @param minVoltage
   * @return
   */
  private WplsSetupSource createPowerLineModule(Envelope envelope, double minVoltage) {
    TransmissionLines.Filter filter = new TransmissionLines.Filter.Builder()
      .filterByMinRatedVoltage(minVoltage)
      .filterByStateName("Idaho")
      .filterByEnvelope(envelope)
      .build();
    TransmissionLineSource trLineSource = new PgTransmissionLineSource(this.dbConnection);
    WeatherStationsSource wsSource = new PgWeatherStationsSource(this.dbConnection);
    TerrainDataSource terrainSource = new PgTerrainDataSource(this.dbConnection);
    WeatherRecordsSource recordsSource = (stations, dateRange) -> {
      return new PgWeatherRecords(this.dbConnection, stations, dateRange);
    };
    WplsSetupSource setUpSource = new WplsSetupSource(filter, trLineSource, wsSource, recordsSource, terrainSource);
    return setUpSource;
  }

}
