package com.rm.springjavafx.popup;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
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
   */
  public void show() {
    Stage alertWindow = this.getAlert();
    alertWindow.showAndWait();
  }

  /**
   *
   * @return
   */
  private Stage getAlert() {
    if (this.alert == null) {
      Node contentNode = this.contentNodeProperty.getValue();
      Scene dialogScene = new Scene(new AnchorPane(contentNode));
      AnchorPane.setBottomAnchor(contentNode, 0.0);
      AnchorPane.setLeftAnchor(contentNode, 0.0);
      AnchorPane.setRightAnchor(contentNode, 0.0);
      AnchorPane.setTopAnchor(contentNode, 0.0);

      this.alert = new Stage();
      this.alert.setTitle("");
      this.alert.setScene(dialogScene);
      this.alert.showingProperty().addListener((evt) -> {

        if (!this.alert.showingProperty().getValue()) {
          try {
            (this.popupControllerProperty).getValue().onClose();
          } catch (IOException ex) {
            Logger.getLogger(Popup.class.getName()).log(Level.SEVERE, null, ex);
          }
        } else {
          Window window = this.windowProperty.getValue();
          if (window != null) {
            double xPos = window.getX() + 0.25*window.getWidth();
            double yPos = window.getY() + 0.25*window.getHeight();
            this.alert.setX(xPos);
            this.alert.setY(yPos);
            this.alert.setAlwaysOnTop(true);
            alert.getScene().getWindow().setX(xPos);
            alert.getScene().getWindow().setY(yPos);
            ObservableList<String> styleSheets = window.getScene().getRoot().getStylesheets(); 
            alert.getScene().getStylesheets().addAll(styleSheets); 
          }
        }
        this.showProperty.setValue(this.alert.showingProperty().getValue());
      });

    }
    return this.alert;
  }
}
