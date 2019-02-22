package com.windsim.wpls.db.entities;

import com.rm.datasources.DbConnection;
import com.vividsolutions.jts.geom.Envelope;
import com.windsim.wpls.plsetup.impl.pg.PgTerrainData;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Ricardo Marquez
 */
@RunWith(JUnitParamsRunner.class)
public class PgTerrainDataIT {

  private DbConnection dbConnection;

  public PgTerrainDataIT() {
  }

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
    "-79.027629584, 40.661951726, -73.4958603429999, 44.9192265710001, 4326, 0.10",
    "172442.7993813412, 4501709.386875887,623401.3030149848, 4973990.9942969745, 26918, 0.10",
  })
  public void testGetRowData(Double xmin, Double ymin, Double xmax, Double ymax,
    Integer srid, Double pctResize) {
    Envelope env = new Envelope(xmin, ymin, xmax, ymax);
    PgTerrainData instance = new PgTerrainData(srid, env, dbConnection, pctResize);
    float[] a = instance.getRowData(0, 0, 10);
    System.out.println(ArrayUtils.toString(a));
    float[] b = instance.getRowData(500, 500, 50);
    System.out.println(ArrayUtils.toString(b));
  }

}
