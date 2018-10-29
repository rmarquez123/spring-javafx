package com.rm.panzoomcanvas.components;

import com.rm.panzoomcanvas.ParamsIntersects;
import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.core.SpatialRef;
import com.rm.panzoomcanvas.impl.polygon.DefaultPolygonSymbology;
import com.rm.panzoomcanvas.layers.DrawArgs;
import com.rm.panzoomcanvas.layers.polygon.PolygonLayer;
import com.rm.panzoomcanvas.layers.polygon.PolygonMarker;
import com.rm.panzoomcanvas.layers.polygon.PolygonPoints;
import com.rm.panzoomcanvas.layers.polygon.PolygonSource;
import com.rm.panzoomcanvas.projections.MapCanvasSR;
import javafx.beans.property.Property;
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
    super("Virtual Box", new DefaultPolygonSymbology(), new PolygonSource() {
      @Override
      public PolygonMarker<String> getScreenPoints(DrawArgs args) {
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
        return new PolygonMarker<>("vbox", new PolygonPoints(x, y, 4));
      }
      
      @Override
      public boolean intersects(ParamsIntersects args) {
        return false;
      }

      @Override
      public SpatialRef getSpatialRef() {
        return new MapCanvasSR();
      }
      
      @Override
      public boolean intersect(FxPoint refPoint) {
        return false;
      }
    });
    Property<Color> fillColorProperty = ((DefaultPolygonSymbology) super.getSymbology()).fillColorProperty();
    fillColorProperty.setValue(Color.BURLYWOOD);
    
    Property<Color> strokeColorProp = ((DefaultPolygonSymbology) super.getSymbology()).strokeColorProperty();
    strokeColorProp.setValue(Color.CORAL);
  }

}
