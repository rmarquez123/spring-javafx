package com.rm.panzoomcanvas.app;

import com.rm.panzoomcanvas.Content;
import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.Layer;
import com.rm.panzoomcanvas.components.CenterLayer;
import com.rm.panzoomcanvas.components.PositionBar;
import com.rm.panzoomcanvas.components.VirtualBoxLayer;
import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.core.SpatialRef;
import com.rm.panzoomcanvas.layers.line.LineLayer;
import com.rm.panzoomcanvas.core.GeometryProjection;
import com.rm.panzoomcanvas.layers.impl.line.FixedLineLayerSource;
import com.rm.panzoomcanvas.layers.impl.points.ArrayPointsSource;
import com.rm.panzoomcanvas.layers.points.PointsLayer;
import com.rm.panzoomcanvas.layers.points.PointMarker;
import com.rm.panzoomcanvas.layers.LayerTooltip;
import com.rm.panzoomcanvas.layers.impl.points.DefaultPointSymbology;
import com.rm.panzoomcanvas.projections.MapCanvasSR;
import com.rm.panzoomcanvas.projections.Projector;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;

/**
 * The purpose of this main application is to test the basic and default
 * features and functions of panzoomcanvas api.
 *
 * @author rmarquez
 */
public class MainApp extends Application {

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public void start(Stage stage) throws Exception {
    FxCanvas mapCanvas = this.createMap();
    this.addLayers(mapCanvas);
    Scene scene = new Scene(mapCanvas.getParent());
    stage.setScene(scene);
    stage.setWidth(500);
    stage.setHeight(500);
    stage.show();
  }

  /**
   *
   * @param mapCanvas
   */
  private void addLayers(FxCanvas mapCanvas) {
    ObservableList<Layer> value = mapCanvas.getContent().getLayers().getValue();
    value.add(new VirtualBoxLayer());
    value.add(new CenterLayer("center"));
    LineLayer lineLayer = this.getLineLayer();
    value.add(lineLayer);
    PointsLayer pointsLayer = this.getPointsLayer();
    value.add(pointsLayer);
  }
  
  /**
   * 
   * @param p1
   * @param p2
   * @return 
   */
  private LineLayer getLineLayer() {
    
    final SpatialRef sr = new MapCanvasSR();
    FxPoint p1 = new FxPoint(0.0, 0.0, sr);
    FxPoint p2 = new FxPoint(120.0, 120.0, sr);
    LineLayer lineLayer = new LineLayer("line", new FixedLineLayerSource<>("line", p1, p2));
    lineLayer.selectableProperty().setValue(Boolean.TRUE);
    lineLayer.hoverableProperty().setValue(Boolean.TRUE);
    return lineLayer;
  }

  /**
   *
   * @return
   */
  private PointsLayer getPointsLayer() {
    FxPoint fxPoint = new FxPoint(120, 120, new MapCanvasSR());
    PointMarker<String> pointMarker = new PointMarker<>("Point 1", fxPoint);
    ArrayPointsSource singlePointSource = new ArrayPointsSource(pointMarker);

    DefaultPointSymbology symbology = new DefaultPointSymbology();
    symbology.fillColorProperty().setValue(Color.ROSYBROWN);
    symbology.strokeColorProperty().setValue(Color.BLACK);
    symbology.selected.fillColorProperty().setValue(Color.ANTIQUEWHITE);
    
    PointsLayer pointsLayer = new PointsLayer("points", symbology, singlePointSource);
    pointsLayer.hoverableProperty().setValue(Boolean.TRUE);
    pointsLayer.selectableProperty().setValue(Boolean.TRUE);
    pointsLayer.setTooltip(new LayerTooltip.Builder()
            .setHeightOffset(54));
    pointsLayer.selectedMarkersProperty().addListener((obs, old, change) -> {
      System.out.println(change);
    });
    return pointsLayer;
  }

  /**
   * Creates the map.
   *
   * @return
   */
  private FxCanvas createMap() {
    Content content = new Content();
    FxCanvas mapCanvas = new FxCanvas(content, new Projector(new MapCanvasSR(), new GeometryProjection() {
      @Override
      public FxPoint project(FxPoint geomPoint, SpatialRef baseSpatialRef) {
        return geomPoint;
      }
    }));
    PositionBar positionBar = new PositionBar(mapCanvas);
    StackPane.setAlignment(positionBar, Pos.BOTTOM_RIGHT);
    StackPane root = new StackPane(mapCanvas, positionBar);
    root.getChildren().addListener((ListChangeListener.Change<? extends Node> c) -> {
      Platform.runLater(() -> {
        positionBar.toFront();
      });
    });
    StackPane.setAlignment(mapCanvas, Pos.CENTER);
    mapCanvas.widthProperty().bind(root.widthProperty());
    mapCanvas.heightProperty().bind(root.heightProperty());
    return mapCanvas;
  }

  /**
   * The main() method is ignored in correctly deployed JavaFX application.
   * main() serves only as fallback in case the application can not be launched
   * through deployment artifacts, e.g., in IDEs with limited FX support.
   * NetBeans ignores main().
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }

}
