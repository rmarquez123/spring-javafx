package com.rm.panzoomcanvas.components;

import com.rm.panzoomcanvas.Content;
import com.rm.panzoomcanvas.ParamsIntersects;
import com.rm.panzoomcanvas.layers.DrawArgs;
import com.rm.panzoomcanvas.layers.polygon.PolygonLayer;
import com.rm.panzoomcanvas.layers.polygon.PolygonPoints;
import com.rm.panzoomcanvas.layers.polygon.PolygonSource;
import javafx.scene.paint.Color;


/**
 *
 * @author rmarquez
 */
public class VirtualBoxLayer extends PolygonLayer {
  
  /**
   * 
   */
  public VirtualBoxLayer() {
    super("Virtual Box", new PolygonSource() {
      @Override
      public PolygonPoints getScreenPoints(DrawArgs args) {
        double x0 = args.getAreaX(); 
        double y0 = args.getAreaY();
        double h = args.getAreaHeight();
        double w = args.getAreaWidth();
        double[] x = new double[]{
          x0, x0 + w, x0 + w, x0
        }; 
        double[] y = new double[]{
          y0, y0, y0 + h, y0 + h
        };
        return new PolygonPoints(x, y, 4); 
      }

      @Override
      public boolean intersects(ParamsIntersects args) {
        return false;
      }
    });
    super.fillColorProperty().setValue(Color.BURLYWOOD);
  }
  

  
  
}
