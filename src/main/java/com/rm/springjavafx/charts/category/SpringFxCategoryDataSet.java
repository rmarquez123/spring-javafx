package com.rm.springjavafx.charts.category;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.scene.paint.Color;

/**
 *
 * @author Ricardo Marquez
 */
public class SpringFxCategoryDataSet {

  private final int datasetId;
  private final String name;
  private final Property<CategoryValues> seriesProperty = new SimpleObjectProperty<>();
  private final ObservableSet<String> categoriesProperty = FXCollections.observableSet();
  private final Color lineColor;
  
  /**
   *
   */
  public SpringFxCategoryDataSet() {
    CategoryFxDataSet conf = this.getClass().getDeclaredAnnotation(CategoryFxDataSet.class);
    this.name = conf.name();
    this.datasetId = conf.dataset();
    this.lineColor = Color.web(conf.lineColorHex());
    this.setTimeSeries(Collections.EMPTY_LIST);
  }

  /**
   * 
   * @return 
   */
  public ObservableSet<String> categoriesProperty() {
    return categoriesProperty;
  }
  
  

  /**
   *
   */
  public void validate() throws Exception {
    
  }

  /**
   *
   */
  public Color getLineColor() {
    return lineColor;
  }

  /**
   *
   */
  public java.awt.Color getLineColorAwt() {
    float r = new Double(this.lineColor.getRed()).floatValue();
    float g = new Double(this.lineColor.getGreen()).floatValue();
    float b = new Double(this.lineColor.getBlue()).floatValue();
    return new java.awt.Color(r, g, b);
  }

  /**
   *
   * @return
   */
  public Shape getShape() {
    double radius = 4;
    Ellipse2D e = new Ellipse2D.Double(-radius / 2.0, -radius / 2.0, radius, radius);
    return e;
  }

  /**
   *
   */
  public String getKey() {
    return name;
  }

  /**
   *
   * @return
   */
  int getDatasetId() {
    return this.datasetId;
  }

  /**
   *
   */
  public final void setTimeSeries(List<CategoryValue> values) {
    if (values != null) {
      CategoryValues a = new CategoryValues(this.getKey(), values);
      this.seriesProperty.setValue(a);
    } else {
      this.setTimeSeries(Collections.EMPTY_LIST);
    }
  }

  /**
   *
   * @return
   */
  public ReadOnlyProperty<CategoryValues> seriesProperty() {
    return this.seriesProperty;
  }

  /**
   *
   * @return
   */
  public CategoryValues getSeries() {
    return seriesProperty.getValue();
  }

  /**
   *
   * @return
   */
  Stroke getLineStroke() {
    BasicStroke stroke = new BasicStroke(2);
    return stroke;
  }
  
}
