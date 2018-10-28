package com.rm.panzoomcanvas.layers.points;

import com.rm.panzoomcanvas.core.FxPoint;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.StringConverter;

/**
 *
 * @author rmarquez
 * @param <T>
 */
public class PointMarker<T> {
  
  private final T userObject;
  private final FxPoint point;
  private final StringProperty labelProperty = new SimpleStringProperty();
  /**
   *
   * @param userObject
   * @param point
   */
  public PointMarker(T userObject, FxPoint point) {
    this.userObject = userObject;
    this.point = point;
    this.labelProperty.setValue(String.valueOf(userObject));
  }
  
  /**
   * 
   * @param <T>
   * @param pointMarkers
   * @return 
   */
  public static <T> FxPoint[] getPoints(PointMarker<T>[] pointMarkers) {
    FxPoint[] result = new FxPoint[pointMarkers.length];
    for (int i = 0; i < pointMarkers.length; i++) {
      result[i] = pointMarkers[i].getPoint();
    }
    return result;
  }

  
  public StringProperty labelProperty() {
    return labelProperty;
  }
  

  /**
   *
   * @return
   */
  public Object getUserObject() {
    return userObject;
  }

  /**
   *
   * @return
   */
  public FxPoint getPoint() {
    return point;
  }
  
  /**
   *
   * @param stringConverter
   */
  public void setLabelProperty(StringConverter<T> stringConverter) {
    String newLabel = stringConverter.toString(userObject);
    this.labelProperty.setValue(newLabel);
  }

  @Override
  public String toString() {
    return "PointMarker{" + "userObject=" + userObject + ", point=" + point + '}';
  }

}
