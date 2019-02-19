package com.windsim.wpls.plsetup.impl.pg;

import com.rm.datasources.DbConnection;
import com.rm.fxmap.postgres.PgUtils;
import com.rm.panzoomcanvas.core.Dimension;
import com.vividsolutions.jts.geom.Envelope;
import com.rm.wpls.powerline.TerrainData;
import java.sql.SQLException;
import java.util.Set;
import mil.nga.tiff.FileDirectoryEntry;
import mil.nga.tiff.ImageWindow;
import mil.nga.tiff.Rasters;
import mil.nga.tiff.TIFFImage;
import mil.nga.tiff.TiffReader;
import org.apache.commons.lang3.mutable.MutableObject;
import org.postgresql.util.PGobject;

/**
 *
 * @author Ricardo Marquez
 */
public final class PgTerrainData extends TerrainData {

  private final int srid;

  private final Envelope env;
  private final DbConnection dbConnection;
  private Dimension dimension = null;
  private TIFFImage image;
  private final double resizePct;

  /**
   *
   * @param srid
   * @param env
   * @param dbConnection
   * @param resizePct
   */
  public PgTerrainData(int srid, Envelope env, DbConnection dbConnection, double resizePct) {
    this.srid = srid;
    this.env = env;
    this.dbConnection = dbConnection;
    this.resizePct = resizePct;
  }

  /**
   *
   * @return
   */
  @Override
  public synchronized Dimension size() {
    if (this.dimension == null) {
      if (this.image == null) {
        this.dimension = this.queryDimension();
      } else {
        this.getDimensionsFromImage();
      }
    }
    return this.dimension;
  }

  @Override
  public float getMinValue() {
    if (this.image == null) {
      readImage();
    }
    int numRows = this.getNumRows();
    float minValue = Float.MAX_VALUE;
    for (int i = 0; i < numRows; i++) {
      float[] rowData = this.getRowData(i);
      for (float f : rowData) {
        minValue = Math.min(minValue, f);
      }
    }
    return minValue;
  }

  @Override
  public float getMaxValue() {
    if (this.image == null) {
      readImage();
    }
    int numRows = this.getNumRows();
    float maxValue = Float.MIN_VALUE;
    for (int i = 0; i < numRows; i++) {
      float[] rowData = this.getRowData(i);
      for (float f : rowData) {
        maxValue = Math.max(maxValue, f);
      }
    }
    return maxValue;
  }

  /**
   *
   * @param row
   * @return
   */
  public float[] getRowData(int row) {
    float[] result = this.getRowData(row, 0, this.getNumCols());
    return result;
  }

  /**
   *
   * @param row
   * @param column
   * @param count
   * @return
   */
  @Override
  public synchronized float[] getRowData(int row, int column, int count) {
    if (this.image == null) {
      readImage();
    }
    int minX = column;
    int imageWidth = this.image.getFileDirectory().getImageWidth().intValue();
    int maxX = Math.min(column + count, imageWidth);
    int minY = row;
    int maxY = row + 1;
    ImageWindow window = new ImageWindow(minX, minY, maxX, maxY);
    float[] result;
    Rasters raster;
    try {
      raster = this.image.getFileDirectory().readRasters(window);
      result = new float[raster.getWidth()];
      for (int colIndex = 0; colIndex < raster.getWidth(); colIndex++) {
        int sampleIndex = 0;
        int rowIndex = 0;
        Number object = raster.getPixelSample(sampleIndex, colIndex, rowIndex);
        result[colIndex] = object.floatValue();
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    return result;
  }

  /**
   * Queries the database for the raster dimensions based on the envelope and srid. 
   *
   * @return The raster dimension in units consistent with the srid.  
   */
  private Dimension queryDimension() {
    String envText = PgUtils.getMakeEnvelopeText(this.env, this.srid);
    int sourceSrid = 4326;
    String sql = "select\n"
      + "	st_metadata(st_resize(st_transform(st_union(st_clip(ter.rast, st_transform(" + envText + ", " + sourceSrid + " ), true)), " + this.srid + "), "
      + String.valueOf(this.resizePct) + ", " + String.valueOf(this.resizePct) + ")) as metadata\n"
      + "from terrain ter\n"
      + "where st_intersects(st_envelope(ter.rast), st_transform(" + envText + ", " + sourceSrid + "))";
    PgUtils.logLongQuery(sql);
    MutableObject<Dimension> result = new MutableObject<>();
    this.dbConnection.executeQuery(sql, (rs) -> {
      try {
        PGobject metaDataAsPgObject = (PGobject) rs.getObject("metadata");
        if (metaDataAsPgObject != null) {
          String value = metaDataAsPgObject.getValue();
          String[] parts = value.replace("(", "").replace(")", "").split(",", -1);
          int width = Integer.parseInt(parts[2]);
          int height = Integer.parseInt(parts[3]);
          double xmin = Double.parseDouble(parts[0]);
          double xmax = xmin + Double.parseDouble(parts[4]) * width;
          double ymax = Double.parseDouble(parts[1]);
          double ymin = ymax + Double.parseDouble(parts[5]) * height;
          Envelope rasterEnvelope = new Envelope(xmin, xmax, ymin, ymax);
          Dimension rasterDimension = new Dimension(width, height, rasterEnvelope);
          result.setValue(rasterDimension);
        } else {
          result.setValue(Dimension.Empty());
        }
      } catch (SQLException ex) {
        throw new RuntimeException(ex);
      }
    });
    return result.getValue();
  }

  /**
   *
   */
  private void getDimensionsFromImage() {
    int width = this.image.getFileDirectory().getImageWidth().intValue();
    int height = this.image.getFileDirectory().getImageHeight().intValue();
    Set<FileDirectoryEntry> entries = this.image.getFileDirectory().getEntries();
    FileDirectoryEntry[] entriesArray = entries.toArray(new FileDirectoryEntry[0]);
    double xmin = ((Double[]) entriesArray[14].getValues())[0];
    double ymax = ((Double[]) entriesArray[14].getValues())[1];
    double deltax = ((Double[]) entriesArray[11].getValues())[0];
    double deltay = ((Double[]) entriesArray[11].getValues())[1];
    double xmax = xmin + deltax * width;
    double ymin = ymax + deltay * height;
    Envelope rasterEnvelope = new Envelope(xmin, xmax, ymin, ymax);
    this.dimension = new Dimension(width, height, rasterEnvelope);
  }

  /**
   *
   */
  private void readImage() {
    int sourceSrid = 4326;
    String envText = PgUtils.getMakeEnvelopeText(this.env, this.srid);
    String sql = "select\n"
      + "	st_astiff(st_resize(st_transform(st_union(st_clip(ter.rast, st_transform(" + envText + ", " + sourceSrid + " ), true)), " + this.srid + "), "
      + String.valueOf(this.resizePct) + ", " + String.valueOf(this.resizePct) + ")) as rast\n"
      + "from terrain ter\n"
      + "where st_intersects(st_envelope(ter.rast), st_transform(" + envText + ", " + sourceSrid + "))";
    PgUtils.logLongQuery(sql);
    this.dbConnection.executeQuery(sql, (rs) -> {
      try {
        byte[] rasterBytes = rs.getBytes("rast");
        if (rasterBytes != null && rasterBytes.length != 0) {
          this.image = TiffReader.readTiff(rasterBytes);
        } else {
          this.image = new TIFFImage();
        }
      } catch (SQLException ex) {
        throw new RuntimeException(ex);
      }
    });
  }

}
