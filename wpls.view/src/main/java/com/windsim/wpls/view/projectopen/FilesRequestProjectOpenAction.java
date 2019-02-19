package com.windsim.wpls.view.projectopen;

import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.popup.Popup;
import gov.inl.glass3.linesolver.Model;
import gov.inl.glass3.linesolver.ModelLoadersProvider;
import gov.inl.glass3.linesolver.impl.files.FilesModelLoaderProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.function.Consumer;
import javafx.beans.property.Property;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
class FilesRequestProjectOpenAction implements RequestProjectOpenAction {

  @Autowired
  FxmlInitializer fxmlInitializer;
  @Autowired
  @Qualifier(value = "confirmOpenProjectPopup")
  private Popup confirmOpenProjectPopup;
  @Autowired
  ConfirmProjectOpenController confirmProjectOpenController;
  @Autowired
  @Qualifier("modelConfigurationFile")
  Property<File> seriesDirectoryProperty;

  /**
   *
   */
  @Override
  public void onAction(Consumer<Model> consumer) {
    FileChooser chooser = new FileChooser();
    chooser.setTitle("Select a .glass file");
    chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Glass model file", "*.glass"));
    File glassFile = chooser.showOpenDialog(fxmlInitializer.getMainRoot().getScene().getWindow());
    if (glassFile != null) {
      ModelLoadersProvider loadersProvider = new FilesModelLoaderProvider(() -> {
        try {
          return new FileInputStream(glassFile);
        } catch (FileNotFoundException ex) {
          throw new RuntimeException(ex);
        }
      });
      confirmProjectOpenController.openModel(loadersProvider, (t) -> {
        seriesDirectoryProperty.setValue(glassFile); 
        consumer.accept(t);
      });
      confirmOpenProjectPopup.show();
    }

  }

}
