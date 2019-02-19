package com.windsim.wpls.view.io.sources;

import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.components.ChildNodeWrapper;
import javafx.beans.property.Property;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class RadioButtonsGroupController implements InitializingBean {

  @Autowired
  FxmlInitializer fxmlInitializer;
  @Autowired
  @Qualifier("importsettings_radiobtn_filesystem")
  private ChildNodeWrapper<RadioButton> fileSystemRBtn;
  @Autowired
  @Qualifier("importsettings_radiobtn_database")
  private ChildNodeWrapper<RadioButton> databaseRBtn;
  @Autowired
  @Qualifier("importsettings_radiobtn_piserver")
  private ChildNodeWrapper<RadioButton> piServerRBtn;

  @Autowired
  @Qualifier("selectedSourceType")
  private Property<SourceType> selectedSourceType;

  /**
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    this.fxmlInitializer.addListener((i) -> {
      ToggleGroup toggleGroup = new ToggleGroup();
      toggleGroup.getToggles().add(this.databaseRBtn.getNode());
      toggleGroup.getToggles().add(this.fileSystemRBtn.getNode());
      toggleGroup.getToggles().add(this.piServerRBtn.getNode());
      this.fileSystemRBtn.getNode().selectedProperty().addListener((obs, old, change) -> {
        if (change) {
          this.selectedSourceType.setValue(SupportedSourceType.FILE);
        }
      });
      this.databaseRBtn.getNode().selectedProperty().addListener((obs, old, change) -> {
        if (change) {
          this.selectedSourceType.setValue(SupportedSourceType.DATABASE);
        }
      });
      this.piServerRBtn.getNode().selectedProperty().addListener((obs, old, change) -> {
        if (change) {
          this.selectedSourceType.setValue(SupportedSourceType.PI);
        }
      });
      this.selectedSourceType.addListener((obs, old, change) -> {
        if (change != null) {
          this.fileSystemRBtn.getNode().setSelected(change == SupportedSourceType.FILE);
          this.databaseRBtn.getNode().setSelected(change == SupportedSourceType.DATABASE);
          this.piServerRBtn.getNode().setSelected(change == SupportedSourceType.PI);
        }
      });
      this.selectedSourceType.setValue(SupportedSourceType.FILE);
    });
  }

}
