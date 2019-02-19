package com.windsim.wpls.plsetup.impl.pg;

import com.rm.datasources.DbConnection;
import com.vividsolutions.jts.geom.Envelope;
import com.rm.wpls.powerline.TerrainData;
import com.rm.wpls.powerline.setup.TerrainDataSource;

/**
 *
 * @author Ricardo Marquez
 */
public class PgTerrainDataSource implements TerrainDataSource {

  private final DbConnection dbConnection;
  
  /**
   * 
   * @param dbConnection 
   */
  public PgTerrainDataSource(DbConnection dbConnection) {
    this.dbConnection = dbConnection;
  }
  
  /**
   * 
   * @param srid
   * @param env
   * @return 
   */
  @Override
  public TerrainData getTerrain(int srid, Envelope env, double pctResolution) {
    PgTerrainData result = new PgTerrainData(srid, env, this.dbConnection, pctResolution); 
    return result;
  }
  
}
