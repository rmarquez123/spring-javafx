package com.windsim.wpls.view.projectopen;

import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.popup.Popup;
import com.rm.springjavafx.popup.PopupContent;
import gov.inl.glass3.linesolver.Model;
import gov.inl.glass3.linesolver.ModelController;
import gov.inl.glass3.linesolver.ModelLoadersProvider;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component("confirm_project_opencontroller")
class ConfirmProjectOpenController implements Initializable, PopupContent {

  private Popup popup; 

  @Autowired
  private FxmlInitializer fxmlInitializer;

  @FXML
  Button loadDataBtn;
  @FXML
  Button cancelBtn;

  @Autowired
  @Qualifier(value = "glassModelProperty")
  private Property<Model> modelProperty;

  @Autowired
  @Qualifier(value = "confirmOpenModelProperty")
  private final Property<Model> localModelProperty = new SimpleObjectProperty<>();

  @Autowired
  @Qualifier("modelConfigurationFile")
  Property<File> modelConfigurationFile;

  /**
   *
   * @param location
   * @param resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    loadDataBtn.setOnAction((evt) -> {
      this.popup.showProperty().setValue(false);
      modelProperty.setValue(localModelProperty.getValue());
      this.localModelProperty.setValue(null);
    });
    cancelBtn.setOnAction((evt) -> {
      popup.showProperty().setValue(false);
    });
    this.localModelProperty.addListener((obs, old, change) -> {
      loadDataBtn.disableProperty().setValue(this.localModelProperty.getValue() == null);

    });
    loadDataBtn.disableProperty().setValue(this.localModelProperty.getValue() == null);
  }

  @Override
  public void setPopupWindow(Popup popup) {
    this.popup = popup;

    Platform.runLater(() -> {
      this.fxmlInitializer.addListener((i) -> {
        if (this.fxmlInitializer.getMainRoot().getScene() == null) {
          this.fxmlInitializer.getMainRoot().sceneProperty().addListener((s) -> {
            Scene scene = this.fxmlInitializer.getMainRoot().getScene();
            this.popup.windowProperty().bind(scene.windowProperty());
          });
        } else {
          Scene scene = this.fxmlInitializer.getMainRoot().getScene();
          this.popup.windowProperty().setValue(scene.getWindow());
          loadDataBtn.requestFocus();
        }
      });
    });
  }

  /**
   * 
   * @param glassFile 
   */
  public void openModel(ModelLoadersProvider provider, Consumer<Model> on) {
    ModelController modelController = new ModelController(provider);
    Model model = modelController.loadModel();
    if (model != null) {
      this.localModelProperty.setValue(model);
      on.accept(model);
    }
  }

  @Override
  public void onClose() throws IOException {

  }
}
