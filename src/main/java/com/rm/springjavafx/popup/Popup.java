package com.rm.springjavafx.popup;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author rmarquez
 */
public class Popup {

  private final Node contentNode;
  private BooleanProperty showProperty = new SimpleBooleanProperty(false);
  private PopupContent popupController;
  private Stage alert;

  /**
   *
   * @param node
   */
  public Popup(Node node, PopupContent popupController) {
    if (node == null) {
      throw new NullPointerException("Content cannot be null");
    }
    if (popupController == null) {
      throw new NullPointerException("Popup Controller cannot be null.");
    }
    this.contentNode = node;
    this.popupController = popupController;
    this.popupController.setPopupWindow(this);
    this.showProperty.addListener((evt) -> {
      if (!showProperty.getValue()) {
        this.getAlert().close();
      }
    });

  }

  /**
   *
   * @return
   */
  public BooleanProperty getShowProperty() {
    return showProperty;
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
      Scene dialogScene = new Scene(new AnchorPane(this.contentNode));
      AnchorPane.setBottomAnchor(this.contentNode, 0.0);
      AnchorPane.setLeftAnchor(this.contentNode, 0.0);
      AnchorPane.setRightAnchor(this.contentNode, 0.0);
      AnchorPane.setTopAnchor(this.contentNode, 0.0);
      this.alert = new Stage();
      this.alert.setTitle("");
      this.alert.setScene(dialogScene);
      this.alert.showingProperty().addListener((evt) -> {
        if (!this.alert.showingProperty().getValue()) {
          try {
            (this.popupController).onClose();
          } catch (IOException ex) {
            Logger.getLogger(Popup.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
        this.showProperty.setValue(this.alert.showingProperty().getValue());
      });

    }
    return this.alert;
  }
}
