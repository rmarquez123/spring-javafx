package com.windsim.wpls.view.map;

import com.rm.fxmap.projections.Wgs84Spheroid;
import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.impl.points.ArrayPointsSource;
import com.rm.panzoomcanvas.impl.points.PointShapeSymbology;
import com.rm.panzoomcanvas.layers.points.PointMarker;
import com.rm.panzoomcanvas.layers.points.PointsLayer;
import com.rm.panzoomcanvas.layers.points.PointsSource;
import com.vividsolutions.jts.geom.Point;
import gov.inl.glass3.linesolver.Model;
import gov.inl.glass3.weather.WeatherStation;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Lazy(false)
public class WeatherStationsLayerController implements InitializingBean {

  @Autowired
  @Qualifier("samplemap")
  public FxCanvas sampleMap;

  @Autowired
  @Qualifier(value = "glassModelProperty")
  private Property<Model> modelProperty;
  
  private final ObjectProperty<PointsLayer<Object>> pointsLayerProperty = new SimpleObjectProperty<>();

  @Override
  public void afterPropertiesSet() throws Exception {
    this.modelProperty.addListener((obs, old, change) -> {
      if (change != null) {
        String name = "Weather Stations";
        PointShapeSymbology symbology = new PointShapeSymbology();
        symbology.fillColorProperty().setValue(Color.GREEN);
        symbology.strokeColorProperty().setValue(Color.YELLOWGREEN);
        
        PointMarker[] markers = new PointMarker[change.getWeatherStations().size()];
        int count = -1;
        for (WeatherStation station : change.getWeatherStations().asList()) {
          count++;
          Point point = station.getGeometry();
          Wgs84Spheroid wgs84Spheroid = new Wgs84Spheroid();
          FxPoint fxPoint = new FxPoint(point.getX(), point.getY(), wgs84Spheroid);
          markers[count] = new PointMarker(station, fxPoint);
        }
        PointsSource<Object> source = new ArrayPointsSource(markers);
        PointsLayer<Object> pointsLayer = new PointsLayer<>(name, symbology, source);
        this.pointsLayerProperty.setValue(pointsLayer);
      } else {
        this.pointsLayerProperty.setValue(null);
      }
    });

    this.pointsLayerProperty.addListener((obs, old, change) -> {
      sampleMap.getContent().getLayers().remove(old);
      if (change != null) {
        sampleMap.getContent().getLayers().add(change);
        this.updateSelectedStationsMarker();
      }
    });

  }

  /**
   *
   */
  private void updateSelectedStationsMarker() {
  }

}
