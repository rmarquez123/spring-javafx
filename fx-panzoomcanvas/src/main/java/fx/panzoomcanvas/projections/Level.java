package fx.panzoomcanvas.projections;

import javafx.scene.input.ScrollEvent;

/**
 *
 * @author rmarquez
 */
public class Level {

  private static final int MAX = 18;
  private static final int MIN = 0;
  private final int value;
  private final ScrollInvoker invoker;

  /**
   * Public constructor. The level value can be between {@linkplain #MAX} and
   * {@linkplain #MIN}. If the value is outside of the range then the property value will
   * be set to the lower or upper limit, whichever is appropriate.
   *
   * @param value
   */
  public Level(int value, ScrollInvoker invoker) {
    if (value <= MIN) {
      this.value = MIN;
    } else if (value >= MAX) {
      this.value = MAX;
    } else {
      this.value = value;
    }
    if (invoker == null) {
      this.invoker = new ScrollInvoker(null); 
    } else {
      this.invoker = invoker;
    }
  }

  /**
   * 
   * @return 
   */
  public ScrollInvoker getInvoker() {
    return invoker;
  }
  

  /**
   * Gets the value set by the constructor.
   *
   * @return
   */
  public int getValue() {
    return value;
  }

  /**
   * Creates a new instance of {@linkplain Level} based on adding 1 to the current value.
   * If the current value is equal to {@linkplain #MAX} then the current instance is
   * returned.
   *
   * @return
   */
  public Level addOne(ScrollEvent evt) {
    Level result;
    if (this.value >= MAX) {
      result = this;
    } else {
      result = new Level(this.value + 1, new ScrollInvoker(evt));
    }
    return result;
  }

  /**
   * Creates a new instance of {@linkplain Level} based on subtracting 1 from to the
   * current value. If the current value is equal to {@linkplain #MIN} then the current
   * instance is returned.
   *
   * @return
   */
  public Level subtractOne(ScrollEvent evt) {
    Level result;
    if (this.value <= MIN) {
      result = this;
    } else {
      result = new Level(this.value - 1, new ScrollInvoker(evt));
    }
    return result;
  }

  @Override
  public String toString() {
    return "Level{" + "value=" + value + '}';
  }

}
