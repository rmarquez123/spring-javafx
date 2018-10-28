package com.rm.panzoomcanvas.layers;

import com.rm.panzoomcanvas.LayerMouseEvent;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.Node;

/**
 *
 * @author rmarquez
 * @param <TMarker>
 * @param <TObj>
 */
public abstract class LayerHoverSelect<TMarker extends Marker<TObj>, TObj> {

  private final ListProperty<TMarker> selected = new SimpleListProperty<>(FXCollections.emptyObservableList());
  private final Property<HoveredMarkers<TMarker>> hovered = new SimpleObjectProperty<>();
  private final LayerCursorHelper<TObj> cursorHelper;
  private final BaseLayer host;
  

  public LayerHoverSelect(BaseLayer host) {
    this.host = host;
    this.cursorHelper = new LayerCursorHelper<>(this);
  }
    
  public BooleanProperty selectableProperty() {
    return this.host.selectableProperty();
  }
  
  public ListProperty<TMarker> selected() {
    return selected;
  }

  public Property<HoveredMarkers<TMarker>> hovered() {
    return hovered;
  }
  
  /**
   * 
   * @return 
   */
  public Node getNode() {
    return this.host.getLayerCanvas();
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  public void onMouseClicked(LayerMouseEvent e) {
    List<TMarker> newVal = this.getMouseEvtList(e);
    this.selected.setValue(FXCollections.observableArrayList(newVal));
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  public void onMouseHovered(LayerMouseEvent e) {
    List<TMarker> newVal = this.getMouseEvtList(e);
    HoveredMarkers<TMarker> oldVal = this.hovered.getValue();
    HoveredMarkers<TMarker> result = new HoveredMarkers<>(e, newVal);
    this.hovered.setValue(result);
    this.repaintIfHoveredListChanged(oldVal == null ? Collections.EMPTY_LIST : oldVal.markers, newVal);
  }

  /**
   *
   * @param oldVal
   * @param newVal
   */
  private void repaintIfHoveredListChanged(List<TMarker> oldVal, List<TMarker> newVal) {
    boolean changed;
    HoveredMarkers<TMarker> currentHoveredVal = this.hovered.getValue();
    if (currentHoveredVal == null) {
      changed = true;
    } else {
      changed = !listEqualsIgnoreOrder(oldVal, newVal);
    }
    if (changed) {
      this.host.repaint();
    }
  }

  /**
   *
   * @param <T>
   * @param list1
   * @param list2
   * @return
   */
  private static <T> boolean listEqualsIgnoreOrder(List<T> list1, List<T> list2) {
    return new HashSet<>(list1).equals(new HashSet<>(list2));
  }

  /**
   *
   * @param e
   * @return
   */
  protected abstract List<TMarker> getMouseEvtList(LayerMouseEvent e);

}
