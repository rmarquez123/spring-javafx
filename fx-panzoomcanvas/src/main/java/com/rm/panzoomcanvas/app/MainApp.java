package com.rm.panzoomcanvas.app;

import com.rm.panzoomcanvas.Content;
import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.ParamsIntersects;
import com.rm.panzoomcanvas.Layer;
import com.rm.panzoomcanvas.components.CenterLayer;
import com.rm.panzoomcanvas.components.PositionBar;
import com.rm.panzoomcanvas.components.VirtualBoxLayer;
import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.core.SpatialRef;
import com.rm.panzoomcanvas.layers.line.LineLayer;
import com.rm.panzoomcanvas.layers.line.LineLayerSource;
import com.rm.panzoomcanvas.core.GeometryProjection;
import com.rm.panzoomcanvas.core.SpatialUtils;
import com.rm.panzoomcanvas.layers.points.PointsLayer;
import com.rm.panzoomcanvas.layers.points.PointsSource;
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
import javafx.util.Pair;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;

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
    value.add(new LineLayer("line", new LineLayerSource() {
      private SpatialRef sr = new MapCanvasSR();
      Pair<FxPoint, FxPoint> points = new Pair<>(new FxPoint(0.0, 0.0, sr), new FxPoint(120.0, 120.0, sr));
      @Override
      public Pair<FxPoint, FxPoint> getFxPoints() {
        return this.points;
      }

      @Override
      public boolean intersects(ParamsIntersects screenPoint) {
        return false;
      }
    }));
    
    value.add(new PointsLayer("points", new PointsSource() {
      private final FxPoint fxPoint = new FxPoint(120, 120, new MapCanvasSR());
      @Override
      public int getNumPoints() {
        return 1;
      }

      @Override
      public FxPoint getFxPoint(int i) {
        return fxPoint;
      }
        
      /**
       * {@inheritDoc}
       * <p>
       * OVERRIDE: </p>
       */
      @Override
      public boolean intersects(ParamsIntersects args) {
        FxPoint geomPoint = args.getGeomPoint(new MapCanvasSR());
        boolean result = SpatialUtils.intersects(this.fxPoint, geomPoint, 5);
        return result;
      }
    })); 
  }

  /**
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
