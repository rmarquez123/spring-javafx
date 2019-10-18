package com.rm.springjavafx.popup;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.springframework.beans.factory.annotation.Configurable;

/**
 *
 * @author rmarquez
 */
@Configurable
public class Popup {

  private final BooleanProperty showProperty = new SimpleBooleanProperty(false);
  private final Property<Node> contentNodeProperty = new SimpleObjectProperty<>();
  private final Property<PopupContent> popupControllerProperty = new SimpleObjectProperty<>();
  private final Property<Window> windowProperty = new SimpleObjectProperty<>();

  private final Property<Object> userDataProperty = new SimpleObjectProperty<>();

  private Stage alert;

  /**
   *
   */
  public Popup() {
    this.popupControllerProperty.addListener((obs, old, change) -> {
      change.setPopupWindow(this);
    });
    this.showProperty.addListener((evt) -> {
      if (!showProperty.getValue()) {
        this.getAlert().close();
      }
    });
    this.windowProperty.addListener((obs, old, change) -> {
      this.setModality();
    });
  }

  public void setNode(Node node) {
    this.contentNodeProperty.setValue(node);
  }

  /**
   *
   * @param controller
   */
  public void setController(PopupContent controller) {
    this.popupControllerProperty.setValue(controller);
  }

  /**
   *
   * @param node
   * @param popupController
   */
  public Popup(Node node, PopupContent popupController) {
    this();
    if (node == null) {
      throw new NullPointerException("Content cannot be null");
    }
    if (popupController == null) {
      throw new NullPointerException("Popup Controller cannot be null.");
    }
    this.contentNodeProperty.setValue(node);
    this.popupControllerProperty.setValue(popupController);
  }

  /**
   *
   * @return
   */
  public BooleanProperty showProperty() {
    return showProperty;
  }

  /**
   *
   * @return
   */
  public Property<Node> contentNodeProperty() {
    return this.contentNodeProperty;
  }

  /**
   *
   * @return
   */
  public Property<Window> windowProperty() {
    return this.windowProperty;
  }

  /**
   *
   * @return
   */
  public Property<PopupContent> popupControllerProperty() {
    return this.popupControllerProperty;
  }

  /**
   *
   * @return
   */
  public Object getUserData() {
    return this.userDataProperty.getValue();
  }

  /**
   *
   * @param userData
   * @return
   */
  public void setUserData(Object userData) {
    this.userDataProperty.setValue(userData);
  }

  /**
   *
   */
  public void show() {
    Stage alertWindow = this.getAlert();
    if (!alertWindow.isShowing()) {
      alertWindow.showAndWait();
    }
  }

  /**
   *
   * @return
   */
  private Stage getAlert() {
    if (this.alert == null) {
      this.createAlert();
      this.alert.showingProperty().addListener((evt) -> this.onAlertShowing());
      this.alert.addEventFilter(KeyEvent.KEY_RELEASED, this::closeIfEscapeKey);
    }
    return this.alert;
  }

  /**
   *
   * @param evt
   */
  private void closeIfEscapeKey(KeyEvent evt) {
    if (evt.getCode() == KeyCode.ESCAPE) {
      this.showProperty.set(false);
    }
  }

  /**
   *
   */
  private void onAlertShowing() {
    if (!this.alert.showingProperty().getValue()) {
      try {
        (this.popupControllerProperty).getValue().onClose();
      } catch (IOException ex) {
        Logger.getLogger(Popup.class.getName()).log(Level.SEVERE, null, ex);
      }
    } else {
      Window window = this.windowProperty.getValue();
      if (window != null) {
        double xPos = window.getX() + 0.25 * window.getWidth();
        double yPos = window.getY() + 0.25 * window.getHeight();
        this.alert.setX(xPos);
        this.alert.setY(yPos);
        this.alert.setAlwaysOnTop(false);
        this.alert.getScene().getWindow().setX(xPos);
        this.alert.getScene().getWindow().setY(yPos);
        ObservableList<String> styleSheets = window.getScene()
          .getRoot().getStylesheets();
        this.alert.getScene().getStylesheets().addAll(styleSheets);
        ObservableList<String> styleSheets2 = window.getScene().getStylesheets();
        this.alert.getScene().getStylesheets().addAll(styleSheets2);
        if (window instanceof Stage) {
          List<Image> icons = ((Stage) window).getIcons();
          this.alert.getIcons().addAll(icons);
        }
      }
    }
    this.showProperty.setValue(this.alert.showingProperty().getValue());
  }

  /**
   *
   */
  private void createAlert() {
    Node contentNode = this.contentNodeProperty.getValue();
    Scene dialogScene = new Scene(new AnchorPane(contentNode));
    AnchorPane.setBottomAnchor(contentNode, 0.0);
    AnchorPane.setLeftAnchor(contentNode, 0.0);
    AnchorPane.setRightAnchor(contentNode, 0.0);
    AnchorPane.setTopAnchor(contentNode, 0.0);
    this.alert = new Stage();
    this.alert.setTitle("");
    this.alert.setScene(dialogScene);
    this.setModality();
  }

  /**
   *
   */
  private void setModality() {
    Window window = this.windowProperty.getValue();
    if (window != null && this.alert != null) {
      this.alert.initOwner(window);
      this.alert.initModality(Modality.WINDOW_MODAL);
    }
  }
}
