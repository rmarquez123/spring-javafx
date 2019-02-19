package com.windsim.wpls.view.map;

import com.rm.fxmap.projections.Wgs84Spheroid;
import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.core.Level;
import com.rm.panzoomcanvas.impl.points.ArrayPointsSource;
import com.rm.panzoomcanvas.layers.points.PointMarker;
import com.rm.panzoomcanvas.layers.points.PointSymbology;
import com.rm.panzoomcanvas.layers.points.PointsLayer;
import com.rm.panzoomcanvas.layers.points.PointsSource;
import com.vividsolutions.jts.geom.Point;
import gov.inl.glass3.linesolver.Model;
import gov.inl.glass3.modelpoints.ModelPoint;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
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
public class ModelPointsLayerController implements InitializingBean {

  @Autowired
  @Qualifier("samplemap")
  public FxCanvas sampleMap;

  @Autowired
  @Qualifier(value = "glassModelProperty")
  private Property<Model> modelProperty;

  @Autowired
  @Qualifier(value = "selectedModelPoint")
  private Property<ModelPoint> modelPointProperty;
  
  @Autowired
  private ModelPointSymbolizer modelPointSymbolizer;

  
  private final ObjectProperty<PointsLayer<Object>> pointsLayerProperty = new SimpleObjectProperty<>();

  @Override
  public void afterPropertiesSet() throws Exception {
    
    this.modelProperty.addListener((obs, old, change) -> {
      if (change != null) {
        String name = "Model Points";
        PointSymbology symbolizer = this.modelPointSymbolizer.getSymbolizer();
        PointMarker[] markers = new PointMarker[change.getModelPoints().size()];
        int count = -1;
        for (ModelPoint modelPoint : change.getModelPoints()) {
          count++;
          Point point = modelPoint.getGeometry().getPoint();
          Wgs84Spheroid wgs84Spheroid = new Wgs84Spheroid();
          FxPoint fxPoint = new FxPoint(point.getX(), point.getY(), wgs84Spheroid);
          markers[count] = new PointMarker(modelPoint, fxPoint);
        }
        PointsSource<Object> source = new ArrayPointsSource(markers);
        PointsLayer<Object> modelPointsLayer = new PointsLayer<>(name, symbolizer, source);
        modelPointsLayer.hoverableProperty().setValue(true);
        modelPointsLayer.selectableProperty().setValue(true);
          
        modelPointsLayer.selectedMarkersProperty().addListener((observable, oldValue, newValue) -> {
          if (newValue != null && !newValue.isEmpty()) {
            ModelPoint modelPoint = (ModelPoint) newValue.get(0).getUserObject();
            if (!Objects.equals(this.modelPointProperty.getValue(), modelPoint)) {
              modelPointProperty.setValue(modelPoint);
            }
          }
        });
        this.pointsLayerProperty.setValue(modelPointsLayer);
      } else {
        this.pointsLayerProperty.setValue(null);
      }
    });

    this.pointsLayerProperty.addListener((obs, old, change) -> {
      sampleMap.getContent().getLayers().remove(old);
      if (change != null) {
        sampleMap.getContent().getLayers().add(change);
        this.updateSelectedModelPointsMarker();
      }
    });

    this.modelPointProperty.addListener((obs, old, change) -> {
      this.updateSelectedModelPointsMarker();
    });
  }

  /**
   *
   */
  private void updateSelectedModelPointsMarker() {
    ModelPoint change = this.modelPointProperty.getValue();
    PointsLayer<Object> pointsLayer = pointsLayerProperty.getValue();
    if (change != null && pointsLayer != null) {
      pointsLayer.selectedMarkersProperty().clear();
      PointMarker pointMarker = pointsLayer.getMarker(change);
      pointsLayer.selectedMarkersProperty().setValue(FXCollections.observableArrayList(pointMarker));
      if (!sampleMap.isInView(pointMarker)) {
        Level level = sampleMap.levelProperty().getValue();
        if (level != null) {

          int selectedLevel = Math.max(level.getValue(), 12);
          sampleMap.zoomToVirtualPoint(selectedLevel, pointMarker.getPoint());
        } else {
          sampleMap.zoomToVirtualPoint(12, pointMarker.getPoint());
        }
      }
    }
  }
}
