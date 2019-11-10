package com.rm.springjavafx.charts;

import org.jfree.chart.plot.PlotOrientation;

/**
 *
 * @author Ricardo Marquez
 */
public enum FxPlotOrientation {
  VERTICAL {
    @Override
    public PlotOrientation toJFreeOrientation() {
      return PlotOrientation.VERTICAL;
    }
  }, HORIZONTAL {
    @Override
    public PlotOrientation toJFreeOrientation() {
      return PlotOrientation.HORIZONTAL;
    }
  };

  public abstract PlotOrientation toJFreeOrientation();
}
