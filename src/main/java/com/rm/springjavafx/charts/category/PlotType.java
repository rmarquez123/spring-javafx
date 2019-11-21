package com.rm.springjavafx.charts.category;

import com.rm.springjavafx.charts.category.datasets.JFreeDefaultCategoryDataset;
import com.rm.springjavafx.charts.category.datasets.JFreeMultiIntervalCategoryDataSet;
import com.rm.springjavafx.charts.category.datasets.MultiIntervalCategoryRenderer;
import java.awt.BasicStroke;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.ScatterRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.category.CategoryDataset;

/**
 *
 * @author Ricardo Marquez
 */
public enum PlotType {
  MARKER_ONLY {
    @Override
    void setSeriesRenderer(RenderArguments args) {
      CategoryItemRenderer renderer = (CategoryItemRenderer) args.renderer;
      renderer.setSeriesPaint(args.seriesIndex, args.dataset.getLineColorAwt(), true);
      renderer.setSeriesStroke(args.seriesIndex, new BasicStroke(0), true);
      renderer.setSeriesShape(args.seriesIndex, args.dataset.getShape(), true);
      
    }

    @Override
    CategoryItemRenderer getRenderer() {
      return new ScatterRenderer();
    }
    
    /**
     * 
     */
    @Override
    CategoryDataset getDataset(CategoryFxDataSetGroup args) {
      JFreeDefaultCategoryDataset result = new JFreeDefaultCategoryDataset();
      return result;
    }
    
  },
  STACKED_PLOT {
    @Override
    void setSeriesRenderer(RenderArguments args) {
      args.renderer.setSeriesPaint(args.seriesIndex, args.dataset.getLineColorAwt(), true);
      args.renderer.setSeriesStroke(args.seriesIndex, args.dataset.getLineStroke(), true);
      args.renderer.setSeriesShape(args.seriesIndex, args.dataset.getShape(), true);
    }

    @Override
    CategoryItemRenderer getRenderer() {
      StackedBarRenderer result = new StackedBarRenderer();
      result.setShadowVisible(false);
      return result;
    }
    @Override
    CategoryDataset getDataset(CategoryFxDataSetGroup args) {
      return new JFreeDefaultCategoryDataset();
    }
  },
  ITEM_VALUES {
    @Override
    void setSeriesRenderer(RenderArguments args) {
      args.renderer.setSeriesPaint(args.seriesIndex, args.dataset.getLineColorAwt(), true);
      args.renderer.setSeriesStroke(args.seriesIndex, args.dataset.getLineStroke(), true);
      args.renderer.setSeriesShape(args.seriesIndex, args.dataset.getShape(), true);
    }

    @Override
    CategoryItemRenderer getRenderer() {
      MultiIntervalCategoryRenderer result = new MultiIntervalCategoryRenderer();
      result.setShadowVisible(false);
      return result;
      
    }
    @Override
    CategoryDataset getDataset(CategoryFxDataSetGroup args) {
//      return new JFreeDefaultCategoryDataset();
      return new JFreeMultiIntervalCategoryDataSet();
    }
  },
  BAR_PLOT {
    @Override
    void setSeriesRenderer(RenderArguments args) {
      CategoryItemRenderer renderer = args.renderer;
      renderer.setSeriesPaint(args.seriesIndex, args.dataset.getLineColorAwt(), true);
      renderer.setSeriesStroke(args.seriesIndex, args.dataset.getLineStroke(), true);
      renderer.setSeriesShape(args.seriesIndex, args.dataset.getShape(), true);
      
    }

    @Override
    CategoryItemRenderer getRenderer() {
      BarRenderer result = new BarRenderer();
      result.setDrawBarOutline(false);
      result.setShadowVisible(false);
      return result;
    }

    @Override
    CategoryDataset getDataset(CategoryFxDataSetGroup args) {
      double width = args.barwidth();
      return new JFreeDefaultCategoryDataset();
    }  
  };
    
  
  /**
   * 
   * @param args 
   */
  abstract void setSeriesRenderer(RenderArguments args);
  
  /**
   * 
   * @return 
   */
  abstract CategoryItemRenderer getRenderer();

  abstract CategoryDataset getDataset(CategoryFxDataSetGroup args);
}
