package fx.panzoomcanvas;

import fx.panzoomcanvas.bindings.MapBindings;
import fx.panzoomcanvas.core.FxPoint;
import fx.panzoomcanvas.projections.Level;
import fx.panzoomcanvas.projections.Projector;
import fx.panzoomcanvas.projections.ScreenEnvelope;
import fx.panzoomcanvas.projections.ScreenPoint;
import fx.panzoomcanvas.projections.ScrollInvoker;
import fx.panzoomcanvas.projections.VirtualEnvelope;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;

/**
 *
 * @author rmarquez
 */
public class FxCanvas extends Canvas {

  private static final ScreenPoint INITIAL_SCREEN_POINT = new ScreenPoint(0, 0);
  private final Content content;
  private final Property<VirtualEnvelope> virtualEnvelope = new SimpleObjectProperty<>();
  private final Property<ScreenEnvelope> screenEnvelope = new SimpleObjectProperty<>();
  private final Property<Level> level = new SimpleObjectProperty<>(new Level(0, null));
  private final Property<ScreenPoint> center = new SimpleObjectProperty<>(INITIAL_SCREEN_POINT);
  private final Projector projector;
  private final BooleanProperty scrolling = new SimpleBooleanProperty(false);

  /**
   *
   * @param content
   * @param projector
   */
  public FxCanvas(Content content, Projector projector) {
    if (projector == null) {
      throw new IllegalArgumentException("Projector cannot be null"); 
    }
    this.projector = projector;
    this.content = content;
    this.widthProperty().addListener((e) -> this.updateScreenView());
    this.heightProperty().addListener((e) -> this.updateScreenView());
    this.center.addListener((e) -> this.updateScreenView());
    this.level.addListener((observable, oldLevel, newLevel) -> {
      this.updateCenterAfterLevelChanged(newLevel, oldLevel);
    });

    this.screenEnvelope.addListener((e) -> this.updateVirtualView());
    this.parentProperty().addListener((obs, newVal, oldVal) -> {
      Platform.runLater(() -> {
        if (this.center.getValue() == INITIAL_SCREEN_POINT) {
          this.center.setValue(this.getCenterOfScreenPoint());
        }
      });
    });
    MapBindings.bindLevelScrolling(this);
    MapBindings.bindPanning(this);
    this.content.setVirtualCanvas(this);
  }

  /**
   *
   * @return
   */
  public final ScreenPoint getCenterOfScreenPoint() {
    return new ScreenPoint(0.5 * this.getWidth(), 0.5 * this.getHeight());
  }

  /**
   *
   * @return
   */
  public ReadOnlyProperty<ScreenEnvelope> screenEnvelopeProperty() {
    return screenEnvelope;
  }

  /**
   *
   * @return
   */
  public ReadOnlyProperty<VirtualEnvelope> virtualEnvelopeProperty() {
    return this.virtualEnvelope;
  }

  /**
   *
   * @return
   */
  public Property<Level> levelProperty() {
    return level;
  }

  /**
   *
   * @return
   */
  public Content getContent() {
    return content;
  }
  
  /**
   * 
   * @param geometricLayer 
   */
  public void zoomToLayer(GeometricLayer geometricLayer) {
    FxPoint value = geometricLayer.centerProperty().getValue();
    this.goToVirtualPoint(this.level.getValue().getValue(), value);
  }

  /**
   *
   * @param newLevel
   * @param point
   */
  public void goToVirtualPoint(int newLevel, FxPoint point) {
    ScreenEnvelope screenEnvVal = this.screenEnvelope.getValue();
    ScreenPoint refPoint = this.getProjector().projectGeoToScreen(point, screenEnvVal);
    ScreenPoint centerVal = this.getCenterOfScreenPoint();
    ScreenPoint diff = centerVal.difference(refPoint);
    double f = 1;
    ScreenPoint newCenter = this.center.getValue().add(diff.multiply(f));
    this.center.setValue(newCenter);
    new AnimationTimer() {
      @Override
      public void handle(long now) {
        final Level change;
        if (newLevel < level.getValue().getValue()) {
          change = level.getValue().subtractOne(null);
        } else if (newLevel > level.getValue().getValue()) {
          change = level.getValue().addOne(null);
        } else {
          change = null;
        }
        if (change != null) {
          level.setValue(change);
        } else {
          this.stop();
        }
      }
    }.start();

  }

  /**
   *
   * @return
   */
  public Property<ScreenPoint> centerProperty() {
    return center;
  }

  public ReadOnlyBooleanProperty scrollingProperty() {
    return scrolling;
  }

  /**
   *
   * @return
   */
  public Projector getProjector() {
    return projector;
  }

  private void updateCenterAfterLevelChanged(Level newLevel, Level oldLevel) {

    ScreenPoint refPoint;
    ScrollInvoker invoker = newLevel.getInvoker();
    try {
      if (!invoker.isScrollEvent()) {
        refPoint = this.getCenterOfScreenPoint();
      } else {
        this.scrolling.setValue(invoker.isScrolling());
        double x = invoker.getX();
        double y = invoker.getY();
        refPoint = new ScreenPoint(x, y);
      }
      ScreenPoint centerVal = FxCanvas.this.center.getValue();
      ScreenPoint diff = centerVal.difference(refPoint);
      double f = Math.pow(2.0, -(oldLevel.getValue() - newLevel.getValue()));
      ScreenPoint newCenter = refPoint.add(diff.multiply(f));
      FxCanvas.this.center.setValue(newCenter);
    } finally {
      this.scrolling.setValue(Boolean.FALSE);
    }

  }

  /**
   *
   */
  private void updateVirtualView() {
    ScreenEnvelope screenEnvVal = this.screenEnvelope.getValue();
    VirtualEnvelope newVal = this.projector.projectScreenToVirtualStrict(screenEnvVal);
    this.virtualEnvelope.setValue(newVal);
  }

  /**
   *
   */
  private void updateScreenView() {
    ScreenPoint min = new ScreenPoint(0, 0);
    ScreenPoint max = new ScreenPoint(this.getWidth(), this.getHeight());
    Level levelVal = this.level.getValue();
    ScreenPoint centerVal = this.center.getValue();
    ScreenEnvelope newScreenEnv = new ScreenEnvelope(min, max, levelVal, centerVal);
    this.screenEnvelope.setValue(newScreenEnv);
  }

  /**
   *
   * @param layerCanvas
   */
  void addLayerCanvas(Node layerCanvas) {
    ((StackPane) this.getParent()).getChildren().add(layerCanvas);
    StackPane.setAlignment(layerCanvas, Pos.TOP_LEFT);
  }

  /**
   *
   * @param layerCanvas
   */
  void removeLayerCanvas(Node layerCanvas) {
    Parent p = this.getParent();
    if (p != null) {
      ((StackPane) p).getChildren().remove(layerCanvas);
    }
  }

}
