package fx.panzoomcanvas.projections;

import javafx.scene.input.ScrollEvent;

/**
 *
 * @author rmarquez
 */
public class ScrollInvoker {

  private final ScrollEvent evt;

  ScrollInvoker(ScrollEvent evt) {
    this.evt = evt;
  }
  
  
  public boolean isScrollEvent() {
    return evt != null;
  }
  
  public double getX() {
    return this.evt.getX();
  }

  public double getY() {
    return this.evt.getY(); 
  }

  /**
   * 
   * @return 
   */
  public boolean isScrolling() {
    boolean result = this.evt != null && this.evt.getEventType() == ScrollEvent.SCROLL;
    return result;  
  }
  
  
}
