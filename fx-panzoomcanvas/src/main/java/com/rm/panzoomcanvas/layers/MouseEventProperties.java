package com.rm.panzoomcanvas.layers;

import com.rm.panzoomcanvas.LayerMouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rmarquez
 */
public class MouseEventProperties {

  private Map<MouseEvent, List<Listener>> listeners = new HashMap<>();

  /**
   *
   * @param type
   * @param listener
   */
  public void addListener(MouseEvent type, Listener listener) {
    this.putIfAbsent(type);
    this.listeners.get(type).add(listener);
  }

  /**
   *
   */
  public void removeListener(MouseEvent type, Listener listener) {
    this.putIfAbsent(type);
    this.listeners.get(type).remove(listener);
  }

  /**
   *
   * @param type
   */
  private void putIfAbsent(MouseEvent type) {
    if (!this.listeners.containsKey(type)) {
      this.listeners.put(type, new ArrayList<>());
    }
  }

  /**
   *
   * @param mouseEvent
   * @param e
   */
  void trigger(MouseEvent mouseEvent, LayerMouseEvent e) {
    this.putIfAbsent(mouseEvent);
    List<Listener> a = new ArrayList<>(this.listeners.get(mouseEvent));
    for (Listener listener : a) {
      listener.onMouseEvent(mouseEvent, e);
    }
  }

  /**
   *
   */
  public static enum MouseEvent {
    HOVERED, CLICKED
  }

  /**
   *
   */
  public static interface Listener {

    /**
     *
     * @param type
     * @param event
     */
    void onMouseEvent(MouseEvent type, LayerMouseEvent event);

  }
}
