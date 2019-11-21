package com.rm.springjavafx.charts.category.datasets;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarPainter;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.IntervalBarRenderer;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;

/**
 *
 * @author Ricardo Marquez
 */
public class MultiIntervalCategoryRenderer extends BarRenderer {

  /**
   * Returns the range of values from the specified dataset. For this renderer, this is
   * equivalent to calling {@code findRangeBounds(dataset, true)}.
   *
   * @param dataset the dataset ({@code null} permitted).
   *
   * @return The range (or {@code null} if the dataset is {@code null} or empty).
   */
  @Override
  public Range findRangeBounds(CategoryDataset dataset) {
    Range result;
    if (dataset instanceof JFreeMultiIntervalCategoryDataSet) {
      double minvalue = Double.MAX_VALUE;
      double maxvalue = Double.MIN_VALUE;
      for (int i = 0; i < dataset.getColumnCount(); i++) {
        for (int j = 0; j < dataset.getRowCount(); j++) {
          JFreeMultiIntervalCategoryDataSet d = (JFreeMultiIntervalCategoryDataSet) dataset;
          for (int k = 0; k < d.getNumIntervals(j, i); k++) {
            double startval = d.getStartValue(j, i, k).doubleValue();
            double endval = d.getEndValue(j, i, k).doubleValue();
            if (startval < minvalue) {
              minvalue = startval;
            }
            if (endval > maxvalue) {
              maxvalue = endval;
            }
          }
        }
      }
      if (minvalue != Double.MAX_VALUE && maxvalue != Double.MIN_VALUE) {
        result = new Range(minvalue, maxvalue);
      } else {
        result = new Range(0, 0);
      }
      
    } else {
      result = findRangeBounds(dataset, true);
    }
    return result;
  }

  /**
   * Draws the bar for a single (series, category) data item.
   *
   * @param g2 the graphics device.
   * @param state the renderer state.
   * @param dataArea the data area.
   * @param plot the plot.
   * @param domainAxis the domain axis.
   * @param rangeAxis the range axis.
   * @param dataset the dataset.
   * @param row the row index (zero-based).
   * @param column the column index (zero-based).
   * @param pass the pass index.
   */
  @Override
  public void drawItem(Graphics2D g2, CategoryItemRendererState state,
    Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis,
    ValueAxis rangeAxis, CategoryDataset dataset, int row, int column,
    int pass) {

    if (dataset instanceof JFreeMultiIntervalCategoryDataSet) {
      JFreeMultiIntervalCategoryDataSet d = (JFreeMultiIntervalCategoryDataSet) dataset;
      drawInterval(g2, state, dataArea, plot, domainAxis, rangeAxis,
        d, row, column);
    } else {
      super.drawItem(g2, state, dataArea, plot, domainAxis, rangeAxis,
        dataset, row, column, pass);
    }
  }

  /**
   * Draws a single interval.
   *
   * @param g2 the graphics device.
   * @param state the renderer state.
   * @param dataArea the data plot area.
   * @param plot the plot.
   * @param domainAxis the domain axis.
   * @param rangeAxis the range axis.
   * @param dataset the data.
   * @param row the row index (zero-based).
   * @param column the column index (zero-based).
   */
  protected void drawInterval(Graphics2D g2, CategoryItemRendererState state,
    Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis,
    ValueAxis rangeAxis, JFreeMultiIntervalCategoryDataSet dataset, int row, int column
  ) {

    int visibleRow = state.getVisibleSeriesIndex(row);
    if (visibleRow < 0) {
      return;
    }
    for (int i = 0; i < dataset.getNumIntervals(row, column); i++) {
      Number value0 = dataset.getEndValue(row, column, i);
      Number value1 = dataset.getStartValue(row, column, i);
      PlotOrientation orientation = plot.getOrientation();
      double rectX = 0.0;
      double rectY = 0.0;

      RectangleEdge rangeAxisLocation = plot.getRangeAxisEdge();

      if (value0 == null) {
        return;
      }

      double java2dValue0 = rangeAxis.valueToJava2D(value0.doubleValue(),
        dataArea, rangeAxisLocation);
      if (value1 == null) {
        return;
      }
      double java2dValue1 = rangeAxis.valueToJava2D(
        value1.doubleValue(), dataArea, rangeAxisLocation);
      if (java2dValue1 < java2dValue0) {
        double temp = java2dValue1;
        java2dValue1 = java2dValue0;
        java2dValue0 = temp;
      }

      // BAR WIDTH
      double rectWidth = state.getBarWidth();
      // BAR HEIGHT
      double rectHeight = Math.abs(java2dValue1 - java2dValue0);
      RectangleEdge barBase = RectangleEdge.LEFT;
      if (orientation == PlotOrientation.HORIZONTAL) {
        // BAR Y
        rectX = java2dValue0;
        rectY = calculateBarW0(getPlot(), orientation, dataArea,
          domainAxis, state, 0, column);
        rectHeight = state.getBarWidth() * state.getVisibleSeriesCount();
        rectWidth = Math.abs(java2dValue1 - java2dValue0);
        barBase = RectangleEdge.LEFT;
      } else if (orientation.isVertical()) {
        // BAR X
        rectX = calculateBarW0(getPlot(), orientation, dataArea,
          domainAxis, state, visibleRow, column);
        rectY = java2dValue0;
        barBase = RectangleEdge.BOTTOM;
      }
      Rectangle2D bar = new Rectangle2D.Double(rectX, rectY, rectWidth,
        rectHeight);
      BarPainter painter = getBarPainter();
      if (state.getElementHinting()) {
        beginElementGroup(g2, dataset.getRowKey(row),
          dataset.getColumnKey(column));
      }
      if (getShadowsVisible()) {
        painter.paintBarShadow(g2, this, row, column, bar, barBase, false);
      }
      getBarPainter().paintBar(g2, this, row, column, bar, barBase);
      if (state.getElementHinting()) {
        endElementGroup(g2);
      }

      CategoryItemLabelGenerator generator = getItemLabelGenerator(row,
        column);
      if (generator != null && isItemLabelVisible(row, column)) {
        drawItemLabel(g2, dataset, row, column, plot, generator, bar,
          false);
      }

      // add an item entity, if this information is being collected
      EntityCollection entities = state.getEntityCollection();
      if (entities != null) {
        addItemEntity(entities, dataset, row, column, bar);
      }
    }

  }

  /**
   * Tests this renderer for equality with an arbitrary object.
   *
   * @param obj the object ({@code null} permitted).
   *
   * @return A boolean.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof IntervalBarRenderer)) {
      return false;
    }
    // there are no fields to check
    return super.equals(obj);
  }

}
