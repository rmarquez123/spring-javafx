package com.rm.springjavafx.charts.xy.datasets;

import com.rm.springjavafx.charts.xy.JFreeDataSet;
import com.rm.springjavafx.charts.xy.XYValues;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jfree.data.xy.AbstractIntervalXYDataset;

/**
 *
 * @author Ricardo Marquez
 */
public class JFreeIntervalXYDataSet extends AbstractIntervalXYDataset implements JFreeDataSet {

  public final ObservableList<XYValues> datasetProperty = FXCollections.observableArrayList();
  private final double width;
  
  /**
   * 
   * @param width 
   */
  public JFreeIntervalXYDataSet(double width) {
    this.width = width;
  }
  
  @Override
  public int getSeriesCount() {
    return this.datasetProperty.size();
  }

  /**
   *
   * @param values
   */
  @Override
  public void addOrUpdate(XYValues values) {
    Objects.requireNonNull(values, "Values cannot be null");
    int indexOf = this.getSeriesIndex((String) values.getKey());
    if (indexOf < 0) {
      this.datasetProperty.add(values);
    } else {
      this.datasetProperty.set(indexOf, values);
    }
  }

  /**
   *
   * @param i
   * @return
   */
  @Override
  public int getSeriesIndex(String key) {
    int result = -1;
    int index = -1;
    for (XYValues xYValues : this.datasetProperty) {
      index++;
      if (Objects.equals(xYValues.getKey(), key)) {
        result = index;
        break;
      }
    }
    return result;
  }
  
  /**
   * 
   * @param i
   * @return 
   */
  @Override
  public Comparable getSeriesKey(int i) {
    Comparable result;
    if (0 <= i && i < this.getSeriesCount()) {
      result = this.datasetProperty.get(i).getKey();
    } else {
      result = null;
    }
    return result;
  }

  @Override
  public int getItemCount(int i) {
    int result;
    if (0 <= i && i < this.getSeriesCount()) {
      result = this.datasetProperty.get(i).size();
    } else {
      result = 0;
    }
    return result;
  }

  @Override
  public Number getX(int i, int i1) {
    Number result;
    if (0 <= i && i < this.getSeriesCount()) {
      result = this.datasetProperty.get(i).getX(i1);
    } else {
      result = null;
    }
    return result;
  }

  @Override
  public Number getY(int i, int i1) {
    Number result;
    if (0 <= i && i < this.getSeriesCount()) {
      result = this.datasetProperty.get(i).getY(i1);
    } else {
      result = null;
    }
    return result;
  }

  @Override
  public Number getStartX(int i, int i1) {
    return this.getX(i, i1).doubleValue() - width/2.0;
  }

  @Override
  public Number getEndX(int i, int i1) {
    return this.getX(i, i1).doubleValue() + width/2.0;
  }

  @Override
  public Number getStartY(int i, int i1) {
    return this.getY(i, i1).doubleValue();
  }
  
  @Override
  public Number getEndY(int i, int i1) {
    return this.getY(i, i1).doubleValue();
  }
}
