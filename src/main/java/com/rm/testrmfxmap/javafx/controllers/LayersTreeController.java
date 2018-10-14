package com.rm.testrmfxmap.javafx.controllers;

import com.gei.geicanvas.Content;
import com.gei.geicanvas.Layer;
import com.gei.geicanvas.components.CenterLayer;
import com.gei.geicanvas.core.FxEnvelope;
import com.gei.geicanvas.core.FxPoint;
import com.gei.geifxmaps.basemap.BaseMapTileLayer;
import com.gei.geifxmaps.pointlayer.PointLayer;
import com.gei.geifxmaps.pointlayer.PointSymbolizer;
import com.gei.geifxmaps.rasters.ImageSource;
import com.gei.geifxmaps.rasters.Raster;
import com.gei.geifxmaps.spatialrefs.Wgs84Spheroid;
import com.gei.geifxmaps.tiles.FileTileCache;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * FXML Controller class
 *
 * @author rmarquez
 */
@Component
public class LayersTreeController implements Initializable, InitializingBean {

  private ObservableList<Layer> layers;
  private Content content; 
        
  public LayersTreeController() {
  }
  
  /**
   *
   * @param layers
   */
  public void setLayers(ObservableList<Layer> layers) {
    this.layers = layers;
  }

  

  public void initLayers() {
    File baseDir = new File("C:\\Users\\rmarquez\\AppData\\Roaming\\TileCache");
    this.layers.add(new BaseMapTileLayer(new FileTileCache(baseDir, "Test")));
    PointLayer pointLayer = new PointLayer("Sample Raster", new PointSymbolizer());
    GeometryFactory geomFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
    Point point = geomFactory.createPoint(new Coordinate(-120.43, 37.36));
    pointLayer.getGeometries().getValue().add(point);
    ImageSource imgSrce = () -> {
      try {
        File file = new File("C:\\Users\\rmarquez\\Desktop\\testimage.png");
        FileInputStream result = new FileInputStream(file);
        return result;
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    };
    Raster raster = new Raster(imgSrce, () -> {
      FxPoint min = new FxPoint(-124.8214997, 31.978502, new Wgs84Spheroid());
      FxPoint max = new FxPoint(-112.7779722, 42.1043853, new Wgs84Spheroid());
      return new FxEnvelope(min, max);
    });
//    layers.add(new RasterLayer("Sample Raster", raster));
    this.layers.add(pointLayer);
    this.layers.add(new CenterLayer("Center"));
  }

  @Override
  public void afterPropertiesSet() throws Exception {

  }

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    
  }
}
