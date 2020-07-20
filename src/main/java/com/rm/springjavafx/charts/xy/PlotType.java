package com.rm.springjavafx.charts.xy;

import com.rm.springjavafx.charts.xy.datasets.JFreeAbstractXYDataSet;
import com.rm.springjavafx.charts.xy.datasets.JFreeIntervalXYDataSet;
import java.awt.BasicStroke;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author Ricardo Marquez
 */
public enum PlotType {
  MARKER_ONLY {
    @Override
    void setSeriesRenderer(RenderArguments args) {
      DefaultXYItemRenderer renderer = (DefaultXYItemRenderer) args.renderer;
      renderer.setSeriesPaint(args.seriesIndex, args.dataset.getLineColorAwt(), true);
      renderer.setSeriesStroke(args.seriesIndex, new BasicStroke(0), true);
      renderer.setSeriesShape(args.seriesIndex, args.dataset.getShape(), true);
      renderer.setSeriesLinesVisible(args.seriesIndex, Boolean.FALSE);
//      renderer.setDotWidth((int) args.dataset.getShape().getBounds2D().getWidth());
//      renderer.setDotHeight((int) args.dataset.getShape().getBounds2D().getHeight());
      
    }

    @Override
    XYItemRenderer getRenderer() {
      return new DefaultXYItemRenderer();
    }
    @Override
    XYDataset getDataset(XYDataSetGroup args) {
      return new JFreeAbstractXYDataSet();
    }
    
  },
  LINE_PLOT {
    @Override
    void setSeriesRenderer(RenderArguments args) {
      args.renderer.setSeriesPaint(args.seriesIndex, args.dataset.getLineColorAwt(), true);
      args.renderer.setSeriesStroke(args.seriesIndex, args.dataset.getLineStroke(), true);
      args.renderer.setSeriesShape(args.seriesIndex, args.dataset.getShape(), true);
    }

    @Override
    XYItemRenderer getRenderer() {
      return new DefaultXYItemRenderer();
    }
    @Override
    XYDataset getDataset(XYDataSetGroup args) {
      return new JFreeAbstractXYDataSet();
    }
    
  },
  BAR_PLOT {
    @Override
    void setSeriesRenderer(RenderArguments args) {
      XYBarRenderer renderer = (XYBarRenderer) args.renderer;
      renderer.setSeriesPaint(args.seriesIndex, args.dataset.getLineColorAwt(), true);
      renderer.setSeriesStroke(args.seriesIndex, args.dataset.getLineStroke(), true);
      renderer.setSeriesShape(args.seriesIndex, args.dataset.getShape(), true);
      renderer.setShadowVisible(false);
      renderer.setDrawBarOutline(false);
      renderer.setMargin(0.0);
      renderer.setGradientPaintTransformer(null);
    }

    @Override
    XYItemRenderer getRenderer() {
      return new XYBarRenderer();
    }

    @Override
    XYDataset getDataset(XYDataSetGroup args) {
      double width = args.barwidth();
      return new JFreeIntervalXYDataSet(width);
    }
    
  };

  abstract void setSeriesRenderer(RenderArguments args);

  abstract XYItemRenderer getRenderer();

  abstract XYDataset getDataset(XYDataSetGroup args);
}
