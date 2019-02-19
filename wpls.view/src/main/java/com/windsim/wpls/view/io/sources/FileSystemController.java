package com.windsim.wpls.view.io.sources;

import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.components.ChildNodeWrapper;
import com.windsim.wpls.view.loadseries.RequestLoadSeriesAction;
import com.windsim.wpls.view.projectopen.RequestProjectOpenAction;
import java.io.File;
import javafx.beans.property.Property;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
public class FileSystemController implements InitializingBean {

  @Autowired
  FxmlInitializer fxmlInitializer;
  @Autowired
  @Qualifier("modelConfigurationFile")
  Property<File> modelConfigurationFile;

  @Autowired
  @Qualifier("seriesDirectoryHistorical")
  Property<File> seriesDirectory;

  @Autowired
  @Qualifier("importsettings_txtfield_modelconf")
  ChildNodeWrapper<TextField> modelConfTxtField;

  @Autowired
  @Qualifier("importsettings_txtfield_seriesdir")
  ChildNodeWrapper<TextField> seriesDirTxtField;

  @Autowired
  @Qualifier("importsettings_btn_modelconf")
  ChildNodeWrapper<Button> modelConfBtn;

  @Autowired
  @Qualifier("importsettings_btn_seriesdir")
  ChildNodeWrapper<Button> seriesDirBtn;

  @Autowired
  @Qualifier("requestProjectOpenAction")
  Property<RequestProjectOpenAction> requestProjectOpenActionProperty;

  @Autowired
  @Qualifier("selectedSourceType")
  private Property<SourceType> selectedSourceType;

  @Autowired
  @Qualifier("requestLoadSeriesAction")
  Property<RequestLoadSeriesAction> requestLoadSeriesActionProperty;

  @Override
  public void afterPropertiesSet() throws Exception {

    this.fxmlInitializer.addListener((i) -> {
      this.seriesDirectory.addListener((obs, old, change) -> {
        if (change != null) {
          this.seriesDirTxtField.getNode().setText(change.toString());
        } else {
          this.seriesDirTxtField.getNode().setText("");
        }
      });
      this.modelConfigurationFile.addListener((obs, old, change) -> {
        if (change != null) {
          this.modelConfTxtField.getNode().setText(change.toString());
        } else {
          this.modelConfTxtField.getNode().setText("");
        }
      });

      this.modelConfBtn.getNode().setOnAction((evt) -> {
        modelConfBtn.getNode().disableProperty().setValue(true);
        try {
          requestProjectOpenActionProperty.getValue().onAction((m) -> {
          });
        } finally {
          modelConfBtn.getNode().disableProperty().setValue(false);
        }
      });
      this.seriesDirBtn.getNode().setOnAction((evt) -> {
        Button button = this.seriesDirBtn.getNode();
        button.disableProperty().setValue(true);
        try {
          this.requestLoadSeriesActionProperty.getValue().onAction();
        } finally {
          button.disableProperty().setValue(false);
        }
      });
      this.selectedSourceType.addListener((obs, old, change) -> {
        this.updateDisabledState();
      });
    });

  }

  private void updateDisabledState() {
    SourceType sourceType = this.selectedSourceType.getValue();
    this.seriesDirBtn.getNode().disableProperty().setValue(sourceType != SupportedSourceType.FILE);
    this.seriesDirTxtField.getNode().disableProperty().setValue(sourceType != SupportedSourceType.FILE);
    this.modelConfBtn.getNode().disableProperty().setValue(sourceType != SupportedSourceType.FILE);
    this.modelConfTxtField.getNode().disableProperty().setValue(sourceType != SupportedSourceType.FILE);
  }

}
