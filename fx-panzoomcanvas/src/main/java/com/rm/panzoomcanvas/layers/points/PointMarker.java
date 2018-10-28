package com.rm.panzoomcanvas.layers.points;

import com.rm.panzoomcanvas.core.FxPoint;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.StringConverter;

/**
 *
 * @author rmarquez
 * @param <T>
 */
public final class PointMarker<T> {

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

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public int hashCode() {
    int hash = 5;
    hash = 53 * hash + Objects.hashCode(this.userObject);
    return hash;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final PointMarker<?> other = (PointMarker<?>) obj;
    if (!Objects.equals(this.userObject, other.userObject)) {
      return false;
    }
    return true;
  }
  
  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public String toString() {
    return "PointMarker{" + "userObject=" + userObject + ", point=" + point + '}';
  }

}
