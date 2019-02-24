package com.rm.wpls.powerline;

import com.rm.panzoomcanvas.core.Dimension;
import com.vividsolutions.jts.geom.Envelope;

/**
 *
 * @author Ricardo Marquez
 */
public abstract class TerrainData {

  /**
   *
   * @return
   */
  public int getNumRows() {
    return this.size().getHeight();
  }

  ;
  
  /**
   * 
   * @return 
   */
  public int getNumCols() {
    return this.size().getWidth();
  }

  ;
  
  
  /**
   * 
   * @return 
   */
  public Envelope getExtent() {
    Dimension dimension = this.size();
    Envelope env = dimension.getExtent();
    return env;
  }

  ;
  
  
  /**
   * 
   * @return 
   */
  public abstract Dimension size();

  /**
   *
   * @param row
   * @param column
   * @param count
   * @return
   */
  public abstract float[] getRowData(int row, int column, int count);

  /**
   *
   * @return
   */
  public abstract float getMinValue();

  /**
   *
   * @return
   */
  public abstract float getMaxValue();

  /**
   *
   * @return
   */
  public double getDeltaX() {
    return this.getExtent().getWidth() / this.getNumCols();
  }

  /**
   *
   * @return
   */
  public double getDeltaY() {
    return this.getExtent().getHeight() / this.getNumRows();
  }

}
