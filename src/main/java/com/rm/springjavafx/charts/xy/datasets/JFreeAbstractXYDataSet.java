package com.rm.springjavafx.charts.xy.datasets;

import com.rm.springjavafx.charts.xy.JFreeDataSet;
import com.rm.springjavafx.charts.xy.XYValues;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.jfree.data.xy.AbstractXYDataset;

/**
 *
 * @author Ricardo Marquez
 */
public class JFreeAbstractXYDataSet extends AbstractXYDataset implements JFreeDataSet {
  
  public final ObservableList<XYValues> datasetProperty = FXCollections.observableArrayList();

  /**
   *
   */
  public JFreeAbstractXYDataSet() {
    this.datasetProperty.addListener((ListChangeListener.Change<? extends XYValues> c) -> {
      if (c.next()) {
        fireDatasetChanged();
      }
    });
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
   * @return
   */
  public ObservableList<XYValues> datasetProperty() {
    return this.datasetProperty;
  }

  /**
   *
   * @return
   */
  @Override
  public int getSeriesCount() {
    return this.datasetProperty.size();
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
  public int getItemCount(int i) {
    int result;
    if (0 <= i && i < this.getSeriesCount()) {
      result = this.datasetProperty.get(i).size();
    } else {
      result = 0;
    }
    return result;
  }

  /**
   *
   * @param i
   * @param i1
   * @return
   */
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

  /**
   *
   * @param i
   * @param i1
   * @return
   */
  @Override
  public Number getY(final int i, final int i1) {
    Number result;
    if (0 <= i && i < this.getSeriesCount()) {
      result = this.datasetProperty.get(i).getY(i1);
    } else {
      result = null;
    }
    return result;
  }
}
