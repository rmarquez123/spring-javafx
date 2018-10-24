package fx.panzoomcanvas.layers;

import javafx.beans.property.Property;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author rmarquez
 */
public abstract class RectangleLayer extends BaseLayer {
    
  private final Property<Color> color;
  /**
   * 
   * @param name
   * @param color 
   */
  public RectangleLayer(String name, Property<Color> color) {
    super(name);
    this.color = color;
  }
  
  /**
   * 
   * @param args 
   */
  @Override
  protected void onDraw(DrawArgs args) {
    GraphicsContext g = ((Canvas) args.getLayerCanvas()).getGraphicsContext2D(); 
    g.setFill(color.getValue());
    double areaX = args.getAreaX();
    double areaY = args.getAreaY();
    double areaWidth = args.getAreaWidth();
    double areaHeight = args.getAreaHeight();
    g.fillRect(areaX, areaY, areaWidth, areaHeight);
  }
}
