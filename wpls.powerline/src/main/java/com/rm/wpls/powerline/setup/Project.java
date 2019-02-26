package com.rm.wpls.powerline.setup;

import common.types.DateTimeRange;
import java.io.File;
import java.util.Objects;

/**
 *
 * @author Ricardo Marquez
 */
public class Project {

  private final File exportDir;
  private final int outputSrid;
  private final double terrainPctResolution;
  private final DateTimeRange dateRange;

  /**
   *
   * @param srid
   * @param exportDir
   */
  public Project(DateTimeRange dateRange, int srid, File exportDir, double terrainPctResolution) {
    Objects.requireNonNull(exportDir, "export directory cannot be null");
    this.dateRange = dateRange;
    this.outputSrid = srid;
    this.exportDir = exportDir;
    this.terrainPctResolution = terrainPctResolution;
  }

  /**
   *
   * @return
   */
  public int getOutputSrid() {
    return outputSrid;
  }

  /**
   *
   * @return
   */
  public File getExportDir() {
    return this.exportDir;
  }

  /**
   *
   * @return
   */
  public DateTimeRange getDateRange() {
    return dateRange;
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
    return "Project{" + "srid=" + outputSrid + ", exportDir=" + exportDir + '}';
  }

}
