package com.windsim.wpls.view.io.sources;

import com.rm.datasources.DbConnection;
import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.components.ChildNodeWrapper;
import com.rm.springjavafx.popup.Popup;
import com.windsim.wpls.view.loadseries.RequestLoadSeriesAction;
import com.windsim.wpls.view.projectopen.RequestProjectOpenAction;
import javafx.beans.property.Property;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class DatabaseController implements InitializingBean {

  @Autowired
  FxmlInitializer fxmlInitializer;

  @Autowired
  @Qualifier(value = "configureDbConnectionPopup")
  private Popup configureDbConnectionPopup;

  @Autowired
  @Qualifier("importsettings_btn_dbconnection")
  ChildNodeWrapper<Button> dbConnectionBtn;

  @Autowired
  @Qualifier("importsettings_btn_dbloaddata")
  ChildNodeWrapper<Button> dbLoadDataBtn;

  @Autowired
  @Qualifier("importsettings_btn_dbloadseries")
  ChildNodeWrapper<Button> dbLoadSeriesBtn;

  @Autowired
  @Qualifier("importsettings_txtfield_dburl")
  ChildNodeWrapper<TextField> urlTxtField;

  @Autowired
  @Qualifier("importsettings_txtfield_user")
  ChildNodeWrapper<TextField> userTxtField;

  @Autowired
  @Qualifier("importsettings_txtfield_password")
  ChildNodeWrapper<PasswordField> passwordTxtField;
  

  @Autowired
  @Qualifier("selectedSourceType")
  private Property<SourceType> selectedSourceType;

  @Autowired
  @Qualifier("dbConnection")
  private Property<DbConnection> dbConnectionProperty;

  @Autowired
  @Qualifier("requestProjectOpenAction")
  Property<RequestProjectOpenAction> requestProjectOpenActionProperty;

  @Autowired
  @Qualifier("requestLoadSeriesAction")
  Property<RequestLoadSeriesAction> requestLoadSeriesActionProperty;

  @Override
  public void afterPropertiesSet() throws Exception {

    this.selectedSourceType.addListener((obs, old, change) -> {
      this.dbConnectionBtn.getNode().disableProperty().setValue(this.selectedSourceType.getValue() != SupportedSourceType.DATABASE);
      this.dbLoadDataBtn.getNode().disableProperty().setValue(this.selectedSourceType.getValue() != SupportedSourceType.DATABASE);
      this.dbLoadSeriesBtn.getNode().disableProperty().setValue(this.selectedSourceType.getValue() != SupportedSourceType.DATABASE);
//      this.passwordTxtField.getNode().disableProperty().setValue(this.selectedSourceType.getValue() != SupportedSourceType.DATABASE);
//      this.urlTxtField.getNode().disableProperty().setValue(this.selectedSourceType.getValue() != SupportedSourceType.DATABASE);
//      this.userTxtField.getNode().disableProperty().setValue(this.selectedSourceType.getValue() != SupportedSourceType.DATABASE);
    });
    this.fxmlInitializer.addListener((i) -> {
      this.dbConnectionBtn.getNode().setOnAction((evt) -> {
        configureDbConnectionPopup.show();
      });
      this.dbLoadDataBtn.getNode().setOnAction((evt) -> {
        this.requestProjectOpenActionProperty.getValue().onAction((model) -> {
        });
      });

      this.dbLoadSeriesBtn.getNode().setOnAction((evt) -> {
        this.requestLoadSeriesActionProperty.getValue().onAction();
      });
      this.passwordTxtField.getNode().disableProperty().setValue(true);
      this.urlTxtField.getNode().disableProperty().setValue(true);
      this.userTxtField.getNode().disableProperty().setValue(true);
    });

    this.dbConnectionProperty.addListener((obs, old, change) -> {
      if (change != null) {
        String url = String.format("jdbc:postgresql://%s:%d/%s", new Object[]{
          change.getUrl(),
          change.getPort(),
          change.getSchema()
        });
        this.passwordTxtField.getNode().setText(change.getPassword());
        this.urlTxtField.getNode().setText(url);
        this.userTxtField.getNode().setText(change.getUser());
      }
    });

  }

}
