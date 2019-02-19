package com.rm.wpls.powerline.setup;

import com.vividsolutions.jts.geom.Envelope;
import com.rm.wpls.powerline.TerrainData;

/**
 *
 * @author Ricardo Marquez
 */
public interface TerrainDataSource {

  /**
   * 
   * @param srid
   * @param env
   * @param pctResolution
   * @return 
   */
  public TerrainData getTerrain(int srid, Envelope env, double pctResolution);
  
}
