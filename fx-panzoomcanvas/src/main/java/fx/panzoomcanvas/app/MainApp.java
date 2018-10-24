package fx.panzoomcanvas.app;

import fx.panzoomcanvas.Content;
import fx.panzoomcanvas.FxCanvas;
import fx.panzoomcanvas.Layer;
import fx.panzoomcanvas.components.PositionBar;
import fx.panzoomcanvas.components.VirtualBoxLayer;
import fx.panzoomcanvas.core.FxPoint;
import fx.panzoomcanvas.core.SpatialRef;
import fx.panzoomcanvas.projections.GeometryProjection;
import fx.panzoomcanvas.projections.MapCanvasSR;
import fx.panzoomcanvas.projections.Projector;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import static javafx.application.Application.launch;

public class MainApp extends Application {
  
  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public void start(Stage stage) throws Exception {
    Content content = new Content();
    ObservableList<Layer> value = content.getLayers().getValue();
    value.add(new VirtualBoxLayer()); 
    FxCanvas mapCanvas = new FxCanvas(content, new Projector(new MapCanvasSR(), new GeometryProjection() {
      @Override
      public FxPoint project(FxPoint geomPoint, SpatialRef baseSpatialRef) {
        return geomPoint;
      }
    }));
    PositionBar positionBar = new PositionBar(mapCanvas);
    StackPane root = new StackPane(mapCanvas, positionBar);
    root.getChildren().addListener((ListChangeListener.Change<? extends Node> c) -> {
      Platform.runLater(()->{
        positionBar.toFront();
      });
    });
    StackPane.setAlignment(mapCanvas, Pos.CENTER);
    StackPane.setAlignment(positionBar, Pos.BOTTOM_RIGHT);
    mapCanvas.widthProperty().bind(root.widthProperty());
    mapCanvas.heightProperty().bind(root.heightProperty());
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.setWidth(500);
    stage.setHeight(500);
    stage.show();
  }

  /**
   * The main() method is ignored in correctly deployed JavaFX application. main() serves
   * only as fallback in case the application can not be launched through deployment
   * artifacts, e.g., in IDEs with limited FX support. NetBeans ignores main().
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }

}
