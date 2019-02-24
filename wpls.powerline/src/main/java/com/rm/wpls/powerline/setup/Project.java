package com.rm.wpls.powerline.setup;

import java.io.File;

/**
 *
 * @author Ricardo Marquez
 */
public class Project {

  private final int srid;
  private final double terrainPctResolution;
  private final File exportDir;

  /**
   *
   * @param srid
   * @param exportDir
   */
  public Project(int srid, File exportDir, double terrainPctResolution) {
    if (exportDir == null) {
      throw new NullPointerException("export directory cannot be null");
    }
    this.srid = srid;
    this.exportDir = exportDir;
    this.terrainPctResolution = terrainPctResolution;
  }

  public int getSrid() {
    return srid;
  }

  public File getExportDir() {
    return this.exportDir;
  }
  
    /**
   * 
   * @return 
   */
  double getTerrainPctResolution() {
    return this.terrainPctResolution;
  }

  @Override
  public String toString() {
    return "Project{" + "srid=" + srid + ", exportDir=" + exportDir + '}';
  }



}
